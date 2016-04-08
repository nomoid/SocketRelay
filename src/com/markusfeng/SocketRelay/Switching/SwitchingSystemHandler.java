package com.markusfeng.SocketRelay.Switching;

import java.io.IOException;
import java.net.Socket;

import com.markusfeng.Shared.Command;
import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.Compatibility.Function;
import com.markusfeng.SocketRelay.Packet.ParsingSystem;
import com.markusfeng.SocketRelay.Packet.SocketPacketHandler;

public class SwitchingSystemHandler<T>extends SocketPacketHandler<SendingObject<T>>{

	protected SwitchingSystemProcessorAbstract<T> system;
	protected long id;

	public SwitchingSystemHandler(long id, Socket socket,
			SwitchingSystemProcessorAbstract<T> switchingSystemProcessorAbstract,
			ParsingSystem<String, SendingObject<T>> parser){
		super(socket, switchingSystemProcessorAbstract, new SendingObjectAdder<String, T>(id, parser));
		this.system = switchingSystemProcessorAbstract;
		this.id = id;
	}

	@Override
	public void initialize() throws IOException{
		super.initialize();
	}

	@Override
	protected Maybe<SendingObject<T>> processPacket(Command packet){
		return system.processPacket(id, packet);
	}

	protected static class SendingObjectAdder<T, S> implements ParsingSystem<T, SendingObject<S>>{

		protected Function<SendingObject<S>, SendingObject<S>> wrap = new Function<SendingObject<S>, SendingObject<S>>(){

			@Override
			public SendingObject<S> apply(SendingObject<S> s){
				if(s.getSender() == SendingObjects.DEFAULT_ID){
					return SendingObjects.make(id, s.getRecipient(), s.getObject());
				}
				else{
					return s;
				}
			}

		};

		protected long id;
		protected ParsingSystem<T, SendingObject<S>> system;

		public SendingObjectAdder(long id, ParsingSystem<T, SendingObject<S>> system){
			this.id = id;
			this.system = system;
		}

		@Override
		public Maybe<SendingObject<S>> parse(T t){
			return Maybe.compose(system.parse(t), wrap);
		}

		@Override
		public Maybe<T> unparse(SendingObject<S> s){
			return system.unparse(s);
		}

	}

}
