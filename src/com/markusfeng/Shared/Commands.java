package com.markusfeng.Shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class containing methods related to Commands.
 *
 * @author Markus Feng
 */
public final class Commands{

	private Commands(){
		//Do nothing
	}

	/**
	 * Makes a new command with the arguments and the name of the original command,
	 * along with the arguments in the map. If arguments are repeated, arguments in
	 * the map overwrite arguments in the original command.
	 * @param command The original command which the name and arguments are based on
	 * @param add The arguments to add to the command
	 * @return The newly created command with the given arguments
	 */
	public static Command make(Command command, Map<String, String> add){
		Map<String, String> data = new LinkedHashMap<String, String>(command.getArguments());
		data.putAll(add);
		return make(command.getName(), data);
	}

	/**
	 * Makes a new command with the given name and no arguments.
	 * @param name The name of the command
	 * @return The newly created command with the given name and no arguments
	 */
	public static Command make(String name){
		Map<String, String> empty = Collections.emptyMap();
		return make(name, empty);
	}

	/**
	 * Makes a new command with the given name and given arguments. The arguments
	 * are ordered in key-value pairs of the array, so the array must have
	 * an even length.
	 * @param name The name of the command
	 * @param argumentData The arguments represented by key-value pairs
	 * @return The newly created command with the given name and arguments
	 */
	public static Command make(String name, String... argumentData){
		//Length of argument data must be even
		if(argumentData.length % 2 != 0){
			throw new IllegalArgumentException("Illegal command arguments!");
		}
		Map<String, String> data = new LinkedHashMap<String, String>();
		for(int i = 0; i < argumentData.length; i += 2){
			data.put(argumentData[i], argumentData[i + 1]);
		}
		return make(name, data);
	}

	/**
	 * Makes a new command with the given name and arguments.
	 * @param name The name of the command
	 * @param data The arguments of the command
	 * @return The newly created command with the given name and arguments
	 */
	public static Command make(String name, Map<String, String> data){
		return new GenericCommand(name, data);
	}

	//Allows the command to have an empty name
	private static final boolean ALLOW_EMPTY_NAME = false;

	/**
	 * A generic implementation of the Command interface.
	 * The getArguments method returns an unmodifiable map
	 * of the arguments map.
	 *
	 * @author Markus Feng
	 */
	protected static class GenericCommand implements Command{

		/**
		 * The arguments map
		 */
		protected Map<String, String> args;

		/**
		 * The name of the command
		 */
		protected String name;

		/**
		 * Create a new generic command with the given name and argument.
		 * A new map will be created that contains a copy of the contents
		 * of the map passed to the constructor.
		 * @param name The name of the command
		 * @param args The arguments of the command.
		 */
		protected GenericCommand(String name, Map<String, String> args){
			//Check for an empty name
			if(!ALLOW_EMPTY_NAME && (name == null || name.length() == 0)){
				throw new IllegalArgumentException("Name cannot be empty");
			}
			this.name = name;
			this.args = new LinkedHashMap<String, String>(args);
		}

		@Override
		public String getName(){
			return name;
		}

		@Override
		public Map<String, String> getArguments(){
			return Collections.unmodifiableMap(args);
		}

		@Override
		public String toString(){
			return Commands.stringValue(this);
		}

		@Override
		public int hashCode(){
			return getName().hashCode() ^ getArguments().hashCode();
		}

