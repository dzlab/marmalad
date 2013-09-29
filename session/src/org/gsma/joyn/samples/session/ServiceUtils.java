/*******************************************************************************
 * Software Name : RCS IMS Stack
 *
 * Copyright (C) 2010 France Telecom S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.gsma.joyn.samples.session;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.Enumeration;

import org.gsma.joyn.capability.CapabilityService;
import org.gsma.joyn.samples.session.sdp.SdpUtils;

public class ServiceUtils {
	/**
	 * CRLF constant
	 */
	public final static String CRLF = "\r\n";

	/**
	 * Service ID constant
	 */
	public final static String SERVICE_ID = CapabilityService.EXTENSION_BASE_NAME + "=\"" +
			CapabilityService.EXTENSION_PREFIX_NAME + ".orange.texto\"";

	/**
	 * Construct an NTP time from a date in milliseconds
	 *
	 * @param date Date in milliseconds
	 * @return NTP time in string format
	 */
	public static String constructNTPtime(long date) {
		long ntpTime = 2208988800L;
		long startTime = (date / 1000) + ntpTime;
		return String.valueOf(startTime);
	}

	/**
	 * Returns the local IP address
	 *
	 * @return IP address
	 */
	public static String getLocalIpAddress() {
		try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = (NetworkInterface)en.nextElement();
	            for (Enumeration<InetAddress> addr = intf.getInetAddresses(); addr.hasMoreElements();) {
	                InetAddress inetAddress = (InetAddress)addr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
	            }
	        }
	        return null;
		} catch(Exception e) {
			return null;
		}
	}

	/**
	 * Returns local SDP
	 * 
	 * @param mode Connection mode (active or passive)
	 * @param port Local port
	 * @return SDP
	 */
	public static String getLocalSdp(String mode, int port) {
		String ntpTime = ServiceUtils.constructNTPtime(System.currentTimeMillis());
		String ipAddress = ServiceUtils.getLocalIpAddress();
		String sdp =
    		"v=0" + "\r\n" +
            "o=- " + ntpTime + " " + ntpTime + " " + SdpUtils.formatAddressType(ipAddress) + CRLF +
            "s=DEMO joyn API" + CRLF +
			"c=" + SdpUtils.formatAddressType(ipAddress) + CRLF +
            "t=0 0" + CRLF +
            "m=message " + port + " TCP/MSRP *" + CRLF +
            "a=path:" + "msrp://" + ipAddress + ":" + port + "/" + System.currentTimeMillis() + ";tcp" + CRLF +
            "a=setup:" + mode + CRLF +
			"a=sendrecv" + CRLF;
		return sdp;
	}

    /**
     * Generate a free TCP port number
     *
     * @param portBase TCP port base
     * @return Local TCP port
     */
    public static int generateLocalTcpPort(int portBase) {
    	int resp = -1;
		int port = portBase;
		while(resp == -1) {
			if (isLocalTcpPortFree(port)) {
				// Free TCP port found
				resp = port;
			} else {
				port++;
			}
		}
    	return resp;
    }
    
	/**
     * Test if the given local TCP port is really free (not used by
     * other applications)
     *
     * @param port Port to check
     * @return Boolean
     */
    private static boolean isLocalTcpPortFree(int port) {
    	boolean res = false;
    	try {
    		ServerSocket socket = new ServerSocket(port);
    		socket.close();
    		res = true;
    	} catch(IOException e) {
    		res = false;
    	}
    	return res;
    }    
}
