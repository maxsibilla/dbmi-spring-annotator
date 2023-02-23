# dbmi-spring-annotator

## Annotator File Directory

The annotator file directory contains  the required documents to make this web tool function properly such as the HTML pages, videos with subtitles, and figures. You may email us for a link that contains a sample directory.

Once the directory is downloaded you must configure Tomcat's `server.xml` file to specify a context in order for the tool to be able to access the directory:

````
<Host name="localhost"  appBase="webapps"unpackWARs="true" autoDeploy="true">

            <Context docBase="path_to_/annotator-file-dir" path="/annotator-file-dir" />
       ...
< /Host>
````
One important file to note is `usersConfig.json`. This file is used to control what files each user has the ability view, whether or not annotations are enabled for that file, and the URL to a pre and post test for each file. 

## JPA
Spring Annotator utilizes Spring Data JPA to store and retrieve annotations, users, and analytics in a PostgreSQL database. After creating your own database, configure the `fake_application.properties` file located in the `resources` directory with your database settings then save this as a new file called `application.properties`. On startup the required tables will be created. 

Initially the database will not contain any users, files, or annotations. The following sections will assist you in adding the necessary data.

### Users
In the database there is a `users`, `role`, and `users_roles` table. The `users` table contains the necessary user information such as email, username, and password. It also contains the fields "next_file_to_complete" and "uncompleted_files". These fields are used to control studies as it prevents files from being viewed until the previous file is marked "complete" by the user. These fields are generated automatically from the `usersConfig.json` file located in the annotator file directory. A user can be added through the web tool by going to `/spring-annotator/registration`. 

The `role` table contains the available roles for users. For our purpose we created two roles: "Registered", which was the default role assigned study participants, and "Editor", which allows the user to create annotations and view analytics.

The final table, `users_role`, contains a mapping of what role each user has. This is the table to modify if you want to assign a user the role of "Editor".

### Files
The `files` table contains the available files that can potentially be viewed in the tool. The `uri` is the encoded string that will be used to access the file, `url` is raw name of the file located in the annotator file directory, and `display_name` is the name of the file that will be presented to the user. If the file is a video you can also set the `start_time` and `end_time` fields to be the second values of the video that you want to limit the video to.

### Annotations
Annotations can be added in one of two ways: directly via the tool if the user has the proper role by highlighting a word, or by utilizing a few functions located in `HomeController.java` and files located in `/annotator-file-dir/pre-annotation`. 

To create annotations set the variable "preAnnotationType" to the proper tag you will be creating ('enlgish' or 'scientific') and set the model attribute "addPreAnnotation" to true. The "annotationFile" is a CSV that contains the word, definition, the video file name and subtitle file associated with it in a comma separated cell, and the image file names separated by comma located in the annotator file directory.

To create pre-annotations for a video with subtitles you must set "subtitleFile" to a TXT file of the subtitles (normal format is VTT) then call "createSubtitlePreAnnotations".
