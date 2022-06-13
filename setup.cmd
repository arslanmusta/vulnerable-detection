call mvn -f browser-api-gw/ clean package -DskipTests
call mvn -f identity-service/ clean package -DskipTests
call mvn -f inventory-service/ clean package -DskipTests
call mvn -f notification-service/ clean package -DskipTests
call mvn -f notification-send-worker/ clean package -DskipTests
call mvn -f vulnerable-service/ clean package -DskipTests
call mvn -f vulnerable-update-worker/ clean package -DskipTests
call mvn -f vulnerable-detect-worker/ clean package -DskipTests

call docker-compose build
call docker-compose up -d
