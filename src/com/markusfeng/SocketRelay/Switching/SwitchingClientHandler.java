package com.markusfeng.SocketRelay.Switching;

import java.net.Socket;
import java.util.function.Supplier;

import com.markusfeng.Shared.Command;
import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.Packet.ParsingSystem;
import com.markusfeng.SocketRelay.Packet.ParsingSystems;

public class SwitchingClientHandler<T>extends SwitchingClientHandlerAbstract<T>{

	private SwitchingClientHandler(Socket socket, SocketProcessor<T> processor, ParsingSystem<String, T> parser,
			DoubleSupplierPromise promise){
		super(socket, processor, ParsingSystems.join(ParsingSystems.sendingSystem(parser),
				ParsingSystems.<T> flatteningSystem(promise.getFirstSupplier(), promise.getSecondSupplier())));
		promise.setupHandler(this);
	}

	public SwitchingClientHandler(Socket socket, SocketProcessor<T> processor, ParsingSystem<String, T> parser){
		this(socket, processor, parser, new DoubleSupplierPromise());
	}

	private static class DoubleSupplierPromise{

		public SupplierSupplier<Long> first = new SupplierSupplier<Long>(0L);
		public SupplierSupplier<Long> second = new SupplierSupplier<Long>(0L);

		public Supplier<Long> getFirstSupplier(){
			return first;
		}

		public Supplier<Long> getSecondSupplier(){
			return second;
		}

		public void setupHandler(SwitchingClientHandler<?> handler){
			//TODO redirect suppliers
		}

		public static class SupplierSupplier<S> implements Supplier<S>{

			public Supplier<S> supplier;

			@SuppressWarnings("unused")
			public SupplierSupplier(){

			}

			public SupplierSupplier(final S def){
				supplier = new Supplier<S>(){

					@Override
					public S get(){
						return def;
					}

				};
			}

			@SuppressWarnings("unused")
			public SupplierSupplier(Supplier<S> supplier){
				this.supplier = supplier;
			}

			@Override
			public S get(){
				return supplier.get();
			}

		}

	}

	@Override
	protected Maybe<T> processPacket(Command packet){
		// TODO process packet
		return null;
	}

}
