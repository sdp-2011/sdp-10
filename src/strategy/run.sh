#! /bin/bash

# Coommands for compiling and linking packages:

echo "Compiling all files and linking packages... \n"
nxjpcc -d ../bin Commander.java
echo " "

# Commands for launching the Commander, and connecting to Stewie:

echo "Running the Commander... \n"
cd ../bin
nxjpc Commander
