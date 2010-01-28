package com.gilagi.docrest;

public class DocumentNode {
    private String docpath;
    private String docname;

    public DocumentNode(String name) {
        super();
        setName(name);
    }

    public DocumentNode(String name, String path) {
        super();
        setName(name);
        setPath(path);
    }

    public String getPath() {
        return docpath;
    }

    public String getName() {
        return docname;
    }

    public void setPath(String path) {
        this.docpath = path;
    }

    public void setName(String name) {
        this.docname = name;
    }

}