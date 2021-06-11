$env:DB_SPRINGUSER_PW = $(cat db.springuser.p)
$env:GEOCODING_KEY = $(cat google.geocoding.key)
$env:STRIPE_SECRET_KEY = $(cat stripe.key)
$env:STRIPE_PUBLIC_KEY = $(cat stripe.pub.key)