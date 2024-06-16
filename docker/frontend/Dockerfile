ARG DOCKER_DEFAULT_PLATFORM

FROM --platform=${DOCKER_DEFAULT_PLATFORM} node:18.13-alpine as build

ARG ENVIRONMENT

WORKDIR /app

RUN npm install -g @angular/cli

COPY ./ /app

RUN npm install

RUN node --max_old_space_size=600 ./node_modules/@angular/cli/bin/ng build --configuration=${ENVIRONMENT} 

FROM --platform=${DOCKER_DEFAULT_PLATFORM} nginx as runtime

COPY --from=build /app/dist/project/ /usr/share/nginx/html