/** 
* this is just to store usefull code ive found 
* Packet creation = scapy
* 
*/




/**
 * A 64 byte PCV packet consists of:
 *  - 4 bytes ID (a random number)
 *  - 2 bytes command (see {@link Command})
 *  - 2 bytes payload length
 *  - remaining bytes payload and junk (to fill 64 byte packet)
 *
 *  Note: PCV packets are generally little endian! For example, assuming 0x010F stores a decimal value we need to reverse
 *  the bytes before converting to decimal. So 0x010F becomes 0x0F01 3841.
 */
 
 /** Payload that requests engine statistics, e.g. RPM, throttle, speed, gear... */
    private static final byte[] REQUEST_PACKET_PAYLOAD = new byte[]{
            (byte) 0x1b, (byte) 0x1c, (byte) 0x2a, (byte) 0x2e, (byte) 0xc5,
            (byte) 0x8f, (byte) 0xc3, (byte) 0x1d, (byte) 0x1f, (byte) 0x8e,
            (byte) 0xe0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
    };
