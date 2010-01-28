package com.gilagi.docrest;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class DocumentServerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Component component  = new Component();
		component.getServers().add(Protocol.HTTP,8182);
		component.getDefaultHost().attach("", new DocumentServer());
		try {
			component.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
