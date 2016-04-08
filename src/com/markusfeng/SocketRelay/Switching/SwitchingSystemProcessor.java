package com.markusfeng.SocketRelay.Switching;

import com.markusfeng.Shared.Command;
import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.B.SocketProcessorGenerator;
import com.markusfeng.SocketRelay.Packet.ParsingSystem;

public class SwitchingSystemProcessor<T>extends SwitchingSystemProcessorAbstract<T>{

	public SwitchingSystemProcessor(SocketProcessorGenerator<SocketProcessor<T>> gen,
			ParsingSystem<String, SendingObject<T>> parser){
		super(gen, parser);
	}

	/*
	 * Commands:
	 *
	 * serverlist (clientdata): gets the server list
	 * connectserver (clientdata, serverdata.id): connects to the server
	 * hostserver (serverdata): hosts the server
	 * connection:reply (connectionid): replies to the connection request
	 *
	 * Arguments:
	 *
	 * requestid (id): the id of the request, to be listened to by the reply
	 *
	 * clientdata (map): the data of the client
	 *   clientdata.name (string): the name of the client
	 *   clientdata.type (string): the type of the client (e.g. logicgame)
	 *   clientdata.version (version): the version of the client
	 *   clientdata.id (id): the id of the client
	 *
	 * serverdata (map): the data of the server
	 *   serverdata.name (string): the name of the server
	 *   serverdata.type (string): the type of the server (e.g. logicgame)
	 *   serverdata.version (version): the version of the server
	 *   serverdata.id (id): the id of the server
	 *
	 * switcherdata (map): the data of the switcher
	 *   switcherdata.name (string): the name of the switcher
	 *   switcherdata.version (version): the version of the switcher
	 *   switcherdata.id (id): the id of the switcher
	 *
	 *
	 */
	@Override
	public Maybe<SendingObject<T>> processPacket(long id, Command packet){
		// TODO process packet
		return null;
	}
}