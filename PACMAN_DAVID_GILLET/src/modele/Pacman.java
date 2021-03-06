package modele;
/**
 *
 * @author Matthieu David - Guillaume Gillet
 */
public class Pacman extends Entite {

    private int nbVies;
    private boolean booste;
    
    public Pacman(Jeu _jeu, int _nbVies) {
        super(_jeu);
        this.nbVies = _nbVies;
        this.mort = false;
        this.booste = false;
        this.d = Direction.bas;
    }
    
    public Pacman(Jeu _jeu) {
        super(_jeu);
        this.nbVies = 3;
        this.mort = false;
        this.booste = false;
        this.d = Direction.bas;
    }
    
    public int getNbVies()
    {
        return nbVies;
    }
    
    public void setNbVies(int _nbVies)
    {
        this.nbVies = _nbVies;
    } 
    
    public boolean getBooste()
    {
        return booste;
    }
    
    public void setBooste(boolean _booste)
    {
        this.booste = _booste;
    }
    
    @Override
    public Direction getDirection() {
        return d;
    }
    
    public void setDirection(Direction _d) {
        d = _d;
    }
    
    @Override
    public void choixDirection() {
        
    }
}
