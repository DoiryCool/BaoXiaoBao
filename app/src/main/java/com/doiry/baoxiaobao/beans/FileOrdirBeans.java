package com.doiry.baoxiaobao.beans;

public class FileOrdirBeans {
    String isfile, name, ischeckvisible, ischecked, absolutepath ;

    public FileOrdirBeans(String... params) {
        this.isfile = params[0];
        this.name = params[1];
        this.ischeckvisible = params[2];
        this.ischecked = params[3];
        this.absolutepath = params[4];
    }

    public String getisfile() {
        return isfile;
    }

    public void setisfile(String isfile) {
        this.isfile = isfile;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getischeckvisible() {
        return ischeckvisible;
    }

    public void setischeckvisible(String ischeckvisible) {
        this.ischeckvisible = ischeckvisible;
    }

    public String getischecked() {
        return ischecked;
    }

    public void setischecked(String ischecked) {
        this.ischecked = ischecked;
    }

    public String getabsolutepath() {
        return absolutepath;
    }

    public void setabsolutepath(String absolutepath) {
        this.absolutepath = absolutepath;
    }
}