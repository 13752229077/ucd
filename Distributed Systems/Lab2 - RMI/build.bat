::navigate to core and install from pom.xml
cd core
call mvn install
::navigate to auldfellas, install from pom.xml and package into a JAR file
cd ..\auldfellas
call mvn install
call mvn package
:: build Docker image with name:tag from the current local directory
docker build -t auldfellas:latest .
::navigate to dodgydrivers, install from pom.xml and package into a JAR file
cd ..\dodgydrivers
call mvn install
call mvn package
:: build Docker image with name:tag from the current local directory
docker build -t dodgydrivers:latest .
::navigate to girlpower, install from pom.xml and package into a JAR file
cd ..\girlpower
call mvn install
call mvn package
:: build Docker image with name:tag from the current local directory
docker build -t girlpower:latest .
::navigate to client, install from pom.xml and package into a JAR file
cd ..\client
call mvn install
call mvn package
:: build Docker image with name:tag from the current local directory
docker build -t client:latest .
::navigate to broker, install from pom.xml and package into a JAR file
cd ..\broker
call mvn install
call mvn package
:: build Docker image with name:tag from the current local directory
docker build -t broker:latest .
:: go up to parent folder
cd ..
:: start created containers
call docker-compose up