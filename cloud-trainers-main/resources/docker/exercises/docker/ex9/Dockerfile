# Implement a multi-stage build here
FROM node:lts-alpine3.23 AS builder
WORKDIR /usr/src/app
COPY sources/package.json ./
RUN npm install
COPY sources/ .

FROM node:lts-alpine3.23
WORKDIR /usr/src/app

COPY --from=builder /usr/src/app/node_modules ./node_modules
COPY --from=builder /usr/src/app .
CMD ["npm", "start"]
