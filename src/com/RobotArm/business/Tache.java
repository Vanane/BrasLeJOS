package com.RobotArm.business;

import com.RobotArm.jsonAdapters.TacheAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lejos.utility.Delay;


@JsonAdapter(TacheAdapter.class)

/*
 * Classe représentant une tâche
 * @author Alvin
 *
 */
public class Tache {
    private String id;
    private String description;
    private TypeAction typeAction;
    private final int valeur;
    private Moteur moteur;

    public enum TypeAction {@SerializedName("Attendre") Attendre, @SerializedName("Tourner") Tourner;}

    public Tache(TypeAction ta, int v) {
        setTypeAction(ta);
        this.valeur = v;
    }

    /**
     * Crée une tâche d'attente
     *
     * @param id
     * @param description
     * @param valeur
     */
    public Tache(String id, String description, int valeur) {
        this.id = id;
        this.description = description;
        this.typeAction = TypeAction.Attendre;
        this.valeur = valeur;

        this.moteur = null;
    }

    /**
     * Crée une tâche de rotation
     *
     * @param id
     * @param description
     * @param valeur
     * @param moteur
     */
    public Tache(String id, String description, int valeur, char moteur) {
        this.id = id;
        this.description = description;
        this.typeAction = TypeAction.Tourner;
        this.valeur = valeur;

        this.moteur = Moteur.getInstance(moteur);
    }


    public void executer() {
        switch (getTypeAction()) {
            case Attendre:
                Delay.msDelay(this.valeur);
                break;
            case Tourner:
                this.moteur.tourner(this.valeur);
                this.moteur.stop();
                break;
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeAction getTypeAction() {
        return this.typeAction;
    }

    public void setTypeAction(TypeAction typeAction) {
        this.typeAction = typeAction;
    }

    public Moteur getMoteur() {
        return this.moteur;
    }

    public int getValeur() {
        return valeur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tache tache = (Tache) o;
        return id.equals(tache.id);
    }

}

