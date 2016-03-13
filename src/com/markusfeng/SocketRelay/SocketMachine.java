package com.markusfeng.SocketRelay;

import java.io.Closeable;

/**
 * Represents a generic SocketMachine. Extend to provide additional functionality.
 * SocketMachines are generally used to handle connections between Sockets.
 *
 * @author Markus Feng
 */
public interface SocketMachine extends Closeable, Runnable{

	/**
	 * Opens the SocketMachine to start all operations.
	 * By convention, one should start the Runnable of the
	 * machine on a different thread before calling open(),
	 * to allow interaction wit the SocketMachine before
	 * open() is called. In most implementations, the Runnable
	 * of the machine is called in the constructor to allow
	 * immediate interaction with the machine once it is created.
	 */
	void open();

	/**
	 * Returns the Socket that is owned by this machine
	 * @return the Socket that is owned by this machine
	 */
	MachineSocket getSocket();
}
