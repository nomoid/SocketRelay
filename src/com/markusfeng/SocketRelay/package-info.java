/**
 * The com.github.assisstion.Communicator.relay API contains classes to
 * enable programmers to easily connect from one Socket to another and to
 * provide custom functionality associated with the sockets. Each package
 * that is labeled with a letter of the alphabet is a stand-alone package
 * that depends only on packages with a letter closer to the beginning of
 * the alphabet, which all eventually depend on this package. Each package
 * later in the alphabet provide higher-level programming functionality
 * than the previous, allowing programmers to choose feasibility, flexibility,
 * or a mixture of both when working with the API. This API is still being
 * updated, so not all interfaces are in their final form. Feel free to
 * extend any class to provide your own additional functionality.
 *
 * This API uses a significant amount of the java.net, java.io,
 * java.util, and java.util.concurrent packages in its code.
 *
 * This particular package contains only interfaces that together define
 * the functionality of SocketClients and SocketServers without providing
 * an implementation.
 *
 * @author Markus Feng
 */
@Version(value="API 1.1.5", lastUpdate = "2014-10-30")
package com.markusfeng.SocketRelay;

import com.markusfeng.Shared.Version;
/*
 * Version History:
 *
 * (Macro version indicates no outgoing API changes)
 * (Minor version indicates no incompatibilities introduced)
 * (Major version indicates major changes)
 *
 * API 1.1.5 (2014-10-30)
 *   Fixed variable hiding bugs
 *
 * API 1.1.4b (2014-10-16)
 *   Changed compile compatibility from 1.8 to 1.6
 *
 * API 1.1.4 (2014-10-15)
 *   Added Thread Pools to improve performance
 *
 * API 1.1.3 (2014-10-12)
 *   Fixed concurrency handlers on API
 *
 * API 1.1.2 (2014-10-10)
 *   Fixed optional redirect
 *   Changed SocketListener system
 *
 * API 1.1.1 (2014-10-10)
 *   Added optional redirect for SocketHandlerHub
 *
 * API 1.1.0 (2014-10-10)
 *   Added SocketHandlerHub
 *   Moved around interfaces
 *   Class changes - added methods
 *
 * API 1.0.1 (2014-10-10)
 *   Added exception handling to various parts of the API
 *
 * API 1.0.0 (2014-10-05)
 *   Added previous work from Communicator
 *   Includes packages:
 *     com.markusfeng.SocketRelay
 *     com.markusfeng.SocketRelay.A
 *     com.markusfeng.SocketRelay.B
 *     com.markusfeng.SocketRelay.C
 *     com.markusfeng.SocketRelay.L
 *     com.markusfeng.SocketRelay.Stream
 *     com.markusfeng.SocketRelay.String
 */

