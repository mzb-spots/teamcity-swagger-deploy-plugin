package mzb.teamcity.plugin.swagger.deployer.agent;

import jetbrains.buildServer.ExtensionHolder;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.agent.impl.artifacts.ArtifactsBuilder;
import jetbrains.buildServer.agent.impl.artifacts.ArtifactsCollection;
import jetbrains.buildServer.util.StringUtil;
import mzb.teamcity.plugin.swagger.deployer.common.SwaggerDeployerRunnerConstants;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SwaggerDeployerRunner implements AgentBuildRunner {

    protected ExtensionHolder extensionHolder;

    public SwaggerDeployerRunner(ExtensionHolder extensionHolder) {
        this.extensionHolder = extensionHolder;
    }

    @NotNull
    @Override
    public BuildProcess createBuildProcess(@NotNull final AgentRunningBuild runningBuild,
                                           @NotNull final BuildRunnerContext context) throws RunBuildException {

        final Map<String, String> runnerParameters = context.getRunnerParameters();

        final String userName = StringUtil.emptyIfNull(runnerParameters.get(SwaggerDeployerRunnerConstants.PARAM_USER_NAME));
        final String apiKey = StringUtil.emptyIfNull(runnerParameters.get(SwaggerDeployerRunnerConstants.PARAM_API_KEY));
        final String apiName = StringUtil.emptyIfNull(runnerParameters.get(SwaggerDeployerRunnerConstants.PARAM_API_NAME));
        final String version = StringUtil.emptyIfNull(runnerParameters.get(SwaggerDeployerRunnerConstants.PARAM_API_VERSION));

        final boolean privateFlag = Boolean.parseBoolean(runnerParameters.get(SwaggerDeployerRunnerConstants.PARAM_PRIVATE_FLAG));
        final boolean forceFlag = Boolean.parseBoolean(runnerParameters.get(SwaggerDeployerRunnerConstants.PARAM_FORCE_FLAG));

        final String sourcePaths = runnerParameters.get(SwaggerDeployerRunnerConstants.PARAM_SOURCE_PATH);

        final Collection<ArtifactsPreprocessor> preprocessors = extensionHolder.getExtensions(ArtifactsPreprocessor.class);

        final ArtifactsBuilder builder = new ArtifactsBuilder();
        builder.setPreprocessors(preprocessors);
        builder.setBaseDir(runningBuild.getCheckoutDirectory());
        builder.setArtifactsPaths(sourcePaths);

        final List<ArtifactsCollection> artifactsCollections = builder.build();
        String targetPath = null;
        for (ArtifactsCollection artifact : artifactsCollections) {
            Map<File, String> files = artifact.getFilePathMap();
            if (files != null && files.size() > 0) {
                targetPath = files.keySet().iterator().next().getAbsolutePath();
            }
        }
        return new SwaggerUploadBuildProcessAdapter(context, context.getBuild().getBuildLogger(), apiKey, userName, apiName, version, privateFlag, forceFlag, targetPath);
    }


    @NotNull
    @Override
    public AgentBuildRunnerInfo getRunnerInfo() {
        return new AgentBuildRunnerInfo() {
            @NotNull
            @Override
            public String getType() {
                return SwaggerDeployerRunnerConstants.SWAGGER_RUN_TYPE;
            }

            @Override
            public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
                return true;
            }
        };
    }


}
