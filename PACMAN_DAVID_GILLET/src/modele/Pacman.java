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
    
    public Direction getDirection() {
        return d;
    }
    
    public void setDirection(Direction _d) {
        d = _d;
    }

    public Pacman(Jeu _jeu, int nbVies) {
        super(_jeu);
        this.nbVies = nbVies;
        this.mort = false;
        this.d = Direction.droite;
    }
    
    public Pacman(Jeu _jeu) {
        super(_jeu);
        this.nbVies = 3;
        this.mort = false;
        this.d = Direction.haut;
    }
    
    @Override
    public void choixDirection() {
        
    }
}
