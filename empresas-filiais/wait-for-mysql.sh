#!/bin/sh

echo "Carregando MySQL..."

while ! nc -z mysql 3306; do
  sleep 2
done

echo "Pronto!"

java -jar app.jar