FROM node:lts-alpine3.23
WORKDIR /usr/src/app
COPY sources/package.json ./
RUN npm install
COPY sources/ .
CMD ["npm", "start"]
