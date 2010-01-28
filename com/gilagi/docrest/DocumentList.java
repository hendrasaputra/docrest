package com.gilagi.docrest;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * Resource that manages a list of items.
 * 
 */
public class DocumentList extends BaseResource {

	
	public DocumentList(Context context, Request request, Response response) {
		super(context, request, response);

		// Allow modifications of this resource via POST requests.
        setModifiable(true);

		// Declare the kind of representations supported by this resource.
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
		getVariants().add(new Variant(MediaType.TEXT_HTML));
	}
	
	/*
	 * Handle POST request: create new item
	 * 
	 * @see
	 * org.restlet.resource.Resource#acceptRepresentation(org.restlet.resource
	 * .Representation)
	 */
	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		
		if (entity != null) {
			if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(),
					true)) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1000240);
				RestletFileUpload upload = new RestletFileUpload(factory);
				List<FileItem> items;
				UUID id = UUID.randomUUID();
				StringBuilder sbFiles = new StringBuilder("");
				try {
					// 3/ Request is parsed by the handler which generates a
					// list of FileItems
					items = upload.parseRequest(getRequest());

					// Process only the uploaded item called "fileToUpload" and
					// save it on disk
					boolean found = false;
					for (final Iterator<FileItem> it = items.iterator(); it
							.hasNext()
							&& !found;) {
						FileItem fileItem = it.next();
						// Process file and write file to specified directory
						if (fileItem.getFieldName().equals(
								Messages.getString("CONST_FIELDNAME"))) {
							found = true;
							String fileName = String.valueOf(id)+"_"+fileItem.getName();
							// String contentType = fileItem.getContentType();
							File file = new File(Messages
									.getString("CONST_PATHUPLOAD")
									+fileName);
							fileItem.write(file);
							
							getDocuments().put(fileName, new DocumentNode(fileName,Messages.getString("CONST_PATHUPLOAD")+fileName));
							sbFiles.append(fileName).append("|");
						}
					}

					// Once handled, the content of the uploaded file is sent
					// back to the client.
					Representation rep = null;
					if (found) {
						// Create a new representation based on disk file.
						// The content is arbitrarily sent as plain text.
						rep = new StringRepresentation(Messages
								.getString(sbFiles.toString()), MediaType.TEXT_PLAIN);
						
					} else {
						// Some problem occurs, sent back a simple line of text.
						rep = new StringRepresentation(Messages
								.getString("CONST_MSGERR_TRANSFER"),
								MediaType.TEXT_PLAIN);
					}
					// Set the representation of the resource once the POST
					// request has been handled.
					getResponse().setEntity(rep);
					// Set the status of the response.
					getResponse().setStatus(Status.SUCCESS_OK);
				} catch (Exception e) {
					// The message of all thrown exception is sent back to
					// client as simple plain text
					getResponse().setEntity(
							new StringRepresentation(e.getMessage(),
									MediaType.TEXT_PLAIN));
					getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * Returns a listing of all registered items.
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		// Generate the right representation according to its media type.
		// TODO: Get file list and display with links
		StringRepresentation representation = new StringRepresentation(
				getFileList(), MediaType.TEXT_HTML);
		return representation;
	}
	
	/**
     * Generate an String representation of an error response.
     * 
     * @param errorMessage
     *            the error message.
     * @param errorCode
     *            the error code.
     */
    @SuppressWarnings("unused")
	private void generateErrorRepresentation(String errorMessage, String errorCode, Response response) {
        // This is an error
        response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        StringRepresentation representation = new StringRepresentation("Error Uploading Document", MediaType.TEXT_PLAIN);
		
		response.setEntity(representation);
    }
}
