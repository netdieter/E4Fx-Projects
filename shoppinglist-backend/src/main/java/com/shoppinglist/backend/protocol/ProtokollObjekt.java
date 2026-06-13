package com.shoppinglist.backend.protocol;

public class ProtokollObjekt {
    private String key;
    private String typ;
    private String file;

    public ProtokollObjekt() {
    }

    public ProtokollObjekt(String key, String typ, String file) {
        this.key = key;
        this.typ = typ;
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
