Simple Document Repository Using RESTlet
========================================

REST architecture basically utilized [URI](http://www.ietf.org/rfc/rfc2396.txt "Uniform Resource Identifier") for a restricted set of operation done via HTTP vocabulary. The most common vocabulary is GET, POST, PUT and DELETE. In my current project we need to have a simple way to store and retrieve PDF documents via a web server.

You might ask why I choose to use REST and not simply code a simple PHP upload script. Currently where I work its often difficult to add or install application to a server. I have a web server on `Server#1`, in which storage is limited and I can't use it to store files other than web pages/scripts. I have another server `Server#2` which is a database and processing server that has large amount of free space but doesn't have any web server on it. So I think why not make a simple web server on Server#2 and expose POST/GET API to store/retrieve pdf.

I use [Restlets](http://www.restlet.org), a simple REST framework build with Java. Using Restlets every URI is represented with a Resources class which will return a Representation if called. For this project I have two REST URI

	http://server/docs/ (POST)
	http://server/docs/{documentName} (GET)

Its very simple, if I want to upload a document I just need to do a POST with form and `<input type="file">`. If the operation is successful it will return the file name. For download I would just do a usual get to the specified URL e.g. 

	GET http://server/docs/sample.pdf HTTP/1.1
	
or I would just direct my browser to the specified URL. I add a simple listing if you direct your URL to `http://server/docs/`