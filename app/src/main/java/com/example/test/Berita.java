package com.example.test;

public class Berita {

    private String judul;
    private String desc;

    public Berita(String judul, String desc){
        this.judul = judul;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getJudul() {
        return judul;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
}
