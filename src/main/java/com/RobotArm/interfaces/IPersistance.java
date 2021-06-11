package com.RobotArm.interfaces;

import com.RobotArm.business.Gamme;
import com.RobotArm.business.Utilisateur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface impl�ment�e par le syst�me de persistance, pour garder en m�moire les donn�es
 * @author Alvin
 *
 */
public interface IPersistance {
	/**
	* Sauvegarde une gamme dans la persistance
	* @param paramGamme Gamme � sauvegarder
	*
	*/
	void creerGamme(Gamme paramGamme);
	
	/**
	* Modifie une gamme dans la persistance
	* @param paramGamme Gamme � modifier
	*
	*/
	void modifierGamme(Gamme paramGamme);
	
	/**
	* Supprime une gamme dans la persistance
	* @param paramString Gamme � supprimer
	*/
	void supprimerGamme(String paramString);
	
	/**
	* @return Liste des gammes sauvegard�es
	*/
	HashMap<String, Gamme> recupererGammes();
	
	/**
	* 
	* @return Retourne la gamme d�finie par d�faut, utilis�e pour le mode automatique
	*/
	Gamme recupererGammeDefaut();
	
	/**
	* Sauvegarde un log dans la persistance
	* @param paramString
	*
	*/
	void sauverLog(String paramString);
	
	/**
	* @return Liste des logs r�cents
	*
	*/
	ArrayList<String> recupererLogs();
	
	/**
	*  Retourne la valeur d'un param�tre sauvegard� en persistance
	* @param paramString Nom du param�tre � lire
	* @return Valeur du param�tre pass� en param�tre, ou null si le param�tre n'existe pas
	*
	*/
	String getConfig(String paramString);
	
	/**
	* Cr�e un compte dans la persistance
	* @param login Identifiant de l'utilisateur
	* @param pwd Mot de passe chiffr� de l'utilisateur
	*
	*/
	void creerCompte(String login, String pwd);

	/**
	* @param l Identifiant de l'utilisateur
	* @param p Mot de passe
	* @return Utilisateur trouv� ou null
	*/
	Utilisateur trouverCompte(String l, String p);
	
	/**
	* Supprime un compte utilisateur
	* @param login Identifiant du compte � supprimer
	*
	*/
	void supprimerCompte(String login);
}
