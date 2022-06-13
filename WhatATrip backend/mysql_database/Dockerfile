FROM mysql:latest

COPY ./init_trip.sql /tmp

CMD [ "mysqld", "--init-file=/tmp/init_trip.sql" ]