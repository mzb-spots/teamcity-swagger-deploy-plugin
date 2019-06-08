<%@ page import="mzb.teamcity.plugin.swagger.deployer.common.SwaggerDeployerRunnerConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="runnerConst" scope="request"
             class="mzb.teamcity.plugin.swagger.deployer.common.SwaggerDeployerRunnerConstants"/>

<l:settingsGroup title="SwaggerHub API Deployment Target">
    <tr>
        <th><label for="mzb.spots.swagger.deployer.apiKey">API key: <l:star/></label></th>
        <td><props:textProperty name="<%=SwaggerDeployerRunnerConstants.PARAM_API_KEY%>" className="longField"
                                maxlength="256"/>
            <span class="smallNote">Enter api token key for swaggerhub</span><span
                    class="error" id="error_mzb.spots.swagger.deployer.apiKey"></span>
        </td>
    </tr>

    <tr>
        <th><label for="mzb.spots.swagger.deployer.userName">User Name: <l:star/></label></th>
        <td><props:textProperty name="<%=SwaggerDeployerRunnerConstants.PARAM_USER_NAME%>" className="longField"
                                maxlength="256"/>
            <span class="smallNote">Enter User name on swaggerhub</span><span
                    class="error" id="error_mzb.spots.swagger.deployer.userName"></span>
        </td>
    </tr>
    <tr>
        <th><label for="mzb.spots.swagger.deployer.apiName">API Name: <l:star/></label></th>
        <td><props:textProperty name="<%=SwaggerDeployerRunnerConstants.PARAM_API_NAME%>" className="longField"
                                maxlength="256"/>
            <span class="smallNote">Enter API name on swaggerhub</span><span
                    class="error" id="error_mzb.spots.swagger.deployer.apiName"></span>
        </td>
    </tr>
    <tr>
        <th><label for="mzb.spots.swagger.deployer.userName">API Version: <l:star/></label></th>
        <td><props:textProperty name="<%=SwaggerDeployerRunnerConstants.PARAM_API_VERSION%>" className="longField"
                                maxlength="256"/>
            <span class="smallNote">Enter API version</span><span
                    class="error" id="error_mzb.spots.swagger.deployer.apiVersion"></span>
        </td>
    </tr>
    <tr>
        <th><label for="mzb.spots.swagger.deployer.privateFlag">Private API: <l:star/></label></th>
        <td><props:checkboxProperty name="<%=SwaggerDeployerRunnerConstants.PARAM_PRIVATE_FLAG%>" className="longField"
                                    uncheckedValue="false"
        />
            <span class="smallNote">Check if published API is private</span><span
                    class="error" id="error_mzb.spots.swagger.deployer.privateFlag"></span>
        </td>
    </tr>
    <tr>
        <th><label for="mzb.spots.swagger.deployer.forceFlag">Force Overwrite API: <l:star/></label></th>
        <td><props:checkboxProperty name="<%=SwaggerDeployerRunnerConstants.PARAM_FORCE_FLAG%>" className="longField"
                                    uncheckedValue="false"
        />
            <span class="smallNote">Check if published version can be overwritten</span><span
                    class="error" id="error_mzb.spots.swagger.deployer.forceFlag"></span>
        </td>
    </tr>
</l:settingsGroup>

<l:settingsGroup title="Deployment Source">
    <tr>
        <th><label for="jetbrains.buildServer.deployer.sourcePath">Paths to Sources: <l:star/></label></th>
        <td>
            <props:multilineProperty name="<%=SwaggerDeployerRunnerConstants.PARAM_SOURCE_PATH%>" className="longField"
                                     cols="30" rows="" expanded="true"
                                     linkTitle="Enter paths to swagger.yaml or swagger.json file"/>
            <span class="smallNote">Path to swagger file
        </td>
    </tr>
</l:settingsGroup>
