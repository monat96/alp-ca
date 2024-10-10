#!/bin/bash

# Check if kubectl is installed
if ! command -v kubectl &> /dev/null
then
  echo "kubectl could not be found"
  exit 1
fi

# Check if helm is installed
if ! command -v helm &> /dev/null
then
  echo "helm could not be found"
  exit 1
fi

# Apply ingress configuration
kubectl create -f ingress.yaml
if [ $? -ne 0 ]; then
  echo "Failed to create ingress"
  exit 1
fi

# Add Helm repositories
helm repo add stable https://charts.helm.sh/stable
if [ $? -ne 0 ]; then
  echo "Failed to add stable repo"
  exit 1
fi

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
if [ $? -ne 0 ]; then
  echo "Failed to add ingress-nginx repo"
  exit 1
fi

# Update Helm repositories
helm repo update
if [ $? -ne 0 ]; then
  echo "Failed to update Helm repos"
  exit 1
fi

# Install ingress-nginx using Helm
helm install ingress-nginx ingress-nginx/ingress-nginx --namespace ingress-basic \
  --set controller.service.annotations."service\.beta\.kubernetes\.io/azure-load-balancer-health-probe-request-path"=/actuator/health
if [ $? -ne 0 ]; then
  echo "Failed to install ingress-nginx"
  exit 1
fi
