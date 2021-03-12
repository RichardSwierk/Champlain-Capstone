#!/usr/bin/env python

import usb
import time
import subprocess
import datetime

vid=0x10b6
pid=0x0502
#vid=0x16c0
#pid=0x0486

count=0

#PCV Commands
INVALID=0
CAN_PASS=8450
GET_CHANNEL_STATUS=5
READ_FLASH=6
CLEAR_ERRORS=20481
ENTER_EDIT_MODE=20482
EXIT_EDIT_MODE=20483
GET_BOOT_INFO=234
GET_FIRMWARE_INFO=235
UPDATE_DEVICE=20480
READ_FRAM=3
WRITE_FRAM=4

#Wait for PCV to turn on
def connect():
    boolean=True;
    while boolean==True:
        dev=usb.core.find(idVendor=vid, idProduct=pid)
        if dev is None:
            time.sleep(1)
        else:
            if dev.is_kernel_driver_active(0):
                dev.detach_kernel_driver(0)
            boolean=False
    return dev

#Get command in packet. bytes 4 and 5
def getCommand(packet):
    command=packet[5]+packet[4]
    command=int(command,16)
    return command

#Get the length of the payload
def getPayloadLength(packet):
    payloadLen=packet[7]+packet[6]
    payloadLen=int(payloadLen,16)
    return payloadLen

#Return the payload from the packet
def getPayload(payloadLen, packet):
    payload=[]
    for x in range(0,payloadLen):
        payload.append(packet[x+8])
    return payload

#Return the RPM value. RPM value starts at byte 1 and is 2bytes long
def getRPM(payload):
    rpm=payload[2]+payload[1]
    rpm=int(rpm,16)
    return rpm

#Return the throttle value. Starts at byte 9 and is 2 bytes long
def getThrottle(payload):
    throttle=payload[10]+payload[9]
    throttle=int(throttle,16)
    return throttle

# Get the ID of the packet. first 4 bytes
def getID(packet):
    return packet[0]+" "+packet[1]+" "+packet[2]+" "+packet[3]

# Make a list that holds the command packet
def makeSendPacketList(packetToSend):
    packToSend=packetToSend.replace("/","").strip().split("x")
    packToSend.pop(0)
    for x in range(len(packToSend)):
        packToSend[x]=packToSend[x].replace("x","")
        packToSend[x]=packToSend[x].upper()
    return packToSend

#Get packet to send
def getSendPacket():
    return subprocess.Popen(['java', 'Packet'], stdout=subprocess.PIPE).communicate()[0]

#Send packet to PCV
def sendPacket(endpoint_out, packetToSend):
    endpoint_out.write(packetToSend)

#Make received packet into a hex list
def makeRecvPacketList(recvPacket):
    recvPack=[]
    sret = ''.join([format(i, '02x') for i in recvPacket])
    for y in range(0,len(sret),2):
        recvPack.append(sret[y]+sret[y+1])
    for x in range(len(recvPack)):
        recvPack[x]=recvPack[x].upper()
    return recvPack

#Get packet sent from PCV
def receivePacket(endpoint_in, dev):
    return dev.read(endpoint_in.bEndpointAddress,64)

#Check to see if PCV responded to the packet sent
#If ID of sent packet is the same as ID of received packet then PCV responded to sent packet
def checkResponse(packetToSend, recvPacket):
    sendID=getID(makeSendPacketList(packetToSend))
    recvID=getID(makeRecvPacketList(recvPacket))
    if sendID == recvID:
        packetList=makeSendPacketList(recvPacket)
        print("Command: "+str(getCommand(packetList)))
        print("Payload length: "+str(getPayloadLength(packetList)))
        payload=getPayload(getPayloadLength(packetList),packetList)
        print("RPM"+str(getRPM(payload)))
        print("Throttle"+str(getThrottle(payload)))
        count=count+1
        f=open("log.txt","a")
        f.write(datetime.datetime.now())
        f.write(recvPacket)
        f.close()

#Send packet to PCV and then receive packet from PCV
def sendRead(endpoint_out, packetToSend, endpoint_in, dev):
    try:
        sendPacket(endpoint_out, packetToSend)
        recvPacket=receivePacket(endpoint_in, dev)
        checkResponse(packetToSend, recvPacket)
    except:
        pass

#Connect to PCV
dev=connect()
#Set endpoint out address
endpoint_out = dev[0][(0,0)][1]
#Set endpoint in address
endpoint_in = dev[0][(0,0)][0]
#Get packet to send to PCV
packetToSend=getSendPacket()
#Send packet, read received packet, check to see if there ids match
while count<6:
    sendRead(endpoint_out, packetToSend, endpoint_in, dev)

