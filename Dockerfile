# Użyj oficjalnego obrazu OpenJDK 21
FROM openjdk:21-jdk

# Ustaw katalog roboczy w kontenerze
WORKDIR /app

# Skopiuj plik JAR (wygenerowany przez Maven lub Gradle) do kontenera
COPY target/poker-0.0.1-SNAPSHOT.jar app.jar

# Otwórz port, na którym aplikacja będzie działać
EXPOSE 8080

# Uruchom aplikację
ENTRYPOINT ["java", "-jar", "app.jar"]

