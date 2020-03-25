package modele;

import java.util.Random;
/**
 *
 * @author Matthieu David - Guillaume Gillet
 */
public class Pacman extends Entite {

    private int nbVies;
    private boolean mort;
    
    public int getNbVies()
    {
        return nbVies;
    }
    
    public void setNbVies(int nbVies)
    {
        this.nbVies = nbVies;
    }
    
    public boolean getMort()
    {
        return mort;
    }
    
    public void setMort(boolean mort)
    {
        this.mort = mort;
    }
    
    public void setDirection(Direction _d) {
        d = _d;
    }

    public Pacman(Jeu jeu, int nbVies, boolean mort) {
        super(jeu);
        this.nbVies = nbVies;
        this.mort = mort;
        d = Direction.droite;
    }
    
    public Pacman(Jeu _jeu) {
        super(_jeu);
        d = Direction.droite;
    }
    
    @Override
    public void choixDirection() {
        
    }
}
