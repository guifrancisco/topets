package com.example.topets.model;

import java.util.Objects;

public class MedicalAppointment extends Activity{
    private String local;
    private String descricao;

    public MedicalAppointment(String nome, Pet pet, String local, String descricao) {
        super(nome, pet);
        this.local = local;
        this.descricao = descricao;
    }

    public String getLocal() {
        return local;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MedicalAppointment that = (MedicalAppointment) o;
        return local.equals(that.local) && descricao.equals(that.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), local, descricao);
    }
}
