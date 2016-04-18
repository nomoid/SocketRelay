package com.markusfeng.SocketRelay.Switching;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.markusfeng.Shared.Command;
import com.markusfeng.Shared.Commands;
import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.B.SocketProcessorGenerator;
import com.markusfeng.SocketRelay.Packet.ParsingSystem;

public class SwitchingSystemProcessor extends SwitchingSystemProcessorAbstract<String>{

	protected List<ServerData> list;

	public SwitchingSystemProcessor(SocketProcessorGenerator<SocketProcessor<String>> gen,
			ParsingSystem<String, SendingObject<String>> parser){
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
	public Maybe<SendingObject<String>> processPacket(long id, Command packet) throws IOException{
		Map<String, String> args = packet.getArguments();
		if(packet.getName().equalsIgnoreCase("severlist")){
			Map<String, String> reply = new HashMap<String, String>();
			reply.put("requestid", args.get("requestid"));
			return Maybe.with(SendingObjects.make(0, id,
					Commands.stringValue(Commands.make(
							Commands.fromIterable(list.stream().map(data -> ServerData.parser.unparse(data))
									.map(command -> Commands.stringValue(command.get())).collect(Collectors.toList())),
					reply))));
		}
		else if(packet.getName().equalsIgnoreCase("connectserver")){
			boolean success = false;
			long targetID;
			try{
				targetID = Long.parseLong(args.get("serverdata.id"));
				//TODO
				output(SendingObjects.make(0, targetID, null), false);
			}
			catch(NumberFormatException e){
				e.printStackTrace();
			}
			Map<String, String> reply = new HashMap<String, String>();
			reply.put("requestid", args.get("requestid"));
			//return Maybe.with(
		}
		return null;
	}
}
