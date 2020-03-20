# Nom de l'image utilisé
FROM openjdk:9-jdk

# Permet d'avoir les fichiers en local (pas dans le container)
VOLUME /tmp

# On place le jag généré à la racine du container
COPY ./target/alarm-clock-0.0.1-SNAPSHOT.jar ./alarmclock-api.jar

# Ce que fait le container au démarrage : execute simplement le jar
ENTRYPOINT ["java", "-jar", "alarmclock-api.jar"]