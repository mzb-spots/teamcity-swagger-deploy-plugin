package mzb.teamcity.plugin.swagger.deployer.server;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.StringUtil;
import mzb.teamcity.plugin.swagger.deployer.common.SwaggerDeployerRunnerConstants;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

class SwaggerDeployerPropertiesProcessor implements PropertiesProcessor {

    @Override
    public Collection<InvalidProperty> process(Map<String, String> properties) {
        final Collection<InvalidProperty> result = new HashSet<>();

        if (StringUtil.isEmptyOrSpaces(properties.get(SwaggerDeployerRunnerConstants.PARAM_USER_NAME))) {
            result.add(new InvalidProperty(SwaggerDeployerRunnerConstants.PARAM_USER_NAME, "Username must be specified."));
        }
        if (StringUtil.isEmptyOrSpaces(properties.get(SwaggerDeployerRunnerConstants.PARAM_API_NAME))) {
            result.add(new InvalidProperty(SwaggerDeployerRunnerConstants.PARAM_API_NAME, "API name must be specified."));
        }
        if (StringUtil.isEmptyOrSpaces(properties.get(SwaggerDeployerRunnerConstants.PARAM_API_KEY))) {
            result.add(new InvalidProperty(SwaggerDeployerRunnerConstants.PARAM_API_KEY, "API access token must be specified."));
        }
        if (StringUtil.isEmptyOrSpaces(properties.get(SwaggerDeployerRunnerConstants.PARAM_API_VERSION))) {
            result.add(new InvalidProperty(SwaggerDeployerRunnerConstants.PARAM_API_VERSION, "API version must be specified."));
        }

        return result;
    }
}
