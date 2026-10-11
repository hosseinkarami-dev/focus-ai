FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew -p aiBackend installDist --no-daemon

FROM eclipse-temurin:21-jre
RUN useradd --system --no-create-home --uid 10001 focusai
WORKDIR /app
COPY --from=build --chown=10001:10001 /workspace/aiBackend/build/install/focusai-ai-backend/ ./
USER 10001:10001
EXPOSE 8080
ENTRYPOINT ["/app/bin/focusai-ai-backend"]
