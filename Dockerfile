# JTPC Docker image used to easily use the CLI without installing more than Docker

# Build Java support
FROM gradle:jdk17-alpine as buildjar
RUN git clone https://github.com/samuelroland/plantuml-parser.git /plantuml-parser
WORKDIR /plantuml-parser
RUN gradle uberJar --no-daemon

# Build Python support
WORKDIR /plantuml-parser
RUN gradle uberJar --no-daemon

# Final image with Java support
FROM eclipse-temurin:21-alpine
WORKDIR /cli

COPY --from=buildjar /jtpc/plantuml-parser-cli/build/libs/plantuml-parser-cli-0.0.1-uber.jar /cli/plantuml-parser-cli.jar
# COPY plantuml-parser-cli/build/libs/plantuml-parser-cli-0.0.1-uber.jar /cli/plantuml-parser-cli.jar
# RUN cp /jtpc/plantuml-parser-cli/build/libs/plantuml-parser-cli-0.0.1-uber.jar /cli/plantuml-parser-cli.jar


COPY jtpc/PostMix.java .
RUN javac PostMix.java

COPY jtpc/jtpc.sh /cli/jtpc.sh

## Add python support
RUN apk add --update --no-cache python3 && ln -sf python3 /usr/bin/python
RUN git clone https://github.com/samuelroland/hpp2plantuml.git /hpp2plantuml
WORKDIR /hpp2plantuml


ENTRYPOINT ["sh", "/cli/jtpc.sh"]
