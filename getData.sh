#!/bin/bash
# Get info from PCV

# Get the ID of the PCV by removing all other known connected devices
devices=$(sudo lsusb | grep -vE "ID 1d6b:0003" | grep -vE "ID 13d3:3529" | grep -vE "ID 0bda:0129" | grep -vE "ID 13d3:5a11" | grep -vE "ID 0bda:8179" | grep -vE "ID 1d6b:0002" | grep -vE "2109:3431")
x=$(expr index "$devices" ":")
device=${devices:$x+4}
y=$(expr index "$device" " ")
PCVID=${device:0:$y-1}

# Get info from device
getInfo(){
        sudo lsusb -v -d $PCVID > "$PCVID-info.txt"
}

# record data for 10s from PCV
dump(){
        sudo timeout 10s usbhid-dump -m $PCVID -es > "$1-dump.txt"
}
