#!/bin/bash

docker build -t "websoa-active-mq:latest" -f ./active-mq/Dockerfile ./
docker build -t "websoa-distribution-service:latest" -f ./distribution-service/Dockerfile ./
docker build -t "websoa-event-service:latest" -f ./event-service/Dockerfile ./
docker build -t "websoa-payment-service:latest" -f ./payment-service/Dockerfile ./
docker build -t "websoa-ticket-service:latest" -f ./ticket-service/Dockerfile ./
docker build -t "websoa-user-service:latest" -f ./user-service/Dockerfile ./
docker build -t "websoa-validation-service:latest" -f ./validation-service/Dockerfile ./
docker build -t "websoa-admin-service:latest" -f ./admin-service/Dockerfile ./

# Connect minikube to the local docker registry
eval $(minikube -p minikube docker-env)
minikube addons enable ingress

# Launch all required services
minikube kubectl -- apply -f ingress.yml
minikube kubectl -- apply -f active-mq/kubernetes.yml
minikube kubectl -- apply -f distribution-service/kubernetes.yml
minikube kubectl -- apply -f event-service/kubernetes.yml
minikube kubectl -- apply -f payment-service/kubernetes.yml
minikube kubectl -- apply -f ticket-service/kubernetes.yml
minikube kubectl -- apply -f user-service/kubernetes.yml
minikube kubectl -- apply -f validation-service/kubernetes.yml
minikube kubectl -- apply -f admin-service/kubernetes.yml
