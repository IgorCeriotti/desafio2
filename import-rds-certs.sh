#!/usr/bin/env sh
OLDDIR="$PWD"

if [ -z "$CACERTS_FILE" ]; then
    CACERTS_FILE=/usr/java/openjdk-17/lib/security/cacerts
fi

mkdir /tmp/rds-ca && cd /tmp/rds-ca

echo "Downloading RDS certificates..."

curl https://s3.amazonaws.com/rds-downloads/rds-combined-ca-bundle.pem > rds-combined-ca-bundle.pem

csplit -sk rds-combined-ca-bundle.pem "/-BEGIN CERTIFICATE-/" "{$(grep -c 'BEGIN CERTIFICATE' rds-combined-ca-bundle.pem | awk '{print $1 - 2}')}"

for CERT in xx*; do
    # extract a human-readable alias from the cert
    ALIAS=$(openssl x509 -noout -text -in $CERT |
                   perl -ne 'next unless /Subject:/; s/.*CN=//; print')
    echo "importing $ALIAS"
    # import the cert into the default java keystore
    keytool -import \
            -keystore  $CACERTS_FILE \
            -storepass changeit -noprompt \
            -alias "$ALIAS" -file $CERT
done

cd "$OLDDIR"

rm -r /tmp/rds-ca