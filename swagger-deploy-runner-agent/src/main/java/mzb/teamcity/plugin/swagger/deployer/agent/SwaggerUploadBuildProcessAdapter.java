package mzb.teamcity.plugin.swagger.deployer.agent;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.*;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import io.swagger.models.Swagger;
import io.swagger.parser.OpenAPIParser;
import io.swagger.parser.Swagger20Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import jetbrains.buildServer.BuildProblemData;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProcessAdapter;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.log.Loggers;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class SwaggerUploadBuildProcessAdapter extends BuildProcessAdapter {
    private static final String targetPostUrlTemplate = "https://api.swaggerhub.com/apis/%user%/%apiName%?isPrivate=%private%&version=%version%&oas=%oas%&force=%force%";
    private static final String targetGetUrlTemplate = "https://app.swaggerhub.com/apis/%user%/%apiName%/%version%";
    private static final String logIdentity = "swagger-upload";
    private static final String logTypeId = "default";

    private final String targetPath;
    private boolean interruptedFlag;
    private boolean finishedFlag;
    private BuildFinishedStatus resultStatus;
    private BuildRunnerContext context;
    private BuildProgressLogger buildProgressLogger;
    private String token;
    private String userName;
    private String apiName;
    private String version;
    private boolean privateFlag;
    private boolean forceFlag;

    private String content;
    private String oas;
    private MediaType mimeType;

    public SwaggerUploadBuildProcessAdapter(@NotNull final BuildRunnerContext context,
                                            @NotNull final BuildProgressLogger buildProgressLogger,
                                            @NotNull final String token,
                                            @NotNull final String userName,
                                            @NotNull final String apiName,
                                            @NotNull final String version,
                                            final boolean privateFlag,
                                            final boolean forceFlag,
                                            final String targetPath) {

        this.context = context;
        this.buildProgressLogger = buildProgressLogger;
        this.token = token;
        this.userName = userName;
        this.apiName = apiName;
        this.privateFlag = privateFlag;
        this.forceFlag = forceFlag;
        this.version = version;
        this.targetPath = targetPath;
    }

    @Override
    public void interrupt() {
        interruptedFlag = true;
    }

    @Override
    public boolean isInterrupted() {
        return interruptedFlag;
    }

    @Override
    public boolean isFinished() {
        return finishedFlag;
    }

    @NotNull
    @Override
    public BuildFinishedStatus waitFor() throws RunBuildException {
        while (!isInterrupted() && !isFinished()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RunBuildException(e);
            }
        }
        return isFinished() ? resultStatus : BuildFinishedStatus.INTERRUPTED;
    }

    @Override
    public void start() throws RunBuildException {
        try {
            if (targetPath == null) {
                resultStatus = BuildFinishedStatus.FINISHED_FAILED;
                finishedFlag = true;
                return;
            }
            try {
                buildProgressLogger.message("reading file " + targetPath + " content");
                content = FileUtils.readFileToString(new File(targetPath), "utf-8");
                buildProgressLogger.message("file " + targetPath + " content was read successfully");
            } catch (IOException e) {
                final String msg = "unable to locate swagger artifact: " + String.valueOf(e);
                Loggers.AGENT.error(msg, e);
                buildProgressLogger.logBuildProblem(buildProblem(msg));
                resultStatus = BuildFinishedStatus.FINISHED_FAILED;
                finishedFlag = true;
                return;
            }

            validateFleStructure();

            validateFileSpec();

            publish();

            resultStatus = BuildFinishedStatus.FINISHED_SUCCESS;
            finishedFlag = true;
        } catch (Exception e) {
            final String msg = "unable to validate and publish swagger artifact: " + String.valueOf(e);
            Loggers.AGENT.error(msg, e);
            buildProgressLogger.logBuildProblem(buildProblem(msg));
            resultStatus = BuildFinishedStatus.FINISHED_FAILED;
            interruptedFlag = true;
        }
    }

    private void validateFileSpec() {
        buildProgressLogger.message("validating swagger file content");
        String msg = null;
        if (oas.startsWith("2")) {
            try {
                Swagger parse = new Swagger20Parser().parse(content);
                if (parse == null) {
                    msg = "swagger file contains a number of errors and is invalid";
                }
            } catch (Exception e) {
                msg = "swagger file cannot be parsed because of " + String.valueOf(e);
            }
        } else {
            SwaggerParseResult result = new OpenAPIParser().readContents(content, null, null);
            if (!CollectionUtils.isEmpty(result.getMessages())) {
                msg = "swagger file cannot be parsed because of " + String.valueOf(result.getMessages());
            }
        }
        if (msg == null) {
            buildProgressLogger.message("swagger file content was validated and no errors have been found");
        } else {
            buildProgressLogger.logBuildProblem(buildProblem(msg));
            throw new RuntimeException(msg);
        }
    }

    @NotNull
    private BuildProblemData buildProblem(String msg) {
        return BuildProblemData.createBuildProblem(logIdentity, logTypeId, msg);
    }

    private void publish() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        buildProgressLogger.message(message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY)
        );


        String targetUrl = targetPostUrlTemplate
                .replace("%user%", userName)
                .replace("%apiName%", apiName)
                .replace("%version%", version)
                .replace("%oas%", oas)
                .replace("%private%", String.valueOf(privateFlag))
                .replace("%force%", String.valueOf(forceFlag));
        Request request = new Request.Builder()
                .url(targetUrl)
                .header("Authorization", "Bearer " + token)
                .post(RequestBody.create(mimeType, content))
                .build();


        buildProgressLogger.message("sending publish request to url " + targetUrl);
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        buildProgressLogger.message("response received from swaggerhub " + response);
        if (response.code() != 201) {
            throw new RuntimeException("swaggerhub responded with error " + response);
        }
        targetUrl = targetGetUrlTemplate
                .replace("%user%", userName)
                .replace("%apiName%", apiName)
                .replace("%version%", version);

        buildProgressLogger.message("published api is accessible at " + targetUrl);
    }

    private void validateFleStructure() {
        mimeType = MediaType.parse("application/json; charset=utf-8");
        try {
            JsonElement jelement = new JsonParser().parse(content);
            JsonObject jobject = jelement.getAsJsonObject();
            oas = jobject.get("swagger").getAsString();
            oas = oas != null ? oas : jobject.get("openapi").getAsString();
        } catch (JsonSyntaxException e) {
            try {
                Yaml yaml = new Yaml();
                Map<String, Object> obj = yaml.load(content);
                oas = (String) obj.get("swagger");
                oas = oas != null ? oas : (String) obj.get("openapi");
                mimeType = MediaType.parse("application/yaml; charset=utf-8");
            } catch (Exception e1) {
                throw new RuntimeException("specified swagger artifact file is neither correct json nor yaml file", e1);
            }
        }
        if (oas == null || oas.trim().length() == 0) {
            oas = "2.0";
        }
        buildProgressLogger.message("assuming version of swagger/openapi file as " + oas);
    }


}
