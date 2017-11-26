#!/bin/bash
openssl aes-256-cbc -d -md md5 -in secret-env-cipher -out secret-env-plain -k $KEY
source secret-env-plain
echo $KEY_ALIAS
