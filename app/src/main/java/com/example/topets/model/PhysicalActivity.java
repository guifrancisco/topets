package com.example.topets.model;

import java.util.Objects;
import java.util.UUID;

public class PhysicalActivity extends Activity{
    private String local;

    public PhysicalActivity(String nome, Pet pet, String local) {
        super(nome, pet);
        this.local = local;
    }

    public String getLocal() {
        return local;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PhysicalActivity that = (PhysicalActivity) o;
        return local.equals(that.local);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), local);
    }
}
