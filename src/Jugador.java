import java.awt.event.KeyEvent;

public class Jugador {
    private int y;
    private final int velocidad = 20;
    private final int alturaPanel;
    private final int alturaPaleta = 60;

    private final int teclaArriba;
    private final int teclaAbajo;

    public Jugador(int alturaPanel, int teclaArriba, int teclaAbajo) {
        this.alturaPanel = alturaPanel;
        this.teclaArriba = teclaArriba;
        this.teclaAbajo = teclaAbajo;
        this.y = alturaPanel / 2 - alturaPaleta / 2;
    }

    public int posY() {
        return y;
    }

    public void teclaPresionada(int keyCode) {
        if (keyCode == teclaArriba) {
            moverArriba();
        } else if (keyCode == teclaAbajo) {
            moverAbajo();
        }
    }

    private void moverArriba() {
        if (y - velocidad >= 0) {
            y -= velocidad;
        }
    }

    private void moverAbajo() {
        if (y + velocidad + alturaPaleta <= alturaPanel) {
            y += velocidad;
        }
    }
}