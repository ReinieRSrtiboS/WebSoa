#!/bin/bash

# Launch minikube
minikube start

# Connect to the minikube to docker registry
eval $(minikube -p minikube docker-env)

# Launch ingress
minikube addons enable ingress
minikube kubectl -- apply -f ingress.yml

# Launch pre-build docker images
minikube kubectl -- apply -f mailhog.yml
minikube kubectl -- rollout restart deployment mailhog-deployment
minikube kubectl -- apply -f postgres.yml
minikube kubectl -- rollout restart deployment postgres

# Build & launch required docker images
docker build -t "websoa-active-mq:latest" -f ./active-mq/Dockerfile ./
minikube kubectl -- apply -f active-mq/kubernetes.yml
minikube kubectl -- rollout restart active-mq-deployment

docker build -t "websoa-admin-service:latest" -f ./admin-service/Dockerfile ./
minikube kubectl -- apply -f admin-service/kubernetes.yml
minikube kubectl -- rollout restart deployment admin-deployment

docker build -t "websoa-distribution-service:latest" -f ./distribution-service/Dockerfile ./
minikube kubectl -- apply -f distribution-service/kubernetes.yml
minikube kubectl -- rollout restart deployment distribution-deployment

docker build -t "websoa-event-service:latest" -f ./event-service/Dockerfile ./
minikube kubectl -- apply -f event-service/kubernetes.yml
minikube kubectl -- rollout restart deployment event-deployment

docker build -t "websoa-payment-service:latest" -f ./payment-service/Dockerfile ./
minikube kubectl -- apply -f payment-service/kubernetes.yml
minikube kubectl -- rollout restart deployment payment-deployment

docker build -t "websoa-ticket-service:latest" -f ./ticket-service/Dockerfile ./
minikube kubectl -- apply -f ticket-service/kubernetes.yml
minikube kubectl -- rollout restart deployment ticket-deployment

docker build -t "websoa-user-service:latest" -f ./user-service/Dockerfile ./
minikube kubectl -- apply -f user-service/kubernetes.yml
minikube kubectl -- rollout restart deployment user-deployment

docker build -t "websoa-validation-service:latest" -f ./validation-service/Dockerfile ./
minikube kubectl -- apply -f validation-service/kubernetes.yml
minikube kubectl -- rollout restart deployment validation-deployment
