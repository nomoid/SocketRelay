package com.markusfeng.SocketRelay.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.markusfeng.Shared.Command;
import com.markusfeng.Shared.Commands;
import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.Pipe.SocketPipeOutImpl;
import com.markusfeng.SocketRelay.String.SocketLineHandler;

public abstract class SocketPacketHandler<T>extends SocketLineHandler<T>{

	public static final String DEFAULT_ESCAPE_GLYPH = "$";

	protected SocketPipeOutImpl<String> commandOut = new SocketPipeOutImpl<String>();
	protected ParsingSystem<String, T> parser;

	public SocketPacketHandler(Socket socket, SocketProcessor<T> processor, ParsingSystem<String, T> parser){
		super(socket, processor);
		this.parser = parser;
	}

	public SocketPacketHandler(Socket socket, SocketProcessor<T> processor, ParsingSystem<String, T> parser,
			InputStream in, OutputStream out){
		super(socket, processor, in, out);
		this.parser = parser;
	}

	@Override
	protected Maybe<T> convertToObject(String string){
		if(string == null){
			return null;
		}
		else if(!toEscape(string)){
			return parser.parse(string);
		}
		else if(escaped(string)){
			return parser.parse(unescape(string));
		}
		else{
			return processPacket(parsePacket(string));
		}
	}

	@Override
	protected Maybe<String> convertFromObject(T obj){
		Maybe<String> m = parser.unparse(obj);
		if(!m.isNonNull()){
			return m;
		}
		String s = m.get();
		if(toEscape(s)){
			return Maybe.with(escape(s));
		}
		else{
			return Maybe.with(s);
		}
	}

	protected String escapeGlyph(){
		return DEFAULT_ESCAPE_GLYPH;
	}

	protected boolean toEscape(String s){
		return s.startsWith(escapeGlyph());
	}

	protected boolean escaped(String s){
		return s.startsWith(escapeGlyph() + escapeGlyph());
	}

	protected String escape(String s){
		return escapeGlyph() + s;
	}

	protected String unescape(String s){
		return s.substring(escapeGlyph().length());
	}

	protected Command parsePacket(String s){
		String packet = unescape(s);
		return Commands.parseCommand(packet);
	}

	protected String unparsePacket(Command c){
		return escape(Commands.stringValue(c));
	}

	protected abstract Maybe<T> processPacket(Command packet);

	public void pushPacket(Command packet) throws IOException{
		commandOut.outputToHandler(getStringPipe(), unparsePacket(packet), false);
	}
}
