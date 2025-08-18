import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private FondoPanel menu; 
    private PanelJuego juego;
    
    private Font fuenteArcade;
    
    public Main() {
        setTitle("Pong");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        try {
            fuenteArcade = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("fuentes/ARCADE_N.TTF")).deriveFont(60f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        crearMenu();

        setContentPane(menu);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void crearMenu() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        menu = new FondoPanel("imagenes/fondo_menu.jpg");
        menu.setPreferredSize(screenSize);
        menu.setLayout(null);

        JPanel tituloPanel = new JPanel();
        tituloPanel.setBounds(10, screenSize.height / 4, screenSize.width, 200);
        tituloPanel.setOpaque(false); 
        tituloPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        JLabel titulo = new JLabel("PONG");
        titulo.setFont(fuenteArcade.deriveFont(180f));
        titulo.setForeground(new Color(79, 0 ,79)); 
        tituloPanel.add(titulo);
        menu.add(tituloPanel);

        JButton botonIniciar = new JButton("INICIAR JUEGO");
        botonIniciar.setBounds((screenSize.width - 140) / 2, (screenSize.height / 2), 140, 40);
        botonIniciar.setBackground(new Color(79, 0 , 79)); 
        botonIniciar.setForeground(Color.white);
        botonIniciar.setFocusPainted(false);
        botonIniciar.setFont(new Font("Arial", Font.PLAIN, 16));
        botonIniciar.setBorder(BorderFactory.createLineBorder(new Color(79, 0 , 79)));

        botonIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJuego();
            }
        });
        menu.add(botonIniciar);
    }

    private void iniciarJuego() {
        juego = new PanelJuego();
        setContentPane(juego);
        revalidate();
        juego.requestFocusInWindow();
    }

    public static void main(String[] args) {
        new Main();
    }

    	public JPanel getMenuPanel() {
    		return menu;
    	}
	}

	class FondoPanel extends JPanel {
		private Image fondo;

		public FondoPanel(String rutaImagen) {
	        try {
	        	java.net.URL imgURL = getClass().getResource("/" + rutaImagen);
	            if (imgURL != null) {
	            	fondo = new ImageIcon(getClass().getResource("/imagenes/fondo_menu.jpg")).getImage();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        if (fondo != null) {
	            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
	        }
	    }
	}