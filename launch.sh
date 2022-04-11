#!/bin/bash

# Launch minikube
minikube start

# Connect to the minikube to docker registry
eval $(minikube -p minikube docker-env)

# Launch ingress
minikube addons enable ingress
minikube kubectl -- apply -f ingress.yml

# Build & launch required docker images
docker build -t "websoa-active-mq:latest" -f ./active-mq/Dockerfile ./
minikube kubectl -- apply -f active-mq/kubernetes.yml

docker build -t "websoa-admin-service:latest" -f ./admin-service/Dockerfile ./
minikube kubectl -- apply -f admin-service/kubernetes.yml

docker build -t "websoa-distribution-service:latest" -f ./distribution-service/Dockerfile ./
minikube kubectl -- apply -f distribution-service/kubernetes.yml

docker build -t "websoa-event-service:latest" -f ./event-service/Dockerfile ./
minikube kubectl -- apply -f event-service/kubernetes.yml

docker build -t "websoa-payment-service:latest" -f ./payment-service/Dockerfile ./
minikube kubectl -- apply -f payment-service/kubernetes.yml

docker build -t "websoa-ticket-service:latest" -f ./ticket-service/Dockerfile ./
minikube kubectl -- apply -f ticket-service/kubernetes.yml

docker build -t "websoa-user-service:latest" -f ./user-service/Dockerfile ./
minikube kubectl -- apply -f user-service/kubernetes.yml

docker build -t "websoa-validation-service:latest" -f ./validation-service/Dockerfile ./
minikube kubectl -- apply -f validation-service/kubernetes.yml
