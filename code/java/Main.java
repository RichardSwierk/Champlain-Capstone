public class Main {
        public static void main(String[] args) {
                Packet sendPacket;
                sendPacket = Packet.createStatsRequestPacket();
                System.out.println(sendPacket);
                final Packet packet = Packet.createSendPacket(Command.GET_CHANNEL_STATUS, new byte[]{
                        (byte)0x1b, (byte)0x1c, (byte)0x2a, (byte)0x2e, (byte)0xc5,
                        (byte)0x8f, (byte)0xc3, (byte)0x1d, (byte)0x1f, (byte)0x8e,
                        (byte)0xe0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
                });
                System.out.println(packet);
        }
}
