# Crossover

This project houses **CodeServer**, an application to help manage SDLC (Software Development Life Cycle) Systems and Projects created within those systems.

## APIS

### v1 Endpoints
`GET /api/v1/sdlc-systems/{systemId}/projects/{id}` - To get a Project.

`POST /api/v1/sdlc-systems/{systemId}/projects` - To create a Project.

`PUT /api/v1/sdlc-systems/{systemId}/projects/{id}` - To update a Project.

### v2 Endpoints
`GET /api/v2/projects/{id}` - To get a Project.

`POST /api/v2/projects/` - To create a Project.

`PATCH /api/v2/projects/{id}` - To update a Project.

## Contribution

### How to Run

`./mvnw clean && ./mvnw spring-boot:run`

### How to Test
Ensure that you have Sonarqube installed and running in the background and has been connected accordingly.
* Run `./mvnw clean && ./mvnw install`
* Run `mvn clean verify sonar:sonar`