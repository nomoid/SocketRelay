package com.markusfeng.SocketRelay.C;

import java.io.IOException;

import com.markusfeng.SocketRelay.A.SocketClient;
import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.A.SocketServer;
import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.B.SocketProcessorGenerator;
import com.markusfeng.SocketRelay.Stream.SocketStreamHandler;
import com.markusfeng.SocketRelay.Stream.SocketStreamHandlerGenerator;
import com.markusfeng.SocketRelay.String.SocketStringHandler;
import com.markusfeng.SocketRelay.String.SocketStringHandlerGenerator;

/**
 * A helper class used to easily generate SocketServers and SocketClients
 *
 * @author Markus Feng
 */
public final class SocketHelper{

	private SocketHelper(){
		//Do nothing
	}

	/**
	 * Creates a String SocketServer
	 * @param port the port of the server
	 * @param gen the SocketProcessorGenerator of the handler of the server
	 * @return the SocketServer generated
	 * @throws IOException
	 */
	public static <T extends SocketProcessor<String>> SocketServer<SocketHandler<String>>
	getStringServer(int port, SocketProcessorGenerator<T> gen)
			throws IOException{
		try{
			return new SocketServer<SocketHandler<String>>(port,
					new SocketStringHandlerGenerator(gen));
		}
		catch(Exception e){
			throw new IOException(e);
		}
	}

	/**
	 * Creates a String SocketClient
	 * @param host the host of the client
	 * @param port the port of the client
	 * @param process the SocketProcess of the handler of the client
	 * @return the SocketClient generated
	 * @throws IOException
	 */
	public static SocketClient<SocketHandler<String>>
	getStringClient(String host, int port, SocketProcessor<String> process)
			throws IOException{
		SocketStringHandler handler = new SocketStringHandler(process);
		SocketClient<SocketHandler<String>> client = new SocketClient<SocketHandler<String>>(host, port,
				handler);
		try{
			handler.openSocket(client.getSocket().get());
			return client;
		}
		catch(Exception e){
			throw new IOException(e);
		}
	}

	/**
	 * Creates a byte[] SocketServer
	 * @param port the port of the server
	 * @param gen the SocketProcessorGenerator of the handler of the server
	 * @return the SocketServer generated
	 * @throws IOException
	 */
	public static <T extends SocketProcessor<byte[]>> SocketServer<SocketHandler<byte[]>>
	getByteArrayServer(int port, SocketProcessorGenerator<T> gen)
			throws IOException{
		try{
			return new SocketServer<SocketHandler<byte[]>>(port,
					new SocketStreamHandlerGenerator(gen));
		}
		catch(Exception e){
			throw new IOException(e);
		}
	}

	/**
	 * Creates a byte[] SocketClient
	 * @param host the host of the client
	 * @param port the port of the client
	 * @param process the SocketProcess of the handler of the client
	 * @return the SocketClient generated
	 * @throws IOException
	 */
	public static SocketClient<SocketHandler<byte[]>>
	getByteArrayClient(String host, int port, SocketProcessor<byte[]> gen)
			throws IOException{
		try{
			SocketStreamHandler handler = new SocketStreamHandler(gen);
			SocketClient<SocketHandler<byte[]>> client = new SocketClient<SocketHandler<byte[]>>(host, port,
					handler);
			handler.openSocket(client.getSocket().get());
			return client;
		}
		catch(Exception e){
			throw new IOException(e);
		}
	}
}
