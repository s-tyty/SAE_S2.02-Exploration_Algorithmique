package graphe.impl;

import graphe.modele.IEntite;

import java.util.Objects;

public final class Entite implements IEntite {
    private final String nom;
    private final TypeEntite type;

    public Entite(String nom, TypeEntite type) {
        this.nom = Objects.requireNonNull(nom);
        this.type = Objects.requireNonNull(type);
    }

    @Override
    public String nom() {
        return nom;
    }

    @Override
    public boolean estType() {
        return type != TypeEntite.PACKAGE;
    }

    public TypeEntite type() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Entite other)) {
            return false;
        }
        return nom.equals(other.nom);
    }

    @Override
    public int hashCode() {
        return nom.hashCode();
    }

    @Override
    public String toString() {
        return nom + " [" + type + "]";
    }
}