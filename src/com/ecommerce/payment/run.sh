#!/usr/bin/env bash
# Compiles and runs the Multinational Payment Platform demo.
# Requires a JDK (java + javac) version 17 or later on the PATH.
set -e

cd "$(dirname "$0")"

echo "Compiling..."
rm -rf out
mkdir -p out
find src -name "*.java" > .sources.txt
javac -d out @.sources.txt
rm -f .sources.txt

echo "Compilation successful."
echo "Starting the application (console demo + HTTP server on port 8080)..."
echo
java -cp out com.ecommerce.payment.Main
