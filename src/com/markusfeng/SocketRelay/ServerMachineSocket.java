package com.markusfeng.SocketRelay;

import java.net.ServerSocket;

/**
 * A ServerSocket wrapped in a MachineSocket. Use get() to retrieve the ServerSocket.
 *
 * @author Markus Feng
 */
public interface ServerMachineSocket extends MachineSocket{
	@Override
	ServerSocket get();
}
