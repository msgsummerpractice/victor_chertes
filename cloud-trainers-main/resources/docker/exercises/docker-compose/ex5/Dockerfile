FROM node:lts-alpine3.23
WORKDIR /usr/src/app
ARG APP_COLOR=white
ENV BG_COLOR=${APP_COLOR}
COPY sources/package.json ./
RUN npm install
COPY sources .
EXPOSE 8081
CMD ["npm", "start"]
