//Create packet to send to PCV
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

import static java.lang.String.format;

public class Packet{
	/** Payload that requests engine statistics, e.g. RPM, throttle, speed, gear... */
	private static final byte[] REQUEST_PACKET_PAYLOAD = new byte[]{(byte) 0x1b, (byte) 0x1c, (byte) 0x2a, (byte) 0x2e, (byte) 0xc5, (byte) 0x8f, (byte) 0xc3, (byte) 0x1d, (byte) 0x1f, (byte) 0x8e, (byte) 0xe0, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x00};

	/** Complete 64-byte packet. */
	private byte[] data;
	/** Sequence number. Useful when working with existing dumps. */
	private int seq;
	/** When was the packet received/captured. */
	private LocalDateTime timestamp;

	private static final Random random = new Random();
	private static final Clock clock = Clock.systemUTC();

	private Packet() {}

	public static String getID(String[] pack){
		String ID="";
		for(int x=0;x<4;x++){
			ID=ID+pack[x]+" ";
		}
		return ID;
	}

	public static String getCommand(String[] pack){
		String Command="";
		for(int x=4;x<6;x++){
			Command=Command+pack[x]+" ";
		}
		return Command;
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String[] bytesToHex(byte[] bytes) {
		String[] pack=new String[bytes.length];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			pack[j]="/x"+hexArray[v >>> 4]+""+hexArray[v & 0x0F];;
		}
		return pack;
	}

	public static String[] createSendPacket(final Command command, final byte[] payload) {
		Packet packet = new Packet();
		packet.data = new byte[64];
		packet.seq = 0;
		packet.timestamp = LocalDateTime.now(clock);

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
		return bytesToHex(packet.data);
	}

	public static void main(String[] args){
		String[] sendPacket=createSendPacket(Command.GET_CHANNEL_STATUS, REQUEST_PACKET_PAYLOAD);
		for(int x=0;x<sendPacket.length;x++){
			System.out.print(sendPacket[x]);
		}
	}
}