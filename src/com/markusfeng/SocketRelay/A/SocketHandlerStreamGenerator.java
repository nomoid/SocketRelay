package com.markusfeng.SocketRelay.A;

import java.io.InputStream;
import java.io.OutputStream;

public interface SocketHandlerStreamGenerator<T extends SocketHandler<?>>{

	T getFromStreams(InputStream inputStream, OutputStream outputStream);
}
