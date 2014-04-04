package model;

import org.apache.log4j.Logger;

public class ArgumentManager {
	private static ArgumentManager instance;
	private String[] arguments;

	private ArgumentManager() {

	}

	public static ArgumentManager getInstance() {
		if (instance == null) {
			instance = new ArgumentManager();
		}
		return instance;
	}

	public void setArguments(String[] arguments) {
		Logger.getLogger(getClass()).debug("arguments: " + "\nip: " + arguments[0] + "\nport: " + arguments[1]);
		this.arguments = arguments;
	}

	public String[] getArguments() {
		return arguments;
	}

	public String getIp() {
		return arguments[0];
	}

	public int getPort() {
		return Integer.valueOf(arguments[1]);
	}
}
