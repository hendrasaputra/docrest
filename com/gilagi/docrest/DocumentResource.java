package com.gilagi.docrest;

import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

public class DocumentResource extends BaseResource {

	/** The sequence of characters that identifies the resource. */
	String docName;

	/** The underlying Item object. */
	DocumentNode docNode;

	public DocumentResource(Context context, Request request, Response response) {
		super(context, request, response);

		// Get the "itemName" attribute value taken from the URI template
		// /docs/{docName}.
		
		try{
			   String encodedurl = URLDecoder.decode((String) getRequest().getAttributes().get("docName"),"UTF-8"); 
			   this.docName = encodedurl;
		}catch(UnsupportedEncodingException uee){
			   System.err.println(uee);
		}
		

		// Get the item directly from the "persistence layer".
		this.docNode = getDocuments().get(docName);

		if (this.docNode != null) {
			// Define the supported variant.
			getVariants().add(new Variant(MediaType.APPLICATION_PDF));
            setModifiable(true);
		} else {
			setAvailable(true);
		}
	}

	/*
	 * Handle GET requests
	 * 
	 * @see
	 * org.restlet.resource.Resource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		// Generate the right representation according to its media type.
		if (MediaType.APPLICATION_PDF.equals(variant.getMediaType())) {
			Representation representation = new FileRepresentation(this.docNode.getPath(), MediaType.APPLICATION_PDF);
			return representation;
		}
		return null;
	}
}
