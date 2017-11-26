#!/bin/bash
echo "Decrypting"
openssl aes-256-cbc -d -md md5 -in secret-env-cipher -out secret-env-plain -k $KEY
ls -l secret-*
cat ./secret-env-plain

echo "Setting vars"
source ./secret-env-plain
echo $KEY_ALIAS
