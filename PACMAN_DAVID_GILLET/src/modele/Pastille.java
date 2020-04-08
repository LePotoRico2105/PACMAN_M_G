package modele;

/**
 *
 * @author Matthieu David - Guillaume Gillet
 */
public class Pastille {
    private final String type;
    private boolean estMange;
    private final Jeu jeu;
    
    
    public Pastille(Jeu _jeu, String p_type) {
        jeu = _jeu;
        type = p_type;
        estMange = false;
    }
    
    public String getType(){
        return type;
    }
    
    public boolean getEstMange(){
        return estMange;
    }
    
    public void mangerPastille(){
        estMange = true;
    }
}
