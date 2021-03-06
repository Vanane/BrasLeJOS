package com.RobotArm.business;
 
import com.RobotArm.interfaces.ICapteur;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.utility.Delay;
 
 /**
  * Classe repr�sentant un moteur du robot
  * @author Alvin
  *
  */
 public class Moteur
 {
	private char port;
	private double ratio;
	private ICapteur capteur;
	private NXTRegulatedMotor NXTMotor;
	private int sensRotation; // Sens vers lequel tourner pour que le moteur arrive en but�e de son capteur
		
	private static Moteur A, B, C, D;
	public static int SENS_NEGATIF = -1, SENS_POSITIF = 1;
	
	/** 
	 * Constructeur priv� pour les 4 instances uniques de la classe
	 * @param port Port sur lequel le moteur � instancier est branch�
	 * @param ratio Ratio de force entre le moteur et la fin de la cha�ne d'engrenages
	 * @param capteur Capteur li� au moteur
	 * @param vitesse Vitesse du moteur
	 * @param acceleration Acc�l�ration du moteur
	 * @param sensRotation Sens de rotation du moteur, permet d'inverser le sens naturel pour que le moteur tourne vers son capteur avec un angle positif, et s'�loigne du capteur avec un angle n�gatif 
	 * 
	 */
	private Moteur(char port, double ratio, ICapteur capteur, float vitesse, int acceleration, int sensRotation) {
		this.port = port;
		this.ratio = ratio;
		this.capteur = capteur;
		
		this.setSensRotation(sensRotation);
		
		switch (this.port)
		{
			 case 'A':
				NXTMotor = Motor.A;
				NXTMotor.setStallThreshold(1, 50);				
				break;
			 case 'B':
				NXTMotor = Motor.B;
				NXTMotor.setStallThreshold(5, 250);
				break;
			 case 'C':
				NXTMotor = Motor.C;
				NXTMotor.setStallThreshold(5, 250);
				break;
			 case 'D':
				NXTMotor = Motor.D;
				NXTMotor.setStallThreshold(5, 250);
				break;
}
		NXTMotor.setSpeed(vitesse);
		NXTMotor.setAcceleration(acceleration);
		NXTMotor.setStallThreshold(5, 250);
	}
	
	
	/**
	 * Permet d'initialiser une instance d'un des 4 moteurs de la classe Singleton Moteur
	 * @param port Port sur lequel le moteur � instancier est branch�
	 * @param ratio Ratio de force entre le moteur et la fin de la cha�ne d'engrenages
	 * @param capteur Capteur li� au moteur
	 * @param vitesse Vitesse du moteur
	 * @param acceleration Acc�l�ration du moteur
	 * @param sensRotation Sens de rotation du moteur, permet d'inverser le sens naturel pour que le moteur tourne vers son capteur avec un angle positif, et s'�loigne du capteur avec un angle n�gatif 
	 */
	public static void initMoteur(char port, float ratio, ICapteur capteur, float vitesse, int acceleration, int sensRotation)
	{
		switch(port)
		{
			case 'a':
			case 'A':
				if(A == null)
					A = new Moteur(port, ratio, capteur, vitesse, acceleration, sensRotation);
				break;
			case 'b':
			case 'B':
				if(B == null)
					B = new Moteur(port, ratio, capteur, vitesse, acceleration, sensRotation);
				break;				
			case 'c':
			case 'C':
				if(C == null)
					C = new Moteur(port, ratio, capteur, vitesse, acceleration, sensRotation);
				break;				
			case 'd':
			case 'D':
				if(D == null)
					D = new Moteur(port, ratio, capteur, vitesse, acceleration, sensRotation);
				break;				
		}
	}
	
	/**
	 * @param port Port du moteur
	 * @return Instance correspondant au port donn�
	 */
	public static Moteur getInstance(char port)
	{
		switch(port)
		{
			case 'a':
			case 'A':
				return A;
			case 'b':
			case 'B':
				return B;
			case 'c':
			case 'C':
				return C;
			case 'd':
			case 'D':
				return D;				
			default:
				return null;
		}
	}
	
	/**
	 * Arr�te imm�diatement tous les moteurs
	 */
	public static void stopAll()
	{
		Motor.A.stop();
		Motor.B.stop();
		Motor.C.stop();
		Motor.D.stop();
		if(A != null) A.stop();
		if(B != null) B.stop();
		if(C != null) C.stop();
		if(D != null) D.stop();
	}
 
	/**
	 * Permet de faire tourner un moteur sur un angle 
	 * @param degres Angle de rotation
	 */
	public void tourner(int degres) {
		
		if(degres == 0 || degres == -0)
			return;
		
		boolean aBouge = false; // Vrai si le moteur a commenc� � tourner
		float sens = Math.signum(degres);
		System.out.println(String.format("Moteur %s tourne sur %d", port, degres));			
		// On v�rifie tout d'abord si le moteur peut tourner
		// Conditions pour que moteur tourne :
		// Si le moteur doit tourner vers un angle positif, alors il tourne vers le capteur.
		// Dans ce cas, on v�rifie si le capteur d�tecte le moteur en but�e.
		if(this.capteur.getMesure() == 1 && sens > 0.0f)
		{
			System.out.println(String.format("Moteur %s en butee", port));
			return;
		}
		// S'il peut tourner, alors on le fait tourner.
		this.NXTMotor.rotate((int)Math.round(degres * this.ratio * this.sensRotation), true);
		// Tant que les conditions pour s'arr�ter ne sont pas remplies, on attend
		// Conditions pour s'arr�ter : 
		// - Le capteur lance un signal, et le sens de rotation est vers le capteur
		// - OU le moteur s'est arr�t� de tourner naturellement
		// - OU le moteur est bloqu� (stalled) et force pour tourner
		Delay.msDelay(25);
		do
		{ } while ((this.capteur.getMesure() == 0 || sens < 0.0f) && (NXTMotor.isMoving()) && !NXTMotor.isStalled());
		this.NXTMotor.stop();
	}


	public void stop()
	{
		NXTMotor.stop();
	}


	private void setSensRotation(int sensRotation) {
		if(sensRotation != SENS_NEGATIF && sensRotation != SENS_POSITIF)
			sensRotation = SENS_POSITIF;
		this.sensRotation = sensRotation;
	}

 }
