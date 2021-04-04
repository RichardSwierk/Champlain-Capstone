#!/bin/bash

#Install whats needed for java
sudo apt-get install default-jre openjdk-11-jre-headless openjdk-8-jre-headless default-jdk --yes
#Install whats needed for python
sudo apt-get install python python-usb python-hidapi --yes
#Compile java code
javac Packet.java
#Compile python code
chmod +X data.py
