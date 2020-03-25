package VueControleur;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import modele.Direction;
import modele.Fantome;
import modele.Jeu;
import modele.Pacman;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 * @author freder
 */
public class VueControleurPacMan extends JFrame implements Observer {

    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    private ImageIcon icone; // icones affichées dans la grille
    private ImageIcon icoPacman;
    private ImageIcon icoBleuH;
    private ImageIcon icoBleuD;
    private ImageIcon icoBleuB;
    private ImageIcon icoBleuG;
    private ImageIcon icoRoseH;
    private ImageIcon icoRoseD;
    private ImageIcon icoRoseB;
    private ImageIcon icoRoseG;
    private ImageIcon icoRougeH;
    private ImageIcon icoRougeD;
    private ImageIcon icoRougeB;
    private ImageIcon icoRougeG;
    private ImageIcon icoOrangeH;
    private ImageIcon icoOrangeD;
    private ImageIcon icoOrangeB;
    private ImageIcon icoOrangeG;
    private ImageIcon icoPastilleS;
    private ImageIcon icoPastilleL;
    private ImageIcon icoLogoTransparent;
    private ImageIcon icoLogoCouleur;
    private ImageIcon icoMurDroit;
    private ImageIcon icoMurFin;
    private ImageIcon icoMurAngle;
    private ImageIcon icoCouloir;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associé à une icône, suivant ce qui est présent dans la partie modèle)


    public VueControleurPacMan(int _sizeX, int _sizeY) {

        sizeX = _sizeX;
        sizeY = _sizeY;
        

        chargerLesIcones();
        placerLesComposantsGraphiques();

        ajouterEcouteurClavier();
    }

    private void ajouterEcouteurClavier() {

        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                
                switch(e.getKeyCode()) {  // on écoute les flèches de direction du clavier
                    case KeyEvent.VK_LEFT : jeu.getPacman().setDirection(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : jeu.getPacman().setDirection(Direction.droite); break;
                    case KeyEvent.VK_DOWN : jeu.getPacman().setDirection(Direction.bas); break;
                    case KeyEvent.VK_UP : jeu.getPacman().setDirection(Direction.haut); break;
                }
                
            }

        });

    }

    public void setJeu(Jeu _jeu) {
        jeu = _jeu;
    }

    private void chargerLesIcones() {
        // icones affichées dans la grille
        icone = chargerIcone("Images/icone.png");
        icoPacman = chargerIcone("Images/pacman.png");
        icoBleuH = chargerIcone("Images/bleuB.png");
        icoBleuD = chargerIcone("Images/bleuD.png");
        icoBleuB = chargerIcone("Images/bleuB.png");
        icoBleuG = chargerIcone("Images/bleuG.png");
        icoRoseH = chargerIcone("Images/roseH.png");
        icoRoseD = chargerIcone("Images/roseD.png");
        icoRoseB = chargerIcone("Images/roseB.png");
        icoRoseG = chargerIcone("Images/roseG.png");
        icoRougeH = chargerIcone("Images/rougeH.png");
        icoRougeD = chargerIcone("Images/rougeD.png");
        icoRougeB = chargerIcone("Images/rougeB.png");
        icoRougeG = chargerIcone("Images/rougeG.png");
        icoOrangeH = chargerIcone("Images/orangeH.png");
        icoOrangeD = chargerIcone("Images/orangeD.png");
        icoOrangeB = chargerIcone("Images/orangeB.png");
        icoOrangeG = chargerIcone("Images/orangeG.png");
        icoPastilleS = chargerIcone("Images/pastilleS.png");
        icoPastilleL = chargerIcone("Images/pastilleL.png");
        icoLogoTransparent = chargerIcone("Images/logoTransparent.png");
        icoLogoCouleur = chargerIcone("Images/logoCouleur.png");
        icoCouloir = chargerIcone("Images/couloir.png");
        icoMurDroit = chargerIcone("Images/murDroit.png");
        icoMurFin = chargerIcone("Images/murFin.png");
        icoMurAngle = chargerIcone("Images/murAngle.png");
    }

    private ImageIcon chargerIcone(String urlIcone) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurPacMan.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {
        Color color = new Color(255,255,255,255);
        setTitle("PacMan");
        setSize(sizeX*40, sizeY*40);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeX, sizeY)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille
        grilleJLabels.setBackground(color);
        tabJLabel = new JLabel[sizeX][sizeY];
        

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                JLabel jlab = new JLabel();
                tabJLabel[y][x] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
                
            }

        }

        add(grilleJLabels);

    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (jeu.getGrille()[x][y] instanceof Pacman) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue     
                    tabJLabel[x][y].setIcon(icoPacman);   
                } else if (jeu.getGrille()[x][y] instanceof Fantome) {
                    Fantome f = (Fantome) jeu.getGrille()[x][y];
                    if (null != f.getColor()) switch (f.getColor()) {
                        case "bleu":
                            tabJLabel[x][y].setIcon(icoBleuB);
                            break;
                        case "rose":
                            tabJLabel[x][y].setIcon(icoRoseB);
                            break;
                        case "rouge":
                            tabJLabel[x][y].setIcon(icoRougeB);
                            break;
                        case "orange":
                            tabJLabel[x][y].setIcon(icoOrangeB);
                            break;
                        default:
                            break;
                    }
                } else {
                        tabJLabel[x][y].setIcon(icoCouloir);
                }
                
                

            }
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        
        
        mettreAJourAffichage();
        
        
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
       */
        
    }

}
