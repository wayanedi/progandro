package com.example.test;

import java.security.PrivateKey;

public class Matakuliah {

    private String namaMatkul;
    private String namaDosen;
    private int sks;

    public Matakuliah(){

    }

    public Matakuliah(String namaMatkul, String namaDosen, int sks){

        this.namaMatkul = namaMatkul;
        this.namaDosen=  namaDosen;
        this.sks = sks;

    }


    public String getNamaMatkul() {
        return namaMatkul;
    }


    public String getNamaDosen() {
        return namaDosen;
    }

    public int getSks() {
        return sks;
    }
}
