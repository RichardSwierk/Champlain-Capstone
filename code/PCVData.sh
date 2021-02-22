#!/bin/bash
# Get info from PCV

# PCV Commands
commands(){
        ReadFram=3
}

# Get the ID of the PCV by removing all other known connected devices
PCVID="10b6:0502"
avail=$(lsusb)
if [[ $avail =~ $PCVID ]]
then
        echo "yes"
else
        echo "PCV not available"
fi

# Get info from device
getInfo(){
        sudo lsusb -v -d $PCVID > "$PCVID-info.txt";
}

# record data for 10s from PCV
dump(){
        sudo timeout 10s usbhid-dump -m $PCVID -es > "$1-dump.txt";
}

getInfo
