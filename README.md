## Usage
1. Create the image with external dependencies using `sudo docker build -t metar_service_image .`
2. Run the container using `sudo docker run --name metar_service_container -p 5432:5432 -d metar_service_image`
3. Run the tests using `./gradlew test` OR the server using `./gradlew bootRun`
5. Clean up the container using `sudo docker rm -f metar_service_container`
