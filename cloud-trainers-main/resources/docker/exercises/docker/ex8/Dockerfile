FROM node:lts-alpine3.23
WORKDIR /usr/src/app
ARG APP_VERSION=1.0.0
ENV GREETING="Hello from Docker!"

COPY sources/package.json ./
RUN npm install
COPY sources/ .

RUN echo "Version: $APP_VERSION" > version.txt

CMD ["npm", "start"]
