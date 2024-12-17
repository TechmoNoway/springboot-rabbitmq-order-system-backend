#FROM rabbitmq:3.12-management-alpine AS rabbitmq
#RUN rabbitmq-plugins enable --offline rabbitmq_stomp rabbitmq_web_stomp rabbitmq_management
#FROM rabbitmq:3.12-alpine
#COPY --from=rabbitmq /etc/rabbitmq/enabled_plugins /etc/rabbitmq/enabled_plugins
#EXPOSE 5672 15672 15674 61613
#ENV RABBITMQ_DEFAULT_USER=myuser \
#    RABBITMQ_DEFAULT_PASS=mypassword
#HEALTHCHECK --interval=30s --timeout=10s \
#    CMD rabbitmqctl status || exit 1

FROM rabbitmq:3.12-management-alpine AS rabbitmq
RUN rabbitmq-plugins enable --offline rabbitmq_management rabbitmq_stomp rabbitmq_web_stomp
RUN apk add --no-cache curl && \
    rm -rf /var/cache/apk/*
EXPOSE 5672 15672 15674 61613
HEALTHCHECK --interval=30s --timeout=10s --retries=5 \
    CMD rabbitmq-diagnostics -q ping || exit 1
CMD ["rabbitmq-server"]