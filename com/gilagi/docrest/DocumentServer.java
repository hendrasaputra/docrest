package com.gilagi.docrest;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;

public class DocumentServer extends Application {

	/** The list of items is persisted in memory. */
	private final ConcurrentMap<String, DocumentNode> documents = new ConcurrentHashMap<String, DocumentNode>();

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public synchronized Restlet createRoot() {
		// Create a router Restlet that defines routes.
		Router router = new Router(getContext());

		// Defines a route for the resource "list of items"
		router.attach("/docs/", DocumentList.class);
		// Defines a route for the resource "item"
		router.attach("/docs/{docName}", DocumentResource.class);

		this.readPath(Messages.getString("CONST_PATHUPLOAD"));
		return router;
	}

	/**
	 * Returns the list of registered items.
	 * 
	 * @return the list of registered items.
	 */
	public ConcurrentMap<String, DocumentNode> getDocuments() {
		return documents;
	}

	public ConcurrentMap<String, DocumentNode> readPath(String path) {
		final String fileFilter = ".pdf";
		File dir = new File(path);
		FilenameFilter filter;

		filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(fileFilter);
			}
		};
		
		String [] files = dir.list(filter);
		
		if (files == null) {
		    // Either dir does not exist or is not a directory
		} else {
		    for (int i=0; i<files.length; i++) {
		        documents.putIfAbsent(files[i], new DocumentNode(files[i],path+files[i])); 
		    }
		}
		return documents;
	}
	
	public String getFileList() {
		final String fileFilter = ".pdf";
		File dir = new File(Messages.getString("CONST_PATHUPLOAD"));
		FilenameFilter filter;
		String filelist = "";
		StringBuilder sb = new StringBuilder();
		filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(fileFilter);
			}
		};
		
		String [] files = dir.list(filter);
		
		if (files == null) {
		    // Either dir does not exist or is not a directory
		} else {
		    for (int i=0; i<files.length; i++) {
		        sb.append("<a href=\"").append(files[i]).append("\">").append(files[i]).append("</a><br>"); 
		    }
		    filelist = sb.toString();
		}
		return filelist;
	}
}
