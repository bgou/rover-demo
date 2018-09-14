# Recover Rover.com

## Architecture Overview

1. Backend: Spring boot
2. Frontend: React

## How to run

### Prerequisite

`docker-compose.yml` includes a basic version of MySQL setup. Run this command to stand up a MySQL docker container:

```bash
docker-compose up -d
```

_Note: I could've chosen MariaDB as it's not controlled by Oracle, but for this exercise I just wanted something quick._

Please note: this will expose port `3306`, please ensure the host doesn't have an existing port mapping.

### Start backend service

Make sure you have `java 8` on your path.

```bash
./gradlew bootrun
```

This does the following:

1. Compile, download dependencies, build, and run the spring boot application.
2. Upon init, it'll also recover the database (ensure the MySQL port is initialized on `3306`).
3. It also serves as an API server for the front end.

#### Framework choices:

1. spring-boot for overall framework
2. spring-data-rest (auto adapt models to rest api's)

### Start the front end UI

Ensure you have `node v10+` installed, and also yarn (`brew install yarn`).

```bash
cd src/ui
yarn
yarn start
```

This does the following:

1. Install required dependencies.
2. Start a webpack dev server
3. Open the browser to `http://localhost:3000` automatically

#### Framework choices:

1. React
2. Material-UI

## Processing reviews

Chose opencsv library for its versatility and simplicity.
Supports streaming input, so we don't overload the memory with file data all at once.
For the time being, I'm parsing the entire file at once and load the list into memory. But it can be easily configured to support streaming processing.

### Assumptions

1. Sitter's info does not vary by properties. 
   When reading the review entries, we consider it to be the same sitter if all of the following match:
    - name
    - phone
    - email
    - image

## FAQ

1. After I stop the service, why are all the entries gone in the database?
    
    Open `src/main/resources/application.properties`, change 
    
    ```
    spring.jpa.hibernate.ddl-auto=create
    ```
    
    to
    
    ```
    spring.jpa.hibernate.ddl-auto=none
    ```
    
    One can also run `curl http://localhost:8080/init` to cause it to rerun the recover command.

2. Why isn't the UI not served by Spring framework?

    This enables easy horizontal scaling. Separating the UI and backend services the static assets can be deployed to CDN without taking up server resources.
    
    Ideally I'd move the UI to a separate repo altogether which we can setup separate deployment pipeline and workflow.

3. What's the logging solution?

    I use `log4j2`. You can tweak the configuration in `log4j2.yml`. Try change `info` to `debug` for more detailed logs.

4. What's the testing strategy?

   The core gist of the service has 100% test coverage. You can run the test by

   ```bash
   ./gradlew test
   ```
   
   I didn't add any tests for the UI. If I had more time I'd like to add tests around UI controls (switching pages) that generates the correct API requests.
   
   To take it to the next level, more integration tests between the front end and the backend services can be setup to ensure API consistency.
   