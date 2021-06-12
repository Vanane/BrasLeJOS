package com.RobotArm.interfaces;

import java.util.ArrayList;

/**
 * Interface impl�ment�e par le syst�me de pilotage, afin de communiquer avec l'op�rateur
 * @author Alvin
 *
 */
public interface IPilotage {
	/**
	 * Transmet l'�tat du robot � l'op�rateur
	 */
	void afficherEtatSysteme();
	
	/**
	 * Transmet l'historique des logs � l'op�rateur
	 */
	void afficherHistorique(ArrayList<String> paramArrayList);
	
	/**
	 * Lance la logique de r�ception de messages du syst�me de pilotage
	 */
	void ecouter();
	
	/**
	 * D�finit le module pilote � notifier lors de la r�ception d'un message
	 */
	void ajoutListener(IPilote paramIPilote);
	
	/**
	 * Envoie un message � l'op�rateur
	 */
	void envoyerMessage(String paramString);
	
	/**
	 * Coupe la connexion entre le robot et l'op�rateur
	 */
	void fermerConnexion();
	
	/**
	 * Arr�te le syst�me de pilotage, en vue d'un arr�t du robot
	 */
	void stop();
}
