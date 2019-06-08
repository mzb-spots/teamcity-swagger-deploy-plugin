### TeamCity plugin to validate and upload swagger OpenAPI specification artifact to swaggerhub (https://swagger.io/tools/swaggerhub/)
----------------------------
https://github.com/mzb-spots/teamcity-swagger-deploy-plugin

This plugin adds swagger/openapi file deployment functions
to TeamCity continuous integration server
http://www.jetbrains.com/teamcity/

#### Installation

Copy zip file to `%TeamCity_data_dir%/plugins` and restart the server. Build agent will be updated automatically

#### Purpose

While developing REST microservices application this plugin can be helpful to publish swagger/openapi files to swaggerhub.

Swaggerhub is quite popular public platform allowing to host REST interfaces (also keeping track of versions) and providing all power of integrated swagger tools.

So every time when your TeamCity builds application a new version of REST API will be uploaded to swaggerhub,
 so every member of team has access to the most fresh version of API and also has access to entire API history.     

#### Usage

In build configuration settings, first look for a new runner "swagger upload".
Having chosen this build runner, specify path to swagger file and provide necessary configuration options (prior this obtain api access token from swaggerhub)   

![Settings](/readme-files/step_configuration.png)

Observe build progress and click on resulting link:

![Results](/readme-files/pic_logs.png)

After build is successful check that swaggerhub has new interface version:

![Results](/readme-files/pic_published.png)

#### Playground

Have a look at a simple demo project that features simple REST API (list of space rockets) and comprises swagger/openapi files (yaml and json versions) 
Just create project/build configuration that points at this project and configure "swagger upload" plugin.  

#### License

Apache, version 2.0
http://www.apache.org/licenses/LICENSE-2.0
