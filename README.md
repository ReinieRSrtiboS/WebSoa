## Launching
The `./launch.sh` script starts a kubernetes cluster running the application. Do note that it may take a few minutes after completion before everything is started correctly.

## Accessing services

The k8s cluster comes with a configured ingress, so you can access services via their associated routes. Most important are `/admin/`, `/user/`, `/payment/` and `/validation/`, and we recommend accessing and exploring them in that order. Do note that the trailing slash after the service name is required.
