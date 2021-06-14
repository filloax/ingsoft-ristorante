#!/bin/bash
ORIG_DIR=$(pwd)
PERC=$(realpath "$0")
DIR=$(dirname "$PERC")

echo "DIR"
echo $DIR
cd "$DIR"

export DB_SPRINGUSER_PW=$(cat db.springuser.p)
export GEOCODING_KEY=$(cat google.geocoding.key)
export STRIPE_SECRET_KEY=$(cat stripe.key)
export STRIPE_PUBLIC_KEY=$(cat stripe.pub.key)
echo "esportato chiavi"
cd "$ORIG_DIR"
