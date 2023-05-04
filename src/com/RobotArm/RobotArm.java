package com.RobotArm;

import com.RobotArm.business.CapteurContact;
import com.RobotArm.business.CapteurCouleur;
import com.RobotArm.business.CapteurPince;
import com.RobotArm.business.Moteur;
import com.RobotArm.controller.Controleur;
import com.RobotArm.interfaces.IPersistance;
import com.RobotArm.interfaces.IPilotage;
import com.RobotArm.interfaces.IPilote;
import com.RobotArm.persistance.DummyPersistance;
import com.RobotArm.pilotage.WifiListener;

import java.io.IOException;
import java.sql.SQLException;


public class RobotArm {
    private static IPersistance persistance;
    private static IPilotage pilotage;
    private static Controleur controleur;

    public static void main(String[] args) {
        System.out.println("Debut du programme");

        try {
            // Création et instanciation du contrôleur et des différents modules
            InitHardware();
            persistance = (IPersistance) new DummyPersistance();
            pilotage = (IPilotage) new WifiListener();
            controleur = new Controleur(persistance, pilotage);
            pilotage.ajoutListener((IPilote) controleur);

            controleur.demarrer();
        } catch (IOException e) {
            System.out.println("Une erreur bloquane est survenue, impossible de demarrer !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Une erreur SQL bloquante est survenue, impossible de demarrer !");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Une erreur inconnue est survenue, impossible de demarrer !");
            e.printStackTrace();
        }

        pilotage.fermerConnexion();

        pilotage = null;
        persistance = null;
        controleur = null;
        System.gc();
        System.out.println("Fin du programme");
    }


    private static void InitHardware() {
        // Configuration des moteurs et des capteurs.
        //TODO Les valeurs sont écrites en dur, et devraient être plutôt écrites dans un fichier externe.
        float vitesse = 120;
        int acceleration = 360;
        CapteurContact.initCapteur('1');
        CapteurCouleur.initCapteur('3');
        CapteurPince.initCapteur();

        Moteur.initMoteur('A', 2, CapteurPince.getInstance(), vitesse, acceleration, Moteur.SENS_POSITIF); // Pince
        Moteur.initMoteur('B', 5, CapteurContact.getInstance(), vitesse, acceleration, Moteur.SENS_POSITIF); // Rotation
        Moteur.initMoteur('C', 3, CapteurCouleur.getInstance(), vitesse, acceleration, Moteur.SENS_NEGATIF); // Levée
    }
}
