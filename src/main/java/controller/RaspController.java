package controller;

import java.io.IOException;
import java.net.InetAddress;

import javafx.concurrent.Task;

public class RaspController extends Task<Void> {

	@Override
	protected Void call() throws Exception {
		checkHosts("192.168.0");
		return null;
	}

	public void checkHosts(String subnet) throws IOException {
		System.out.println("check");
		InetAddress localhost = InetAddress.getLocalHost();
		// this code assumes IPv4 is used
		byte[] ip = localhost.getAddress();

		for (int i = 1; i <= 254; i++) {
			ip[3] = (byte) i;
			InetAddress address = InetAddress.getByAddress(ip);
			if (address.isReachable(1000)) {
				System.out.println(address + " machine is turned on and can be pinged");
			} else if (!address.getHostAddress().equals(address.getHostName())) {
				System.out.println(address + " machine is known in a DNS lookup");
			} else {
				System.out.println(address + " the host address and host name are equal, meaning the host name could not be resolved");
			}
		}
	}
}
