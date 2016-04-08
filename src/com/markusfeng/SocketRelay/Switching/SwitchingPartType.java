package com.markusfeng.SocketRelay.Switching;

import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.Packet.SocketPacketHandler;

public enum SwitchingPartType{
	SERVER(SwitchingServerHandler.class, SwitchingServerProcessor.class), SWITCHER(SwitchingSystemHandler.class,
			SwitchingSystemProcessor.class), CLIENT(SwitchingClientHandler.class, null);

	private SwitchingPartType(@SuppressWarnings("rawtypes") Class<? extends SocketPacketHandler> handler,
			@SuppressWarnings("rawtypes") Class<? extends SocketProcessor> processor){
		//TODO implement switching part interfaces
	}
}
