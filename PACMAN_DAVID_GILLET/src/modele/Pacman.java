package modele;

import java.util.Random;
/**
 *
 * @author Matthieu David - Guillaume Gillet
 */
public class Pacman extends Entite {

    private int nbVies;
    private boolean mort;
    private boolean booste;
    
    public Pacman(Jeu _jeu, int _nbVies) {
        super(_jeu);
        this.nbVies = _nbVies;
        this.mort = false;
        this.booste = false;
        this.d = Direction.haut;
    }
    
    public Pacman(Jeu _jeu) {
        super(_jeu);
        this.nbVies = 3;
        this.mort = false;
        this.booste = false;
        this.d = Direction.haut;
    }
    
    public int getNbVies()
    {
        return nbVies;
    }
    
    public void setNbVies(int _nbVies)
    {
        this.nbVies = _nbVies;
    }
    
    public boolean getMort()
    {
        return mort;
    }
    
    public boolean getBoostee()
    {
        return booste;
    }
    
    public void setMort(boolean _mort)
    {
        this.mort = _mort;
    }
    
    public void setBooste(boolean _booste)
    {
        this.booste = _booste;
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
