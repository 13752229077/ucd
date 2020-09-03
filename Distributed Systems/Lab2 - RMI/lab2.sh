cd core
mvn install
cd ../auldfellas
mvn install
mvn package
sudo docker build -t auldfellas:latest .
cd ../dodgydrivers
mvn install
mvn package
sudo docker build -t dodgydrivers:latest .
cd ../girlpower
mvn install
mvn package
sudo docker build -t girlpower:latest .
cd ../client
mvn install
mvn package
sudo docker build -t client:latest .
cd ../broker
mvn install
mvn package
sudo docker build -t broker:latest .
cd ..
docker-compose up
