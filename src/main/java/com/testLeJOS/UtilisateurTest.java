package com.testLeJOS;

import static org.junit.Assert.*;

import org.junit.Test;

import com.RobotArm.business.Utilisateur;

public class UtilisateurTest {

	@Test
	/**
	 * On v�rifie qu'un utilisateur d�finit comme admin l'est bien
	 */
	public void isAdmin() {
		Utilisateur user = new Utilisateur("test","test",true);
		
		assertTrue(user.isAdmin());
	}
	
	@Test
	public void isNotAdmin() {
		Utilisateur user = new Utilisateur("test","test",true);
		
		assertFalse(user.isAdmin());
	}
}
