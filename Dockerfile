FROM openjdk:17-jdk-alpine

# Variables d’environnement Nexus (évite d’inclure des secrets en dur en production)
ENV NEXUS_USERNAME=admin
ENV NEXUS_PASSWORD=Mohabir2310-

# Dossier de travail
WORKDIR /app

# Port exposé par ton application Spring Boot
EXPOSE 8089

# Installer curl et récupérer le JAR depuis Nexus
RUN apk add --no-cache curl && \
    curl -u ${NEXUS_USERNAME}:${NEXUS_PASSWORD} -O http://192.168.50.4:8081/repository/maven-releases/tn/esprit/tp-foyer/5.0.0/tp-foyer-5.0.0.jar

# Point d'entrée
ENTRYPOINT ["java", "-jar", "tp-foyer-5.0.0.jar"]
