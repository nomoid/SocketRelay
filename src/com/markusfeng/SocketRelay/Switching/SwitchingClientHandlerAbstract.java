package com.markusfeng.SocketRelay.Switching;

import java.net.Socket;

import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.Packet.ParsingSystem;
import com.markusfeng.SocketRelay.Packet.SocketPacketHandler;

public abstract class SwitchingClientHandlerAbstract<T>extends SocketPacketHandler<T>{

	public SwitchingClientHandlerAbstract(Socket socket, SocketProcessor<T> processor, ParsingSystem<String, T> parser){
		super(socket, processor, parser);
	}
}
