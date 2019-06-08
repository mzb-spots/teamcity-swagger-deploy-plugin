<%@ page import="mzb.teamcity.plugin.swagger.deployer.common.SwaggerDeployerRunnerConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
    API token key: <strong><props:displayValue name="<%=SwaggerDeployerRunnerConstants.PARAM_API_KEY%>"
                                               emptyValue="none"/></strong>
</div>

<div class="parameter">
    Username: <strong><props:displayValue name="<%=SwaggerDeployerRunnerConstants.PARAM_USER_NAME%>"
                                          emptyValue="none"/></strong>
</div>

<div class="parameter">
    API name: <strong><props:displayValue name="<%=SwaggerDeployerRunnerConstants.PARAM_API_NAME%>"
                                          emptyValue="none"/></strong>
</div>

<div class="parameter">
    API version: <strong><props:displayValue name="<%=SwaggerDeployerRunnerConstants.PARAM_API_VERSION%>"
                                             emptyValue="none"/></strong>
</div>

<div class="parameter">
    API is private: <strong><props:displayValue name="<%=SwaggerDeployerRunnerConstants.PARAM_PRIVATE_FLAG%>"
                                                emptyValue="none"/></strong>
</div>

<div class="parameter">
    Overwrite API version: <strong><props:displayValue name="<%=SwaggerDeployerRunnerConstants.PARAM_FORCE_FLAG%>"
                                                       emptyValue="none"/></strong>
</div>


<div class="parameter">
    Source: <strong><props:displayValue name="<%=SwaggerDeployerRunnerConstants.PARAM_SOURCE_PATH%>"
                                        emptyValue="none"/></strong>
</div>
