/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moidel;

/**
 *
 * @author user
 */
public class model_hasilperiksa {
    private String id_pemeriksaan;
    private model_pasien no_rm;
    private String tanggal;
    private String nama_pasien;
    private String nama_dokter;
    private String layanan;
    private String hasil;
    
    public String getId_pemeriksaan() {
        return id_pemeriksaan;
    }

    public void setId_pemeriksaan(String id_pemeriksaan) {
        this.id_pemeriksaan = id_pemeriksaan;
    }

    public model_pasien getNo_rm() {
        return no_rm;
    }

    public void setNo_rm(model_pasien no_rm) {
        this.no_rm = no_rm;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNama_pasien() {
        return nama_pasien;
    }

    public void setNama_pasien(String nama_pasien) {
        this.nama_pasien = nama_pasien;
    }

    public String getNama_dokter() {
        return nama_dokter;
    }

    public void setNama_dokter(String nama_dokter) {
        this.nama_dokter = nama_dokter;
    }

    public String getLayanan() {
        return layanan;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    }

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }
}
