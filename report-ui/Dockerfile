# Dockerfile
#FROM nginx:alpine
#COPY dist/ /usr/share/nginx/html/
#EXPOSE 80

FROM node:14-alpine3.14

RUN \
npm install -g vue-cli &&\
npm install -g cross-env

RUN mkdir /data

WORKDIR /data

