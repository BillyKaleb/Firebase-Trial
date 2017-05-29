package com.kuliah.firebase.tesfiredb;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by MuhammadAminul on 5/18/2017.
 */
@IgnoreExtraProperties
public class Anggota {

    private String nama;
    private String alamat;

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }


    public Anggota(String nama, String alamat) {
        this.nama = nama;
        this.alamat = alamat;
    }

    public Anggota() {
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
