# Recommendation Service for Cryptos
•After cloning it from Git open a command line and run the following

•To install it run mvn install

•To run the application run mvn spring-boot:run

•The application will start on port 8081, it can be changed from the application.properties file


To run with Docker run:

•docker build -t crypto-recommendation-service 

•docker run -p 8081:8081 crypto-recommendation-service

Access  the application http://localhost:8081

To stop the container run:

•docker ps


To deploy in Kubernetes run: 

•kubectl apply -f deployment.yaml

•kubectl apply -f service.yaml
