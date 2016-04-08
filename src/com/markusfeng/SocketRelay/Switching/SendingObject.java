package com.markusfeng.SocketRelay.Switching;

public interface SendingObject<T>{
	T getObject();

	long getSender();

	long getRecipient();

}
