 package com.RobotArm.pilotage;

import com.RobotArm.interfaces.IPilotage;
import com.RobotArm.interfaces.IPilote;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lejos.utility.Delay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class WifiListener
	implements IPilotage
{
	private ServerSocket socket;
	private Socket client;
	private boolean wifiOn = true;
	private int port = 2048;
	private IPilote pilote;
	private Future execThread;
	private ExecutorService execService = Executors.newSingleThreadExecutor();
	
	private BufferedReader in;
	private BufferedWriter out;

	/**
	* Lance le thread permettant d'�couter sur la connexion Wifi
	*/
	public void ecouter() {
		this.execThread = this.execService.submit(new Runnable()
		{			
			public void run()
			{
				try
				{
					// En cas d'erreur, on interrompt le thread
					socket = new ServerSocket(port);
					System.out.println(String.format("Ecoute sur le port : %d", port));							
					
					// Tant que le robot doit �couter, et que le socket n'est pas ferm�
					while(wifiOn && !socket.isClosed())
					{
						// On accepte une connexion et on obtient un socket vers le client connect�
						// On r�cup�re les buffers d'entr�e et de sortie
						client = socket.accept();
						in = new BufferedReader(new InputStreamReader(client.getInputStream()));
						out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
						
						// Et on attend un message de sa part.
						while(!client.isClosed())
						{
							String s ="";
							System.out.println("Attente d'un message...");								
					if((s = in.readLine()) != null)
							{
						System.out.println(String.format("Message re�u : %s", s));
								
						// Notification du contr�leur
								notifierPilote(s);
							}
					else
					{
						client.close();
					}
					Delay.msDelay(500); // Ajout d'un petit d�lai pour ne pas surcharger le thread
						}
						// Notification au contr�leur de la d�connexion
						notifierDeconnexion();						
					}		
					System.out.println("Wifi termin�e");
				} catch(SocketException e)
				{
					System.out.println("Arret du socket serveur.");
				} catch (Exception e)
				{							
					e.printStackTrace();
					return;
				}
			}
		});		
	}
		
	/**
	* Notifie le contr�leur du message re�u par le thread
	* @param message
	*/
	private void notifierPilote(String message) {
		this.pilote.notifierMessage(message);
	}
	
	/**
	* Notifie le contr�leur de la fermeture du thread, en lui ordonnant de d�connecter l'utilisateur
	*/
	private void notifierDeconnexion()
	{
		JsonObject root = new JsonObject();
		root.addProperty("action", "deco");
		notifierPilote(new Gson().toJson(root));
	}

	/**
	* Envoie au socket connect� les informations sur l'�tat du robot
	*/
	public void afficherEtatSysteme() {}

	/**
	* Envoie au socket connect� les derniers logs en date
	*/
	public void afficherHistorique(ArrayList<String> rapports) {}

	/**
	* Envoie au socket connect� un message JSON stringifi� contenant des informations
	* @param message Message envoy�, cha�ne en format JSON ou texte
	*/
	public void envoyerMessage(String message)
	{
		try
		{			
			if(client.isConnected())
			{
				out.write(String.format("%s\n", message));
				out.flush();				
			}
		} catch (Exception e) {
			System.out.println(String.format("Impossible d'envoyer un message : %s", e.getMessage()));
		}
	}

	/**
	* Arr�te le thread d'�coute et le cl�t
	*/
	public void stop() {
		this.wifiOn = false;
		fermerConnexion();
		this.execThread.cancel(true);
		this.execService.shutdownNow();
	}

	/**
	* Ferme la connexion avec le client, et ferme ensuite le socket d'�coute
	*/
	public void fermerConnexion() {
		try {
			this.client.close();
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affecte le "listener", l'objet qui sera notifi� des messages re�us
	 * @param listener Listener qui �coutera
	 */
	public void ajoutListener(IPilote listener) {
		this.pilote = listener;
	}
}
