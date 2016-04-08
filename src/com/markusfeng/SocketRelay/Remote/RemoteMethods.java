package com.markusfeng.SocketRelay.Remote;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import com.markusfeng.Shared.Command;
import com.markusfeng.Shared.Commands;

/**
 * Helper class containing methods related to RemoteMethods
 *
 * @author Markus Feng
 */
public final class RemoteMethods{

	private RemoteMethods(){
		//Do not instantiate
	}

	/**
	 * Creates a VoidRemoteMethod from a consumer that returns null.
	 *
	 * @param consumer The consumer to create a VoidRemoteMethod from
	 * @return the VoidRemoteMethod created from the consumer
	 */
	public static VoidRemoteMethod fromConsumer(final Consumer<Map<String, String>> consumer){
		return new VoidRemoteMethod(){

			@Override
			public String apply(Map<String, String> socket){
				consumer.accept(socket);
				return null;
			}

		};
	}

	/**
	 * Create a "serveronly" version of the command, by adding
	 * the serveronly key to the map, with an empty string as its value.
	 *
	 * @param command The command to add "severonly" to
	 * @return A copy of the command that is "serveronly"
	 */
	public static Command serverOnly(Command command){
		return Commands.make(command, Collections.singletonMap("serveronly", ""));
	}

	/**
	 * Creates a version of the command with the given recipients, by adding
	 * the recipients key to the map, with the values being the recipients.
	 *
	 * @param command The command to add recipients to
	 * @param recipients The recipients to add to the command
	 * @return A copy of the command with added recipients
	 */
	public static Command recipients(Command command, long... recipients){
		return Commands.make(command, Collections.singletonMap("recipients", Commands.fromArray(recipients)));
	}
}
