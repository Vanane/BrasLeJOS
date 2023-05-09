package com.RobotArm.persistance;

import com.RobotArm.business.Gamme;
import com.RobotArm.business.Operation;
import com.RobotArm.business.Tache;
import com.RobotArm.business.Utilisateur;
import com.RobotArm.exception.GammeNotFoundException;
import com.RobotArm.exception.UnableToReadGammesException;
import com.RobotArm.exception.UnableToReadUsersException;
import com.RobotArm.exception.UserNotFoundException;
import com.RobotArm.interfaces.IPersistance;
import com.RobotArm.jsonAdapters.JsonAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonPersistance implements IPersistance {
    private final JsonAdapter<Gamme> gammeAdapter;
    private final FileReader gammeReader;
    private final FileWriter gammeWriter;
    private final String fichierGammes;
    private final JsonAdapter<Utilisateur> userAdapter;
    private final FileReader userReader;
    private final FileWriter userWriter;

    private ArrayList<Gamme> gammes;

    public JsonPersistance(String fichierGammes, String fichierUsers) throws IOException {
        this.fichierGammes = fichierGammes;
        this.gammeAdapter = new JsonAdapter<>();
        this.gammeWriter = new FileWriter(fichierGammes);
        this.gammeReader = new FileReader(fichierGammes);

        this.userAdapter = new JsonAdapter<>();
        this.userWriter = new FileWriter(fichierUsers);
        this.userReader = new FileReader(fichierUsers);
    }

    @Override
    public void creerGamme(Gamme gamme) {
        try {
            this.gammes = getGammes();
        	if (gammes == null) {
                gammes = new ArrayList<>();
            } else {
                for (Gamme g:gammes) {
                    System.out.println(g.getId());
                }
            }
            gammes.add(gamme);

            this.gammeAdapter.serializeAll(gammes, this.gammeWriter);
            this.gammeWriter.flush();
        } catch (IOException e) {
            System.out.println("Impossible de stocker la gamme");
        }
    }

    @Override
    public void modifierGamme(Gamme paramGamme) {

    }

    @Override
    public void supprimerGamme(String paramString) {
        Gamme gamme;
        try {
            gamme = this.findGamme(paramString);
            System.out.println(gamme.getId());

            this.gammes = getGammes();
            if (gammes == null) {
                gammes = new ArrayList<>();
            }
            gammes.remove(gamme);
            System.out.println(gammes);

            PrintWriter writer = new PrintWriter(fichierGammes);
            writer.print("");
            writer.close();
            this.gammeAdapter.serializeAll(gammes, this.gammeWriter);
            this.gammeWriter.flush();
        } catch (IOException e) {
            System.out.println("Impossible de stocker la gamme");
        } catch (GammeNotFoundException e) {
            e.getMessage();
        }
    }

    @Override
    public ArrayList<Gamme> getGammes() throws UnableToReadGammesException {
        ArrayList<Gamme> gammes = null;
        try {
            gammes = this.gammeAdapter.deserialize(this.gammeReader, new TypeToken<List<Gamme>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (gammes == null) {
            gammes = new ArrayList<>();
        }
        return gammes;
    }

    @Override
    public Gamme findGamme(String id) throws GammeNotFoundException {
        try {
            ArrayList<Gamme> gammes = getGammes();

            for (Gamme gamme : gammes) {
                if (gamme.getId().equals(id))
                    return gamme;
            }
        } catch (UnableToReadGammesException e) {
            e.printStackTrace();
        }
        throw new GammeNotFoundException();
    }

    @Override
    public Gamme recupererGammeDefaut() {
        Gamme g = new Gamme("1", "Gamme defaut");

        Operation ope = new Operation("1", "Gamme défaut");

        Tache t1 = new Tache("1", "Tourner à gauche", -90, 'B');
        Tache t2 = new Tache("2", "Tourner à droite", 90, 'B');
        Tache t3 = new Tache("3", "Ouvre la pince", -180, 'A');
        Tache t4 = new Tache("4", "Ferme la pince", 180, 'A');
        Tache t5 = new Tache("5", "Baisse le bras", -120, 'C');
        Tache t6 = new Tache("6", "Monter le bras", 120, 'C');
        Tache t7 = new Tache("7", "Attendre 2 secondes", 2000);

        // Saisir un objet
        ope.AjouterTache(t3);
        ope.AjouterTache(t5);
        ope.AjouterTache(t4);
        ope.AjouterTache(t6);
        ope.AjouterTache(t1);
        ope.AjouterTache(t5);
        ope.AjouterTache(t3);
        ope.AjouterTache(t6);
        ope.AjouterTache(t4);
        ope.AjouterTache(t7);
        ope.AjouterTache(t2);

        g.AjouterOperation(ope);
        return g;
    }


    @Override
    public void sauverLog(String paramString) {

    }

    @Override
    public ArrayList<String> recupererLogs() {
        return null;
    }

    @Override
    public String getConfig(String paramString) {
        return null;
    }

    @Override
    public void createUser(String login, String pwd) {
        String jsonContent = userAdapter.serialize(new Utilisateur(login, pwd, false));
    }

    @Override
    public Utilisateur findUser(String login, String pwd) throws UserNotFoundException {
        try {
            ArrayList<Utilisateur> users = getUsers();

            for (Utilisateur user : users) {
                if (user.getLogin().equals(login) && user.getPwd().equals(pwd))
                    return user;
            }
        } catch (UnableToReadUsersException e) {
            e.printStackTrace();
        }
        throw new UserNotFoundException();
    }

    @Override
    public ArrayList<Utilisateur> getUsers() throws UnableToReadUsersException {
        return new ArrayList<>();
    }

    @Override
    public void supprimerCompte(String login) {

    }
}
