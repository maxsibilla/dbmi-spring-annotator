# dbmi-spring-annotator

Configure tomcat's server.xml file to specify a context. Make sure in IntelliJ "Deploy application configured in Tomcat instance" is checked in the run configurations.

<Host name="localhost"  appBase="webapps"unpackWARs="true" autoDeploy="true">

            <Context docBase="/Users/maxsibilla/Documents/Dev/annotator-file-dir" path="/annotator-file-dir" />

       ...
< /Host>
