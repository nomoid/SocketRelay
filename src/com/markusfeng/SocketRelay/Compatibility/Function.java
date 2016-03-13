package com.markusfeng.SocketRelay.Compatibility;

public interface Function<T, S>{
	S apply(T socket);
}
