 package com.RobotArm.business;

 import com.RobotArm.interfaces.IExecuteur;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 import java.util.concurrent.Future;

 /**
  * Service d'ex�cution des gammes
  * @author Alvin
  *
  */
 public class ThreadGamme
 {
	public IExecuteur executeur;
	private ExecutorService execService;
	private Future execThread;
	private boolean stopper;
	private Gamme gammeEnExec;
	
	/**
	 * Constructeur de la classe
	 * @param executeur
	 */
	public ThreadGamme(IExecuteur executeur) {
		this.executeur = executeur;
		this.execService = Executors.newSingleThreadExecutor();
		stopper = false;
	}

	/**
	 * Ex�cute une gamme en parall�le du reste du programme
	 * @param g
	 */
	public void executer(final Gamme g) {
		gammeEnExec = g;
		System.out.println(String.format("D�marrage de la gamme %s", gammeEnExec.id));
		stopper = false;
		this.execThread = this.execService.submit(new Runnable()
		{
			public void run()
			{
				// Pour chaque t�che de la gamme, on lui demande d'ex�cuter son programme.
				try {
					for(Operation o:g.getListeOperations())
					{
						for(Tache t:o.getListeTaches())
						{
							// La variable bool�enne stopper permet d'interrompre le thread
							// proprement et sans perte de m�moire ou de contr�leur du thread
							if(stopper == true)
								throw new InterruptedException();
							t.executer();							
						}						
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				stopper = true;
				ThreadGamme.this.notifierObservateur();				
			}
		});
	}
	
	/**
	 * Appel�e lors de la fin d'une gamme
	 */
	private void notifierObservateur() {
		System.out.println("Gamme terminee");
		this.executeur.notifierFinGamme();
	}

	/**
	 * Retourne True si une gamme est en cours, False sinon
	 * @return
	 */
	public Boolean gammeEnCours() {
		return execThread == null ? false : ! this.execThread.isDone();
	}
	
	/**
	 * Retourne la gamme actuellement ex�cut�e
	 * @return
	 */
	public Gamme getGammeEnExecution()
	{
		return gammeEnExec;
	}
	
	/**
	 * D�clenche le mode panne, ce qui arr�te le thread et l'ex�cution de la gamme
	 */
	public void panne()
	{
		stopper = true;
		Moteur.stopAll();		
	}
	
	/**
	 * Arr�te le thread complet en vue d'arr�ter le robot
	 */
	public void stop()
	{
		panne();
		if(this.execThread != null)
			this.execThread.cancel(true);
		this.execService.shutdownNow();
	}
}