		@Override
		public boolean equals(Object o){
			if(o instanceof Command){
				Command c = (Command) o;
				if(getName().equals(c.getName()) && getArguments().equals(c.getArguments())){
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Parses a command with the given string.
	 *
	 * Command format:
	 * "[" name (";" argument "=" value)* (";")? "]"
	 *
	 * e.g.
	 * [add;x=3;y=4]
	 *
	 * @param in the String to parse the command from.
	 * @return the Command parsed by the string
	 */
	public static Command parseCommand(String in){
		return Parser.defaultParser().parseCommand(in);
	}

	/**
	 * Converts a command to its string representation.
	 *
	 * Command format:
	 * "[" name (";" argument "=" value)* (";")? "]"
	 *
	 * e.g.
	 * [add;x=3;y=4]
	 *
	 * @param command the command to convert to String
	 * @return
	 */
	public static String stringValue(Command command){
		return Parser.defaultParser().commandToString(command);
	}

	public static final String ARRAY_SEPARATOR = "/";

	/**
	 * Creates a string representation of an int array that can
	 * be used with commands. Separates each item in the string
	 * with a '/', with no escaping done.
	 *
	 * @param ia the int array to convert to a String
	 * @return the String representation of the int array
	 */
	public static String fromArray(int[] ia){
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(int i : ia){
			if(first){
				first = false;
			}
			else{
				sb.append(ARRAY_SEPARATOR);
			}
			sb.append(i);
		}
		return sb.toString();
	}

	/**
	 * Creates a string representation of an long array that can
	 * be used with commands. Separates each item in the string
	 * with a '/', with no escaping done.
	 *
	 * @param ia the long array to convert to a String
	 * @return the String representation of the long array
	 */
	public static String fromArray(long[] ia){
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(long i : ia){
			if(first){
				first = false;
			}
			else{
				sb.append(ARRAY_SEPARATOR);
			}
			sb.append(i);
		}
		return sb.toString();
	}

	/**
	 * Creates a string representation of an Iterable that can
	 * be used with commands. Separates each item in the string
	 * with a '/', with no escaping done. Uses the toString method
	 * of the objects in the Iterable.
	 *
	 * @param ia the Iterable to convert to a String
	 * @return the String representation of the Iterable
	 */
	public static String fromIterable(Iterable<?> iter){
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(Object obj : iter){
			if(first){
				first = false;
			}
			else{
				sb.append(ARRAY_SEPARATOR);
			}
			String s = obj.toString();
			s = s.replace("\\", "\\\\");
			s = s.replace(ARRAY_SEPARATOR, "\\" + ARRAY_SEPARATOR);
			sb.append(obj);
		}
		return sb.toString();
	}

	/**
	 * Converts a string representation to an int array. The string
	 * representation is separated with a '/'.
	 * @param s the string representation of the int array
	 * @return the int array given by the string representation
	 * @throws NumberFormatException if the parsing on the int array fails
	 */
	public static int[] toIntArray(String s) throws NumberFormatException{
		String[] arr = s.split(ARRAY_SEPARATOR);
		int[] ia = new int[arr.length];
		for(int i = 0; i < arr.length; i++){
			ia[i] = Integer.parseInt(arr[i]);
		}
		return ia;
	}

	/**
	 * Converts a string representation to an long array. The string
	 * representation is separated with a '/'.
	 * @param s the string representation of the long array
	 * @return the long array given by the string representation
	 * @throws NumberFormatException if the parsing on the long array fails
	 */
	public static long[] toLongArray(String s) throws NumberFormatException{
		String[] arr = s.split(ARRAY_SEPARATOR);
		long[] ia = new long[arr.length];
		for(int i = 0; i < arr.length; i++){
			ia[i] = Long.parseLong(arr[i]);
		}
		return ia;
	}

	public static List<String> toStringList(String s){
		return new ArrayList<String>(Parser.LIST_PARSER.parseCommand(s).getArguments().keySet());
	}

	/**
	 * A parser class that represents methods used the parse a command.
	 * A default parser is provided to be used by the Commands methods.
	 *
	 * @author Markus Feng
	 */
	protected static class Parser{

		/**
		 * The default parser.
		 * Equivalent to new Parser("[", "]", ";", "=", "\\");
		 */
		protected static final Parser DEFAULT_PARSER = new Parser("[", "]", ";", "=", "\\", true);

		/**
		 * The list parser.
		 * Equivalent to new Parser("", "", ARRAY_SEPARATOR, "", "\\");
		 */
		protected static final Parser LIST_PARSER = new Parser("", "", ARRAY_SEPARATOR, "", "\\", false);

		/**
		 * The value at the initial portion of the command.
		 */
		protected String init;

		/**
		 * The value at the final portion of the command.
		 */
		protected String fin;

		/**
		 * The value that separated each command entry.
		 */
		protected String entrySeparator;

		/**
		 * The value that separated each key-value pair in a command entry.
		 */
		protected String keyValueSeparator;

		/**
		 * The value for escaping values in the command.
		 */
		protected String escape;

		/**
		 * Whether the first argument should be parsed as the name
		 */
		protected boolean hasName;

		/**
		 * Creates an new parser with the given init, fin, entrySeparator, keyValueSeparator, and escape.
		 * @param init The value at the initial portion of the command
		 * @param fin The value at the final portion of the command
		 * @param entrySeparator The value that separated each command entry
		 * @param keyValueSeparator The value that separated each key-value pair in a command entry
		 * @param escape The value for escaping values in the command
		 * @param hasName Whether the first argument should be parsed as the name
		 */
		protected Parser(String init, String fin, String entrySeparator, String keyValueSeparator, String escape,
				boolean hasName){
			this.init = init;
			this.fin = fin;
			this.entrySeparator = entrySeparator;
			this.keyValueSeparator = keyValueSeparator;
			this.escape = escape;
		}

		/**
		 * Parses a command of the given input string.
		 * @param in The input string to parse the command from
		 * @return The parsed command
		 */
		protected Command parseCommand(String in){
			//A command must start with init and end with fin
			if(!(in.startsWith(init) && in.endsWith(fin))){
				throw new IllegalArgumentException("Illegal command.");
			}
			Map<String, String> hm = new LinkedHashMap<String, String>();
			//Creates an empty command
			if(in.length() == init.length() + fin.length()){
				return make("");
			}
			String name = null;
			//Cut off the init and fin
			in = in.substring(0, in.length() - fin.length());
			in = in.substring(init.length());
			//Splits the command by the entries
			String[] sa = in.split(escaperString(in) + entrySeparator);
			//Splits each entry (except the first) by key-value pairs
			for(String sn : sa){
				if(sn.length() == 0){
					continue;
				}
				//Passes the first entry to the name
				if(hasName && name == null){
					name = sn;
					continue;
				}
				if(keyValueSeparator.length() == 0){
					hm.put(commandUnescape(sn), "");
				}
				else{
					//Splits the entry by the key-value separator
					String[] sna = sn.split(escaperString(sn) + keyValueSeparator);
					String object;
					if(sna.length > 1){
						object = commandUnescape(sna[1]);
					}
					else{
						object = "";
					}
					String meta = commandUnescape(sna[0]);
					//Puts the key-value pair into the command
					hm.put(meta, object);
				}
			}
			if(!hasName){
				name = "";
			}
			return make(name, hm);
		}

		/**
		 * Unescapes the portion of the command/
		 * @param in the portion to unescape
		 * @return the unescaped string
		 */
		private String commandUnescape(String in){
			in = in.replaceAll(escaperString(in) + "\\" + escape + entrySeparator, entrySeparator);
			in = in.replaceAll(escaperString(in) + "\\" + escape + keyValueSeparator, keyValueSeparator);
			in = in.replace(escape + escape, escape);
			return in;
		}

		/**
		 * The string to help the regex find escaped portions.
		 * @param in the string in which to find escaped portions
		 * @return the escaper string used in the regex
		 */
		private String escaperString(String in){
			return "(?<=([^\\" + escape + "])(\\" + escape + "\\" + escape + "){0," + in.length() / 2 + "})";
		}

		/**
		 * Converts a command to its string representation.
		 * @param command The command to convert
		 * @return The string representation of the command
		 */
		public String commandToString(Command command){
			//Adds the initial string to the command
			String s = init;
			s += command.getName();
			//Adds each entry to the String
			for(Map.Entry<String, String> entry : command.getArguments().entrySet()){
				s += entrySeparator + commandEscape(entry.getKey().toString()) + keyValueSeparator
						+ commandEscape(entry.getValue().toString());
			}
			//Adds the final string to the command
			s += fin;
			return s;
		}

		/**
		 * Escapes the portion of the command.
		 * @param in the portion to escape
		 * @return the escaped string
		 */
		private String commandEscape(String in){
			in = in.replace(escape, escape + escape);
			in = in.replace(keyValueSeparator, escape + keyValueSeparator);
			in = in.replace(entrySeparator, escape + entrySeparator);
			return in;
		}

		public static Parser defaultParser(){
			return DEFAULT_PARSER;
		}
	}
}
