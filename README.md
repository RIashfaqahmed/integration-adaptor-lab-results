# integration-adaptor-lab-results
Integration Adaptor to simplify processing of Pathology and Screening results

## Development

The following sections provide the necessary information to develop the Integration Adaptor.

### Pre-requisites (IntelliJ)

* Install a Java JDK 11. [AdoptOpenJdk](https://adoptopenjdk.net/index.html?variant=openjdk11&jvmVariant=hotspot) is recommended.
* Install [IntelliJ](https://www.jetbrains.com/idea/)
* Install the [Lombok plugin](https://plugins.jetbrains.com/plugin/6317-lombok)

### Import the integration-adaptor-nhais project

* Clone this repository
* Open the cloned `integration-adaptor-lab-results` folder
* Click pop-up that appears: (import gradle daemon)
* Verify the project structure

### Running

**From IntelliJ***

Navigate to: IntegrationAdapterLabResultsApplicationTests -> right click -> Run

### Running Tests

**All Tests**

    ./gradlew check

**Integration Tests**

A separate source folder [src/intTest](./src/intTest) contains integration tests. To run the integration tests use:

    ./gradlew integrationTest


