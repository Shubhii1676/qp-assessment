FROM openjdk:17
COPY build/libs/QPGroceryProject-0.0.1-SNAPSHOT.jar QPGroceryProject-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "QPGroceryProject-0.0.1-SNAPSHOT.jar"]