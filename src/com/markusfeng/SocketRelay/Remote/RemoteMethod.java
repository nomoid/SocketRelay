package com.markusfeng.SocketRelay.Remote;

import java.util.Map;

import com.markusfeng.SocketRelay.Compatibility.Function;

/**
 * A method that can be remotely invoked.
 * The input of the function is the map of parameters, while the output
 * of the function is the return value.
 *
 * @author Markus Feng
 */
public interface RemoteMethod extends Function<Map<String, String>, String>{

	/**
	 * Calls the remote method with the given parameters.
	 *
	 * @param parameters The parameters to call the remote method with.
	 * @return The result of the remote method call.
	 */
	@Override
	String apply(Map<String, String> parameters);

}
