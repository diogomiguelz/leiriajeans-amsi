package com.example.leiriajeansamsi.Modelo;

public class Utilizador {
    private int id;
    private String username;
    private String primeironome;
    private String apelido;
    private String email;
    private String codigopostal;
    private String rua;
    private String localidade;
    private String dtanasc;
    private String telefone;
    private String nif;
    private String genero;
    private String auth_key;
    private String password_hash;
    private String password_reset_token;
    private String status;
    private String created_at;
    private String updated_at;
    private String verification_token;

    public Utilizador(int id, String username, String primeironome, String apelido, String email,
                      String codigopostal, String rua, String localidade, String dtanasc,String telefone, String nif,
                      String genero, String auth_key, String password_hash, String password_reset_token,
                      String status, String created_at, String updated_at, String verification_token) {
        this.id = id;
        this.username = username;
        this.primeironome = primeironome;
        this.apelido = apelido;
        this.email = email;
        this.codigopostal = codigopostal;
        this.rua = rua;
        this.localidade = localidade;
        this.dtanasc = dtanasc;
        this.telefone = telefone;
        this.nif = nif;
        this.genero = genero;
        this.auth_key = auth_key;
        this.password_hash = password_hash;
        this.password_reset_token = password_reset_token;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.verification_token = verification_token;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPrimeironome(String primeironome) {
        this.primeironome = primeironome;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public void setDtanasc(String dtanasc) { this.dtanasc = dtanasc; }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public void setGenero(String nif) {
        this.genero = genero;
    }

    public void setAuth_key(String auth_key) { this.auth_key = auth_key; }

    public void setPassword_hash(String password_hash) { this.password_hash = password_hash; }

    public void setPassword_reset_token(String password_reset_token) { this.password_reset_token = password_reset_token; }

    public void setStatus(String status) { this.status = status; }

    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }

    public void setVerification_token(String verification_token) { this.verification_token = verification_token; }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPrimeironome() {
        return primeironome;
    }

    public String getApelido() { return apelido; }

    public String getEmail() {
        return email;
    }

    public String getCodigopostal() {
        return codigopostal;
    }

    public String getRua() {
        return rua;
    }

    public String getLocalidade() {
        return localidade;
    }

    public String getDtanasc() { return dtanasc; }

    public String getTelefone() {
        return telefone;
    }

    public String getNif() {
        return nif;
    }

    public String getGenero() {
        return genero;
    }

    public String getAuth_key() { return auth_key; }

    public String getPassword_hash() { return password_hash; }

    public String getPassword_reset_token() { return password_reset_token; }

    public String getStatus() { return status; }

    public String getCreated_at() { return created_at; }

    public String getUpdated_at() { return updated_at; }

    public String getVerification_token() { return verification_token; }


}
