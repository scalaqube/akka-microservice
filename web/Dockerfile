FROM nginx:alpine

COPY index.html /usr/share/nginx/html


EXPOSE 80

RUN rm /etc/nginx/conf.d/*

COPY default.conf /etc/nginx/conf.d/