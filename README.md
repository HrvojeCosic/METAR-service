## Usage

### Setting up the environment
1. Create the image with external dependencies using `sudo docker build -t metar_service_image .`
2. Run the container using `sudo docker run --name metar_service_container -p 5432:5432 -d metar_service_image`

### Using the application
* Run the tests using `./gradlew test` OR the service using `./gradlew bootRun`
* Run the job scheduler using `./schedule_job.sh`

### Cleaning up
* Clean up the container using `sudo docker rm -f metar_service_container`
