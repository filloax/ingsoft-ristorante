#!/bin/bash
ORIG_DIR=$(pwd)
PERC=$(realpath "$0")
DIR=$(dirname "$PERC")
cd "$DIR"

export DB_SPRINGUSER_PW=$(cat db.springuser.p)
export GEOCODING_KEY=$(cat google.geocoding.key)
export STRIPE_SECRET_KEY=$(cat stripe.key)
export STRIPE_PUBLIC_KEY=$(cat stripe.pub.key)

cd "$ORIG_DIR"
