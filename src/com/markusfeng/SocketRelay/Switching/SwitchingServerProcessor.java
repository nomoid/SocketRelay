package com.markusfeng.SocketRelay.Switching;

import com.markusfeng.Shared.Command;
import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.A.SocketHandlerStreamGenerator;
import com.markusfeng.SocketRelay.Packet.ParsingSystem;

public class SwitchingServerProcessor<T>extends SwitchingServerProcessorAbstract<T>{

	public SwitchingServerProcessor(ParsingSystem<String, T> parser,
			SocketHandlerStreamGenerator<SocketHandler<T>> gen){
		super(parser, gen);
	}

	/*
	 * Commands:
	 *
	 * hostserver:reply (host:reply): hosts the server
	 * connection (clientdata, connectionid): connect request
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
	protected Maybe<SendingObject<T>> processPacket(Command packet){
		// TODO process packet
		return null;
	}

}
