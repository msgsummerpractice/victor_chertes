# Implement a production-grade multi-stage build
# Requirements:
# 1. Use a non-root user
# 2. Add a HEALTHCHECK instruction
# 3. Optimize for size and security
FROM node:lts-alpine3.23 AS builder
WORKDIR /usr/src/app
COPY sources/package.json ./
RUN npm install
COPY sources/ .

FROM node:lts-alpine3.23
WORKDIR /usr/src/app

RUN apk add --no-cache curl

COPY --from=builder /usr/src/app/node_modules ./node_modules
COPY --from=builder /usr/src/app .

USER node

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8081/ || exit 1

CMD ["npm", "start"]
