//////////////////////////////
//////////TIME TAKEN//////////
///////4 hrs and 40 min///////
///excluding making this doc//
//////////////////////////////

Implementation choices
 * I've deceided let the AbstractBaseEntity (super class of the entitie) assign id's on creation to Songs and Artists for the following reason:
    * simplify the adding process (id doesn't have to be passed in this way) and since Hibernate keeps track of the ID we don't have to worry about this.
    
 * the service class functions that are being invoked in the controllers expect the (if asked for path variable) and request body to be filled (hence no extensive validation in the service implementations) (some api's have more strict requirements and whould require payload validation.
 
    
Implemented:
* Api following spec (my interpretation that is).
* Used a non-in memory database MySQL to persist data
* Used Docker to run the (local) MySql Database
* Used Http Basic Auth for basic authentication on the API (I've used Oauth aswell (and also Ldap in combination with SSO applications))
* Tried to "conform" to the restfull guilelines as much as possible 



What whould I implement further?
* Due to the given timeframe, I failed to unit test all of the application (I focussed on functionality first since the assignment stated to focus on that part).
  - missing tests for controllers (i would do this with mockito)
* Better security (plain passwords are a bad idea, this was just to demonstrate)
* Would i have the time I would spec the API in OpenApi with a Yaml and that would help with the following:
    - this would give us visual documentation 
    - a generated client to use in the restcontrollers 
    - validation (given you spec it in the Yaml)
    - the possibility to version the API (updates are easy to do)



TO GET STARTED:

(working from macOS && assuming docker is installed locally)

* Run a docker container for the database with the following command:
docker run -p 127.0.0.1:3306:3306  --name rockstar -e MYSQL_ROOT_PASSWORD=rockstar -d mysql:5.7
* validate if the container is up with the command:
docker ps 

(Worked with IntelliJ Ultimate and DataGrip)
* Connect to the database with the following Credentials

Host = localhost (we run it locally)
username = root 
password = rockstar

* Paste and execute the contents of src/main/resources/before_start.sql 
the command above creates a user/password (which corresponds to the the connection credentials in the properties)

Run the RockstarApplication with activeprofile=dev this the following will happen:
* The entities will be generated (artist & song)
* On an ApplicationReadyEvent the JsonDocumentParser.class will attemt to read the (root folder) provided songs.json and artist.json
    * note: because we use a real database we don't want the parser to run more than 1 time if you restart, so it checks whether there are records present at startup

* You can use the API in PostMan afterwards
    * note: enable basicAuth for your requests (username: rockstar / password: password) (I know, unsafe :-) )
    
    
Endpoints: (basepath: http://localhost:8081/rest)
*Artists (/artist)
    - GET    - /(path variable){artistName}
    - POST   - /(requestbody)
    - DELETE - /(path variable){artistId} (note: this will in cascade, delete the songs of the artist aswell.)
    - PUT    - /(path variable){artistId} + (requestbody) 
    
    
*Songs (/song)
    - GET    - /(path variable){genre}
    - POST   - /(requestbody)
    - DELETE - /(path variable){songId} 
    - PUT    - /(path variable){songId} + (requestbody) 