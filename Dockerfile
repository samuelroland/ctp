# CTP Docker image used to easily use the CLI without installing more dependencies than Docker

# Build Java support
FROM gradle:jdk17-alpine as buildjar
ADD "https://api.github.com/repos/samuelroland/plantuml-parser/commits?per_page=1" latest_commit
RUN git clone https://github.com/samuelroland/plantuml-parser.git /plantuml-parser
WORKDIR /plantuml-parser
RUN gradle uberJar --no-daemon

# Final image with Java support
FROM eclipse-temurin:21-alpine
COPY --from=buildjar /plantuml-parser/plantuml-parser-cli/build/libs/plantuml-parser-cli-0.0.1-uber.jar /cli/plantuml-parser-cli.jar

## Add python support
RUN apk add --update --no-cache python3 py3-pip git && ln -sf python3 /usr/bin/python
ADD "https://api.github.com/repos/samuelroland/hpp2plantuml/commits?per_page=1" latest_commit
RUN git clone https://github.com/samuelroland/hpp2plantuml.git /hpp2plantuml
WORKDIR /hpp2plantuml
RUN pip install . --no-cache-dir --break-system-packages

# Final CLI
COPY *.java .
RUN javac CLI.java
RUN javac PostMix.java

ENTRYPOINT ["java", "CLI"]
