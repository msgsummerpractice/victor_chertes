# Read README.adoc for instructions on how to use this exercise
FROM node:lts-alpine3.23
WORKDIR usr/src/app
COPY sources/package.json ./
RUN npm install
COPY sources/ .
EXPOSE 8081
CMD ["npm", "start"]