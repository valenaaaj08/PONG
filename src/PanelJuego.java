import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.*;
import java.awt.FontMetrics;
import javax.swing.Timer;

import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class PanelJuego extends JPanel implements ActionListener, KeyListener {

    private double bolaX, bolaY;
    private double bolaDX, bolaDY;
    private final int bolaRadio = 13;

    private int paletaXIzquierda;
    private int paletaXDerecha;
    private int alturaPanel;
    private int anchoPanel;
    private final int alturaPaleta = 90;

    private Jugador jugadorIzquierda;
    private Jugador jugadorDerecha;

    private Timer timer;
    private boolean jugando = false;

    private int puntajeIzquierda = 0;
    private int puntajeDerecha = 0;

    private Random random = new Random();
    private boolean pelotaSaleHaciaIzquierda;

    private boolean wPresionado = false, sPresionado = false;
    private boolean upPresionado = false, downPresionado = false;
    private final double velocidadPaleta = 3.5;
    
    private Font fuenteArcade;
    private Image fondo;

    public PanelJuego() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        anchoPanel = screenSize.width;
        alturaPanel = screenSize.height;

        setPreferredSize(new Dimension(anchoPanel, alturaPanel));
        setBackground(new Color(0xF5F5F5));

        paletaXIzquierda = anchoPanel / 20;
        paletaXDerecha = anchoPanel - anchoPanel / 20 - 10;

        jugadorIzquierda = new Jugador(alturaPanel);
        jugadorDerecha = new Jugador(alturaPanel);

        addKeyListener(this);
        setFocusable(true);
        
        try {
            fuenteArcade = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("fuentes/ARCADE_N.TTF")).deriveFont(60f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        fondo = new ImageIcon(getClass().getResource("/imagenes/fondo_juego.png")).getImage();
        
        resetearPelota();

        timer = new Timer(10, this);
        timer.start();
    }

    private void resetearPelota() {
        bolaX = (anchoPanel / 2) - (bolaRadio / 2);
        bolaY = (alturaPanel / 2) - (bolaRadio / 2);
        bolaDX = 0;
        bolaDY = 0;
    }

    private void iniciarPelota() {
        double velocidadInicial = 5;
        bolaDX = pelotaSaleHaciaIzquierda ? -velocidadInicial : velocidadInicial;
        bolaDY = random.nextBoolean() ? velocidadInicial : -velocidadInicial;
        jugando = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
        
        g.setColor(new Color(0xAAAAAA));
        for (int y = 0; y < alturaPanel; y += 20) {
            g.fillRect(anchoPanel / 2 - 0, y, 1, 10);
        }

        g.setColor(new Color(79, 0, 79));
        g.setFont(fuenteArcade);
        g.drawString(String.valueOf(puntajeIzquierda), anchoPanel / 4, 80);
        g.drawString(String.valueOf(puntajeDerecha), anchoPanel * 3 / 4, 80);

        g.setColor(new Color(130, 7, 219));
        g.fillRect(paletaXIzquierda, jugadorIzquierda.getY(), 10, alturaPaleta);
        g.setColor(new Color(130, 7, 219));
        g.fillRect(paletaXDerecha, jugadorDerecha.getY(), 10, alturaPaleta);

        g.setColor(new Color (255, 255, 255));
        g.fillOval((int) bolaX, (int) bolaY, bolaRadio, bolaRadio);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setFont(fuenteArcade);  
        g2d.setColor(new Color(150, 150, 150));         
        FontMetrics fm = g2d.getFontMetrics();
        String logo = "P O N G";
        int logoX = (anchoPanel - fm.stringWidth(logo)) / 2;
        int logoY = (alturaPanel - fm.getHeight()) / 2 + fm.getAscent() + 10;
        g2d.drawString(logo, logoX, logoY);
        g2d.dispose();

        if (!jugando) {
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            fm = g.getFontMetrics();
            String mensaje = "Presiona ESPACIO para comenzar";
            int mensajeX = (anchoPanel - fm.stringWidth(mensaje)) / 2;
            int mensajeY = alturaPanel / 2 + fm.getHeight() + 80;  
            g.setColor(new Color(200, 200, 200));
            g.drawString(mensaje, mensajeX, mensajeY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (wPresionado) jugadorIzquierda.mover(-velocidadPaleta);
        if (sPresionado) jugadorIzquierda.mover(velocidadPaleta);
        if (upPresionado) jugadorDerecha.mover(-velocidadPaleta);
        if (downPresionado) jugadorDerecha.mover(velocidadPaleta);

        if (jugando) {
            bolaX += bolaDX;
            bolaY += bolaDY;

            if (bolaY <= 0 || bolaY >= alturaPanel - 30) {
                bolaDY = -bolaDY;
            }

            if (bolaDX < 0) {
                double nextX = bolaX + bolaDX;
                if (nextX <= paletaXIzquierda + 10) {
                    if (bolaY + bolaRadio >= jugadorIzquierda.getY() && bolaY <= jugadorIzquierda.getY() + alturaPaleta) {
                        bolaX = paletaXIzquierda + 10;
                        bolaDX = -bolaDX;
                        aumentarVelocidad();
                        ajustarRebote(jugadorIzquierda);
                    }
                }
            }

            if (bolaDX > 0) {
                double nextX = bolaX + bolaDX + bolaRadio;
                if (nextX >= paletaXDerecha) {
                    if (bolaY + bolaRadio >= jugadorDerecha.getY() && bolaY <= jugadorDerecha.getY() + alturaPaleta) {
                        bolaX = paletaXDerecha - bolaRadio;
                        bolaDX = -bolaDX;
                        aumentarVelocidad();
                        ajustarRebote(jugadorDerecha);
                    }
                }
            }

            if (bolaX < 0) {
                puntajeDerecha++;
                pelotaSaleHaciaIzquierda = true;
                jugando = false;
                resetearPelota();
                verificarGanador();
            }

            if (bolaX > anchoPanel - bolaRadio) {
                puntajeIzquierda++;
                pelotaSaleHaciaIzquierda = false;
                jugando = false;
                resetearPelota();
                verificarGanador();
            }
        }
        repaint();
    }

    private void aumentarVelocidad() {
        double factorAumento = 1.05;
        bolaDX *= factorAumento;
        bolaDY *= factorAumento;
    }

    private void ajustarRebote(Jugador jugador) {
        int centroPaleta = jugador.getY() + alturaPaleta / 2;
        int centroBola = (int) bolaY + bolaRadio / 2;
        double velocidadActual = Math.sqrt(bolaDX * bolaDX + bolaDY * bolaDY);
        double direccion = (centroBola - centroPaleta) / 15.0;

        bolaDY = direccion;

        double signo = bolaDX > 0 ? 1 : -1;
        bolaDX = signo * Math.sqrt(velocidadActual * velocidadActual - bolaDY * bolaDY);
    }

    private void verificarGanador() {
        if (puntajeIzquierda >= 3 || puntajeDerecha >= 3) {
            String ganador = (puntajeIzquierda >= 3) ? "Jugador Izquierdo" : "Jugador Derecho";

            int opcion = JOptionPane.showOptionDialog(
                    this,
                    ganador + " ha ganado!",
                    "Fin de la partida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Revancha", "Salir"},
                    "Revancha"
            );

            if (opcion == JOptionPane.YES_OPTION) {
                puntajeIzquierda = 0;
                puntajeDerecha = 0;
                resetearPelota();
                jugando = false;
            } else {
                System.exit(0);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) wPresionado = true;
        if (key == KeyEvent.VK_S) sPresionado = true;
        if (key == KeyEvent.VK_UP) upPresionado = true;
        if (key == KeyEvent.VK_DOWN) downPresionado = true;

        if (!jugando && key == KeyEvent.VK_SPACE) {
            iniciarPelota();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) wPresionado = false;
        if (key == KeyEvent.VK_S) sPresionado = false;
        if (key == KeyEvent.VK_UP) upPresionado = false;
        if (key == KeyEvent.VK_DOWN) downPresionado = false;
    }

    @Override public void keyTyped(KeyEvent e) {}

    class Jugador {
        private double y;
        private int alturaPanel;

        public Jugador(int alturaPanel) {
            this.alturaPanel = alturaPanel;
            y = alturaPanel / 2 - alturaPaleta / 2;
        }

        public void mover(double dy) {
            y += dy;
            if (y < 0) y = 0;
            if (y > alturaPanel - alturaPaleta) y = alturaPanel - alturaPaleta;
        }

        public int getY() {
            return (int) y;
        }
    }
}
