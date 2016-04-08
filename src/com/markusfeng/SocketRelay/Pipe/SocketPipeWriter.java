package com.markusfeng.SocketRelay.Pipe;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public interface SocketPipeWriter{
	PipedInputStream getPipedInputStream();

	PipedOutputStream getPipedOutputStream();

	boolean isStringMode();
}
