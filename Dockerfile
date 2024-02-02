FROM postgres:15.5-alpine AS db

ENV POSTGRES_USER metar_service_user
ENV POSTGRES_PASSWORD metar_service_password
ENV POSTGRES_DB metar_service_db

EXPOSE 5432