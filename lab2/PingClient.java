import java.io.*;
import java.net.*;
import java.util.*;

public class PingClient {
	private static final int REQUEST_TIME_OUT = 600;

	public static void main(String [] args) throws Exception {

		if (args.length != 2) {
			System.out.println("Required Argument: host and port");
			return;
		}
		InetAddress server = InetAddress.getByName(args[0]);
		int port = Integer.parseInt(args[1]);
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(REQUEST_TIME_OUT);

		long totalRTT = 0;
		long maxRTT = 0;
		long minRTT = 600;
		int numValid = 0;
		for (int i = 3331; i <= 3345; i++ ) {
			long timeStart = System.currentTimeMillis();
			String message = "PING " + i + " " + timeStart + "\r\n";

			DatagramPacket request = new DatagramPacket(message.getBytes(), message.length(), server, port);
			socket.send(request);

			try {
				DatagramPacket serverReply = new DatagramPacket(new byte[1024], 1024);
				socket.receive(serverReply);
				long timeFinish = System.currentTimeMillis();
				long rtt = timeFinish - timeStart;
				System.out.println("ping to " + args[0] + ", seq = " + i + ", rtt = " + rtt + " ms");
				totalRTT += rtt;
				if (rtt > maxRTT) maxRTT = rtt;
				if (rtt < minRTT) minRTT = rtt;
				numValid ++;
			}catch (SocketTimeoutException e) {
				System.out.println("ping to " + args[0] + ", seq = " + i + ", time out");
			}
		}
		System.out.println("Average RTT = " + (totalRTT / numValid) + " ms" + ", Minimum RTT = "
                + minRTT + " ms" + ", Maximum RTT = " + maxRTT + " ms");
        socket.close();
	}



}