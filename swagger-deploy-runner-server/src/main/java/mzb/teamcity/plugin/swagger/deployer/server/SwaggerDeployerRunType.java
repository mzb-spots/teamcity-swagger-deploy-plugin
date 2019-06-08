package mzb.teamcity.plugin.swagger.deployer.server;

import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import mzb.teamcity.plugin.swagger.deployer.common.SwaggerDeployerRunnerConstants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SwaggerDeployerRunType extends RunType {

    private final PluginDescriptor myDescriptor;

    public SwaggerDeployerRunType(@NotNull final RunTypeRegistry registry,
                                  @NotNull final PluginDescriptor descriptor) {
        myDescriptor = descriptor;
        registry.registerRunType(this);
    }

    @NotNull
    @Override
    public String getType() {
        return SwaggerDeployerRunnerConstants.SWAGGER_RUN_TYPE;
    }

    @Override
    public String getDisplayName() {
        return "Swagger Upload";
    }

    @Override
    public String getDescription() {
        return "Deploys swagger artifact to swagger hub";
    }

    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return new SwaggerDeployerPropertiesProcessor();
    }

    @Override
    public String getEditRunnerParamsJspFilePath() {
        return myDescriptor.getPluginResourcesPath() + "editSwaggerDeployerParams.jsp";
    }

    @Override
    public String getViewRunnerParamsJspFilePath() {
        return myDescriptor.getPluginResourcesPath() + "viewSwaggerDeployerParams.jsp";
    }

    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        return new HashMap<String, String>();
    }

    @NotNull
    @Override
    public String describeParameters(@NotNull Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        //sb.append("SwaggerHub API token: ").append(parameters.get(SwaggerDeployerRunnerConstants.PARAM_API_KEY));
        sb.append("User name: ").append(parameters.get(SwaggerDeployerRunnerConstants.PARAM_USER_NAME)).append("\n");
        sb.append("API name: ").append(parameters.get(SwaggerDeployerRunnerConstants.PARAM_API_NAME)).append("\n");
        sb.append("API version: ").append(parameters.get(SwaggerDeployerRunnerConstants.PARAM_API_VERSION)).append("\n");
        sb.append("Private API: ").append(parameters.get(SwaggerDeployerRunnerConstants.PARAM_PRIVATE_FLAG)).append("\n");
        sb.append("Force Overwrite: ").append(parameters.get(SwaggerDeployerRunnerConstants.PARAM_FORCE_FLAG));
        return sb.toString();
    }

}
