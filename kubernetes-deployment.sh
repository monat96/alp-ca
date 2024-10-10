#!/bin/bash

services=(
  "gateway"
  "cctv-service"
  "health-check-service"
  "issue-service"
  "notification-service"
)

for service in "${services[@]}"; do
  kubectl apply -f "./backend/${service}/kubernetes/deployment.yaml"
  kubectl apply -f "./backend/${service}/kubernetes/service.yaml"
done
