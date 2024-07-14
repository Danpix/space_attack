package Code;


import javax.swing.*;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.sound.sampled.*;

public class Space_Jframe extends JFrame {
    private JPanel contentPane;
    private Clip backgroundMusic;
    private final JFXPanel jfxpanel=new JFXPanel();
    
    public Space_Jframe() {
    	
    	createScene();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920,1080);
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);
        contentPane.add(jfxpanel, BorderLayout.CENTER);
        IniciarScene();
        
        

        // Añadir el KeyListener al contentPane
        contentPane.setFocusable(true);
        contentPane.requestFocusInWindow();
        contentPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    DetenerScene();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        
        
    }
    
    private void createScene(){
    	  Platform.runLater(new Runnable() {
              @Override
              public void run() {
                  try {
                      File file = new File("C:/Users/Usuario/Desktop/Graficacion/Programas/Space_Attack/Code/IntroJuego.mp4"); // Carga del archivo
                      MediaPlayer oracleVid = new MediaPlayer( // Se crea el archivo media el cual control total del video que se mostrará
                              new Media(file.toURI().toString())
                      );
                      //oracleVid.setVolume(0.03);
                      oracleVid.setCycleCount(MediaPlayer.INDEFINITE);

                      MediaView mediaView = new MediaView(oracleVid);
                      jfxpanel.setScene(new Scene(new Group(mediaView))); // En este caso el grupo solo es un elemento pero se podrían agregar más para que se muestren simultáneamente

                      oracleVid.setOnReady(new Runnable() {
                          @Override
                          public void run() {
                              IniciarScene();
                          }
                      });
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }
          });
			}
    private void IniciarScene() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mediaPlayer = ((MediaView) ((Group) jfxpanel.getScene().getRoot()).getChildren().get(0)).getMediaPlayer();
                if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                    mediaPlayer.play();
                }
                
            }
        });
        
    }
    private void DetenerScene() {
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	MediaPlayer mediaPlayer = ((MediaView) ((Group) jfxpanel.getScene().getRoot()).getChildren().get(0)).getMediaPlayer();
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.stop();
                }
                contentPane.remove(jfxpanel);
                
                space_Jpanel gamePanel= new space_Jpanel(getWidth(), getHeight());
                contentPane.add(gamePanel, BorderLayout.CENTER);
                contentPane.revalidate();
                contentPane.repaint();
                gamePanel.requestFocusInWindow();
                
              
            }
        });
    }
    
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Space_Jframe frame = new Space_Jframe();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
