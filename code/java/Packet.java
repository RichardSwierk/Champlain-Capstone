import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

import static java.lang.String.format;

public class Packet {

        private static final byte[] REQUEST_PACKET_PAYLOAD = new byte[]{(byte) 0x1b, (byte) 0x1c, (byte) 0x2a, (byte) 0x2e, (byte) 0xc5, (byte) 0x8f, (byte) 0xc3, (byte) 0x1d, (byte) 0x1f, (byte) 0x8e, (byte) 0xe0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
        private byte[] data;
        //private PacketDirection direction;
        private int seq;
        private LocalDateTime timestamp;
        private static final Random random = new Random();
        private static final Clock clock = Clock.systemUTC();
        private Packet() {}

        public static Packet createSendPacket(final Command command, final byte[] payload) {
                Packet packet = new Packet();
                packet.data = new byte[64];
                packet.seq = 0;
                packet.timestamp = LocalDateTime.now(clock);
                //packet.direction = PacketDirection.DOWN;

                // set packet ID
                final int randInt = random.nextInt();
                packet.data[0] = (byte)(randInt & 0xFF);
                packet.data[1] = (byte)((randInt >> 8) & 0xFF);
                packet.data[2] = (byte)((randInt >> 16) & 0xFF);
                packet.data[3] = (byte)((randInt >> 24) & 0xFF);

                // set command type
                final int cmdValue = command.toValue();
                packet.data[4] = (byte)(cmdValue & 0xFF);
                packet.data[5] = (byte)((cmdValue >> 8) & 0xFF);

                // set payload length
                packet.data[6] = (byte)(payload.length & 0xFF);
                packet.data[7] = (byte)((payload.length >> 8) & 0xFF);

                // set payload
                for (int i = 0; i < payload.length; i++) {
                        packet.data[8 + i] = payload[i];
                }
                return packet;
        }

        public static Packet createStatsRequestPacket() {
                return createSendPacket(Command.GET_CHANNEL_STATUS, REQUEST_PACKET_PAYLOAD);
        }
}