 package com.RobotArm.business;

import com.RobotArm.jsonAdapters.TacheAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import lejos.utility.Delay;

@JsonAdapter(TacheAdapter.class)

/**
 * Classe repr�sentant une t�che
 * @author Alvin
 *
 */
public class Tache {
	private String id;
	private String description;
	private TypeAction typeAction;
	private int valeur;
	private Moteur moteur;

	public enum TypeAction {@SerializedName("Attendre") Attendre, @SerializedName("Tourner") Tourner; }

	public Tache(TypeAction ta, int v) {
		setTypeAction(ta);
		this.valeur = v;
	}
 
	/**
	 * Cr�e une t�che d'attente
	 * @param id
	 * @param description
	 * @param typeAction
	 * @param valeur
	 */
	public Tache(String id, String description, int valeur)
	{
		this.id = id;
		this.description = description;
		this.typeAction = TypeAction.Attendre;
		this.valeur = valeur;
		
		this.moteur = null;
	}

	/**
	 * Cr�e une t�che de rotation
	 * @param id
	 * @param description
	 * @param typeAction
	 * @param valeur
	 * @param moteur
	 */
	public Tache(String id, String description, int valeur, char moteur)
	{		
		this.id = id;
		this.description = description;
		this.typeAction = TypeAction.Tourner;
		this.valeur = valeur;
		
		this.moteur = Moteur.getInstance(moteur);
	}
	

	public void executer()
	{
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
}

