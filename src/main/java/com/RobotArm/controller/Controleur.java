 package com.RobotArm.controller;
 
 import com.RobotArm.business.Gamme;
 import com.RobotArm.business.ThreadGamme;
 import com.RobotArm.interfaces.IEtatMode;
 import com.RobotArm.interfaces.IExecuteur;
 import com.RobotArm.interfaces.IPersistance;
 import com.RobotArm.interfaces.IPilotage;
 import com.RobotArm.interfaces.IPilote;
 import com.google.gson.Gson;
 import com.google.gson.GsonBuilder;
 import com.google.gson.JsonElement;
 import com.google.gson.JsonObject;
 import com.google.gson.JsonParseException;
 import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import lejos.hardware.motor.Motor;

import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.HashMap;
 
 public class Controleur
	implements IExecuteur, IPilote
 {
	HashMap<String, Gamme> listeGammes;
	Gamme gammeDefaut;
	IPersistance persistance;
	IEtatMode modeFonctionnement;
	IPilotage pilotage;
	ThreadGamme execGammeService;
	
	public Controleur(IPersistance pe, IPilotage pi) throws Exception {
		this.persistance = pe;
		this.pilotage = pi;
		this.listeGammes = new HashMap<>();
		this.gammeDefaut = new Gamme();
		this.modeFonctionnement = new ModeManuel();
		
		initRobot();
		initGamme();
	}
 
	
	public void demarrer() {
		System.out.println("Démarrage du contrôleur");
		this.pilotage.ecouter();
		while (true);
	}
 
 
 
 
	
	private void initRobot() throws NumberFormatException, Exception {
		int vitesseMoteur = Integer.parseInt(this.persistance.getConfig("vitesseMoteur"));
		int accelerationMoteur = Integer.parseInt(this.persistance.getConfig("accelerationMoteur"));
		int rendementMotA = Integer.parseInt(this.persistance.getConfig("rendementMotA"));
		int rendementMotB = Integer.parseInt(this.persistance.getConfig("rendementMotB"));
		int rendementMotC = Integer.parseInt(this.persistance.getConfig("rendementMotC"));


		Motor.A.setSpeed(vitesseMoteur);
		Motor.B.setSpeed(vitesseMoteur);
		Motor.C.setSpeed(vitesseMoteur);
		
		Motor.A.setAcceleration(rendementMotA);
		Motor.B.setAcceleration(rendementMotB);
		Motor.C.setAcceleration(rendementMotC);		
		
		initPositionMoteurs();
	}
 
 
	
	private void initPositionMoteurs() {}
 
 
	
	private void initGamme() throws Exception {
		this.listeGammes = this.persistance.recupererGammes();
		this.gammeDefaut = this.persistance.recupererGammeDefaut();
	}
 
 
	
	public void sauverRapport(String r) {
		try {
			this.persistance.sauverLog(r);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 
	}
 
 
	
	public void executerGamme(String id) {
		if (this.modeFonctionnement.peutExecuter())
		{
			if (!gammeEnCours()) {
				
				Gamme gamme = this.listeGammes.get(id);
				if (gamme != null) {
					this.execGammeService.executer(gamme);
				} else {
					this.pilotage.envoyerMessage("La gamme demandée n'existe pas.");
				} 
			} else {
				
				this.pilotage.envoyerMessage("Gamme déjà  en cours.");
				return;
			} 
		}
	}
 
 
	
	public void notifierFinGamme() {
		if (this.modeFonctionnement.estAutonome())
		{
			executerGamme(this.gammeDefaut.getId());
		}
	}
 
 
 
 
	
	public ArrayList<String> filtrerLog(Date date) {
		try {
			ArrayList<String> rapports = this.persistance.recupererLogs();
			return rapports;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		} 
	}
 
	
	public void notifierMessage(String msg) {
		JsonObject root = new JsonObject();
		Gson gson = (new GsonBuilder()).create();

		try {
			root = JsonParser.parseString(msg).getAsJsonObject();
		}
		catch (Exception e)
		{		
			System.out.println("Message reçu invalide ! Le format JSON n'est pas respecté");
			e.printStackTrace();
			return;
		} 
		
		String action = root.get("action").getAsString();
		switch (action) {
			
			case "creerGamme":
				
				try {
					Gamme g = (Gamme)gson.fromJson((JsonElement)root.get("gamme").getAsJsonObject(), Gamme.class);
					
					if (g != null) {
						
						this.persistance.creerGamme(g);
						break;
					} 
					throw new Exception("Informations invalides");
				}
				catch (Exception e) {
					
					this.pilotage.envoyerMessage("La gamme n'a pas pu être créée.");
					sauverRapport(e.getMessage());
					break;
				} 
			
			case "modifierGamme":
				try {
					Gamme g = (Gamme)gson.fromJson((JsonElement)root.get("gamme").getAsJsonObject(), Gamme.class);
					if (g != null) {
						
						this.persistance.modifierGamme(g);
						break;
					} 
					throw new Exception("Informations invalides");
				}
				catch (Exception e) {
					
					this.pilotage.envoyerMessage("La gamme n'a pas pu Ãªtre modifiÃ©e.");
					sauverRapport(e.getMessage());
					break;
				} 
			
			case "supprimerGamme":
				try {
					String g = root.get("idGamme").getAsString();
					if (g != null) {
						
						this.persistance.supprimerGamme(g);
						this.listeGammes.remove(g);
						break;
					} 
					throw new Exception("Informations invalides");
				}
				catch (Exception e) {
					
					this.pilotage.envoyerMessage("La gamme n'a pas pu Ãªtre supprimÃ©e.");
					sauverRapport(e.getMessage());
					break;
				} 
			
			case "creerCompte":
				try {
					String login = root.get("login").getAsString();
					String pwd = root.get("pwd").getAsString();
					if (login != null && pwd != null) {
						
						this.persistance.creerCompte(login, pwd);
						break;
					} 
					throw new Exception("Informations invalides");
				}
				catch (Exception e) {
					
					this.pilotage.envoyerMessage("Le compte n'a pas pu être créé.");
					sauverRapport(e.getMessage());
					break;
				} 
			case "supprimerCompte":
				try {
					this.persistance.supprimerCompte(root.get("login").getAsString());
				} catch (SQLException e) {
					System.out.println("Erreur lors de la suppression du compte");
				} 
				break;
			case "modeAutonome":
				this.modeFonctionnement = new ModeAutonome();
				break;
			case "declencherPanne":
				declencherPanne();
				break;
			case "executerGamme":
				executerGamme(root.get("idGamme").getAsString());
				break;
			
			case "recupererLogs":
				if (root.get("date").getAsString() != null) {
					ArrayList<String> rapports = filtrerLog(new Date(root.get("d").getAsString())); break;
				} 
				try {
					ArrayList<String> rapports = this.persistance.recupererLogs();
					this.pilotage.afficherHistorique(rapports);
				} catch (SQLException e) {
					System.out.println("Erreur lors de la récupération des logs");
				} 
				break;
			case "ping":
				System.out.println("Ping re�u");
				this.pilotage.envoyerMessage("Pong !");
				break;
			default:
				System.out.println(String.format("Commande inconnue : %s", action));
				break;
		} 
		System.out.println("Message trait�");
	}
 
	
	public void declencherPanne() {
		this.modeFonctionnement = new ModePanne();
		this.execGammeService.stop();
		this.pilotage.envoyerMessage("Mode panne activé.");
	}
 
 
	
	public boolean gammeEnCours() {
		return this.execGammeService.gammeEnCours().booleanValue();
	}
 }
