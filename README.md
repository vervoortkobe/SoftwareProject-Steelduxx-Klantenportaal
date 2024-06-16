# SP - Steelduxx customer portal

The Steelduxx customerportal. <br>
The goal of this application is for customers and employees at Steelduxx to get an overview of:

- Their orders with detailed information
- Location of their orders
- Allow customers to see their orders and track them

# Table of contents:

- [Structure](#structure)
- [Required tasks + manual building](#required-tasks-manual-building)
  - [Frontend](#frontend)
  - [Backend](#backend)
    - [Environment variables](#environment-variables)
  - [Database](#database)
  - [Traefik (Optional)](#traefik-optional)
  - [Sentry (Optional)](#sentry-optional)
- [Automatic running](#automatic-running)
- [Gitlab](#gitlab)

# Structure

- `/api/`: Contains the backend.
- `/docker/`: Contains the docker-compose files for building in different environments.
- `/FrontEnd/project/`: Contains the frontend.
- `/Database/my.cnf`: Contains settings for a MySQL database (optional)

# Required tasks + manual building

## Frontend

The frontend is an Angular 16.2.12 application. <br>
By default, this application will communicate with the API at localhost:8080.

To run the frontend run the following command within the /FrontEnd/project/ folder. <br>
(By default it will connect to the API on port 8080).

```
ng serve
```

**REQUIRED FOR NON-LOCALHOST**: <br>
Please create a custom environment containing the URL your API is hosted on at <br>
`/FrontEnd/project/src/environments/`.

Like so:

```
export const environment = {
  production: true,
  API_URL: '//google.com:8080',
};

```

OR if you have an endpoint:

```
export const environment = {
  production: true,
  API_URL: '//google.com/api',
};
```

To specify an environment for your endpoint URL run the following command:

```
ng serve --configuration=ENVIRONMENT_NAME
```

## Backend

The backend is located within: /api/ <br>
It is a JAVA backend running on JAVA 17. <br>
To manually run, use the following command (or use an IDE of choice):

```
mvn clean compile package exec:java -D"exec.mainClass=edu.ap.softwareproject.api.ApiApplication"
```

### Environment variables

To build the backend you will need to set the following environment variables:

- SECRET_KEY: The signing key used for signing JWT tokens (HMAC).
- API_SECRET_GROUP: The admin group key used for the Steelduxx API.
- API_SECRET_KEY: The admin key used for the Steelduxx API.
- MAIL_ADDRESS: The email address used for sending emails.
- MAIL_PWD: The SMTP email password used for sending emails.
- PATH_PREFIX: The prefix that the Java API is running on (leave empty if none.)

The following environment variables are optional <br>
(everything with a \* is required for production purposes):

- spring.datasource.url\*: The database URL for SpringBoot.
- spring.datasource.username\*: The username to log into the database.
- spring.datasource.password\*: The password used to log into the database.
- server.servlet.context-path: The path the API is hosted on.
- DOMAIN\*: The domain where the application is hosted.
- SENTRY_DSN: The sentry DSN.
- SENTRY_ENABLED: Disable this if you do not use Sentry.
- SENTRY_ENVIRONMENT: The name of the environment the application is hosted on.
- SENTRY_SERVERNAME: The domain name the server is hosted on.

## Database

By default, if there is no database available, the application will be ran within memory. <br>
This database is an **H2** database.

Set the `spring.datasource.url` database url if you are using your own database. <br>
On first start, the database will be built automatically.

**REQUIRED FOR PRODUCTION**: <br>
The default administrator account that gets created can be edited in the file: <br>
`/resources/data.sql`.

Please edit the email and password (BCRYPT) in this script, <br>
or create an admin user with the id: -1, manually in the database.

Example with password `admin`:
```
INSERT IGNORE INTO `accounts` (`id`,`approved`, `email`, `password`, `role`) VALUES
(-1, 1, 'my@email.com', '$2a$10$EzOZg8WexweMcZIDNkdFdegv03GdlcbfNQV3ohS6LTflSasNdNOna', 1)
```

## Traefik (Optional)

You can use Traefik with this application, however you will have to host this yourself. <br>
By default the docker-compose file will look for the "Traefik" network, make sure you have set this up.

For more information see here: https://hub.docker.com/_/traefik

## Sentry (Optional)

You can use Sentry with this application, however you will have to host this yourself. <br>
For more information see here: https://develop.sentry.dev/self-hosted/

# Automatic running

To build and run the application automatically you can do the following: <br>
(This will run a test environment, locally.)

Run the `docker-compose.yml` file in `/docker/`:

```
docker-compose up -f docker-compose_LOCAL_TEST.yml
```

This will create a DEFAULT image.

For running the `docker-compose.yml` file, you will need to set up Traefik.

If you wish to use Traefik and/or Sentry please, use one of the environment docker-compose files and set up the following environment variables:

- PROJECT_HOST: The production domain.
- PROJECT_HOST_STAGING: The staging domain
- PROJECT_HOST_TEST: The test domain

```
docker compose -f docker-compose.yml -f docker-compose-ENVIRONMENT.yml
```

# Gitlab

This project is fully integrated with the gitlab CD/CI. <br>
See `.gitlab-ci.yml`.

**REQUIRED FOR GITLAB**: <br>
You will have to configure the following in your Settings -> CI/CD -> Variables

- DOCKER_DEFAULT_PLATFORM: The platform to build the docker image for (CPU architecture)
- MYSQL_USER: The database username.
- MYSQL_PASSWORD: The database password.
- DATASOURCE_URL: The data source URL for the SQL database.
- SECRET_KEY: The signing key for the JWT tokens.
- SENTRY_ENABLED: If Sentry is enabled or not.
- PROJECT_HOST: The production domain.
- PROJECT_HOST_STAGING: The staging domain.
- PROJECT_HOST_TEST: The test domain.

The project will build and or run on the following conditions

- Every merge request a test image will be build and removed.
- There are 3 environments which can each be built and configured:
  - PRODUCTION
  - STAGING
  - TEST

**REQUIRED FOR GITLAB**: <br>
Please note: You will have to set up Traefik for this configuration to work.

For data migration, you will have to manually migrate the data to your database of choice. <br>
(We recommend the MySQL database workbench built-in migration for this.)

For migration: You will have to move the /docker/uploads/ folder to the new location if you want to migrate uploads.
