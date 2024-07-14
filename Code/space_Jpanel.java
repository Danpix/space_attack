package Code;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class space_Jpanel extends JPanel implements KeyListener {
	private List<Thread> disparoThreads;
	private List<Disparo> disparos;
	private Image imagenFondo;
	private Image naveImage;
	private Image explosion;
	private Enemigo enemigo;
	private int naveX;
	private int naveY;
	private int enemigoX;
	private int enemigoY;
	private int vidas=3;
	private int puntaje = 0;
	private Random random = new Random();

	
	public space_Jpanel(int ventanaAncho, int ventanaAlto) {
		
		enemigoY = -50;
		enemigoX = random.nextInt(ventanaAncho - 70);
		imagenFondo = new ImageIcon(getClass().getResource("backgroud.jpg")).getImage();
		naveImage = new ImageIcon(getClass().getResource("ship.png")).getImage();
		explosion = new ImageIcon(getClass().getResource("explosion.png")).getImage();
		naveX = (ventanaAncho / 2) - 35;
		naveY = ventanaAlto - 120;

		setLayout(new BorderLayout());
		addKeyListener(this);
		setFocusable(true);
		disparoThreads = new ArrayList<>();
		disparos = new ArrayList<>();
		
		
		enemigo = new Enemigo(enemigoX, enemigoY);//Iniciamos el hilo del enemigo
		Thread enemigoThread = new Thread(enemigo);
		enemigoThread.start();

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);// Dibujando
		if(vidas>0){
			g.drawImage(naveImage, naveX, naveY, 70, 70, this);
			manejoColisiones(g);
		
			Font font = new Font("Arial", Font.BOLD, 15); // Aquí especificamos el tipo de fuente, negrita y tamaño
			g.setFont(font);
			g.setColor(Color.GREEN);
			g.drawString("Puntaje = " + puntaje, 10, 20);
			g.drawString("Vidas restantes "+ vidas, 10 , getHeight()-15);
		}else{
			g.setColor(Color.RED);
			Font font = new Font("Arial", Font.BOLD, 30); 
			g.setFont(font);
			g.drawString("Perdiste tu puntuacion fue " + puntaje, getWidth()/4, getHeight()/2);
			JButton reiniciarButton = new JButton("Reiniciar");
            reiniciarButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   reinicioJuego();
                   }
            });
            reiniciarButton.setBounds(getWidth() / 2 - 50, getHeight() / 2 + 20, 100, 30);
            add(reiniciarButton);
		}
			
	}
	private void reinicioJuego(){
		vidas=3;
        puntaje=0;
        enemigo = new Enemigo(random.nextInt(getWidth() - 70), 0);
        Thread enemigoThread = new Thread(enemigo);
        enemigoThread.start();
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                remove(component);
            }
        }
        requestFocusInWindow();
        setFocusable(true);
        repaint();

	}
	private void manejoColisiones(Graphics g) {
		
		List<Disparo> copiaDisparos = new ArrayList<>(disparos);
		for (Disparo disparo : copiaDisparos) {
			disparo.dibujar(g);
			if (enemigo != null && disparo.getY() <= enemigo.getY() + enemigo.anchoEnemy
					&& disparo.getY() + disparo.anchoDisparo >= enemigo.getY()
					&& disparo.getX() <= enemigo.getX() + enemigo.anchoEnemy
					&& disparo.getX() + disparo.anchoDisparo >= enemigo.getX()) {
				g.drawImage(explosion, enemigo.getX(), enemigo.getY(), 70, 70, this);
				
				
				actualizarDisparos();
				enemigo.setY(0);
				enemigo.setX(random.nextInt(getWidth() - 70));
				puntaje += 100;
				
				
			}
			
			
			
		}
		if (vidas!=0&&enemigo != null) {
			enemigo.dibujar(g);
			
		}
	}

	private void actualizarDisparos() {
		Iterator<Disparo> iterator = disparos.iterator();
		while (iterator.hasNext()) {
			Disparo disparo = iterator.next();

			if (disparo.getY() <= 0) {
				iterator.remove();
				repaint();
			}
			if (enemigo != null && disparo.getY() <= enemigo.getY() + enemigo.anchoEnemy
	                && disparo.getY() + disparo.anchoDisparo >= enemigo.getY()
	                && disparo.getX() <= enemigo.getX() + enemigo.anchoEnemy
	                && disparo.getX() + disparo.anchoDisparo >= enemigo.getX()) {
	            iterator.remove();
			}

		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
			if (naveX - 10 <= 0) {
				break;
			}
			naveX -= 10;
			break;
		case KeyEvent.VK_RIGHT:
			if (naveX + 80 >= getWidth()) {
				break;
			}
			naveX += 10;

			break;
		case KeyEvent.VK_UP:
			if (naveY <= 0) {
				break;
			}
			naveY -= 10;

			break;
		case KeyEvent.VK_DOWN:
			if (naveY + 80 > getHeight()) {
				break;
			}
			naveY += 10;

			break;
		case KeyEvent.VK_SPACE:
			int disparoX = naveX - 20;

			Disparo nuevoDisparo = new Disparo(disparoX, naveY, 70, 70);
			disparos.add(nuevoDisparo);
			for (Disparo disparo : disparos) {
				Thread disparoThread = new Thread(disparo);
				disparoThreads.add(disparoThread);
				disparoThread.start();
				
			}
			break;
		}
		repaint();
		actualizarDisparos();
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private class Disparo  implements Runnable {

		private int x = naveX;
		private int y = naveY;
		private int velocidad = 10;
		private int anchoDisparo = 40;

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public Disparo(int x, int y, int anchoNave, int altoNave) {
			this.x = x + anchoNave / 2;
			this.y = y;
			
		}

		public void avanzar() {
			y -= velocidad;

		}

		public void dibujar(Graphics g) {
			g.drawImage(new ImageIcon(getClass().getResource("disparo.png")).getImage(), x, y, anchoDisparo,
					anchoDisparo, null);
		}

		@Override
		public void run() {
			
			while (y >= 0) {
				avanzar();
				if(y<=0){
					actualizarDisparos();
				}
				try {	
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

	public class Enemigo implements Runnable {

		private int enemyX;
		private int enemyY = 0;
		private int anchoEnemy = 70;
		private int velocidad = 10;
		
		public int getX() {
			return enemyX;
		}

		public int getY() {
			return enemyY;
		}

		public void setX(int x) {
			this.enemyX = x;
		}

		public void setY(int y) {
			this.enemyY = y;
		}

		public Enemigo(int enemyX, int enemyY) {
			this.enemyX = enemyX;
			this.enemyY = enemyY;
			
		}

		public void avanzar() {
			enemyY += velocidad;
			repaint();
		}

		public void dibujar(Graphics g) {
			g.drawImage(new ImageIcon(getClass().getResource("enemy.png")).getImage(), enemyX, enemyY, 50, anchoEnemy,
					null);

		}

		@Override
		public void run() {
			while (enemyY <= getHeight()&& vidas>0) {
				avanzar();
				
				if (enemyY >= getHeight()) {		
						vidas--;
						enemyY = 0;
						enemyX = random.nextInt(850 - anchoEnemy);
						puntaje-=100;
					
				}
				try {
					
					Thread.sleep(100);
					repaint();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
