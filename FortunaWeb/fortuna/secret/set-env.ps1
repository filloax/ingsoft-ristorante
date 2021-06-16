$origdir = Get-Location
$scriptpath = $MyInvocation.MyCommand.Path
$dir = Split-Path $scriptpath
cd $dir

$Env:DB_SPRINGUSER_PW = $(cat db.springuser.p)
$Env:GEOCODING_KEY = $(cat google.geocoding.key)
$Env:STRIPE_SECRET_KEY = $(cat stripe.key)
$Env:STRIPE_PUBLIC_KEY = $(cat stripe.pub.key)
$Env:TWILIO_ACCOUNT_SID = $(cat twilio.acct.key)
$Env:TWILIO_AUTH_TOKEN = $(cat twilio.auth.key)

cd $origdir