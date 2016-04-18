package com.markusfeng.SocketRelay.Switching;

import java.util.HashMap;
import java.util.Map;

import com.markusfeng.Shared.Command;
import com.markusfeng.Shared.Commands;
import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.Packet.ParsingSystem;

public class ServerData{

	private String name;
	private long id;

	protected ServerData(){

	}

	public static ServerData get(String name, long id){
		ServerData data = new ServerData();
		data.name = name;
		data.id = id;
		return data;
	}

	public String getName(){
		return name;
	}

	public long getID(){
		return id;
	}

	public static ParsingSystem<Command, ServerData> parser = new ParsingSystem<Command, ServerData>(){

		@Override
		public Maybe<ServerData> parse(Command command){
			ServerData data = new ServerData();
			Map<String, String> map = command.getArguments();
			data.name = map.get("serverdata.name");
			try{
				data.id = Long.parseLong(map.get("serverdata.id"));
			}
			catch(NumberFormatException e){
				throw new NumberFormatException("invalid id: " + data.id);
			}
			return Maybe.with(data);
		}

		@Override
		public Maybe<Command> unparse(ServerData data){
			Map<String, String> map = new HashMap<String, String>();
			map.put("serverdata.name", data.getName());
			map.put("serverdata.id", String.valueOf(data.getID()));
			return Maybe.with(Commands.make("data_serverdata", map));
		}

	};
}
