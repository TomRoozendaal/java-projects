/**
 * 
 * @author Tom van Roozendaal
 *
 */
package prisonersdilemma;
        
import javax.swing.SwingUtilities;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

class PrisonersDilemma implements ChangeListener, ActionListener /* possible extends... */ {
    JFrame frame;
    PlayingField playingField;
    int width;
    int amount;
    JPanel panel; // panel will include two seperate panels: top and bottom
    JPanel topPanel;
    JPanel bottomPanel;
    
    JButton reset;
    JButton goPause;
    JButton step;
    JButton stepBack;
    JSlider slider;
    JSlider tSlider;
    JLabel label;
    JLabel label2;
    JCheckBox check; // option to turn on update rule
    JCheckBox check2; // extra colors option
    
    PrisonersDilemma(int w, int num){
        width = w; // frame becomes width*width in pixels
        amount = num; // grid becomes amount*amount in patches
    }
    
    void buildGUI() {
        frame = new JFrame("PrisonersDilemma");
        frame.getContentPane().setPreferredSize(new Dimension(width, width+100));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        playingField = new PlayingField(width, amount);
        playingField.setSize(width, width);
        
        panel = new JPanel();
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        
        reset = new JButton("Reset");
        reset.addActionListener( this );
        goPause = new JButton("Go");
        goPause.addActionListener( this );
        slider = new JSlider( 0, 30, 10 ); // divide by 10 to get alpha (double)
        slider.addChangeListener( this );
        tSlider = new JSlider( 0, (1000-33), 0 ); // slider to control timer speed
        tSlider.addChangeListener( this );
        label = new JLabel("alpha: 1.0");
        label2 = new JLabel("refresh every 1000 ms");
        check = new JCheckBox("Own score rule");
        check.addActionListener( this );
        check2 = new JCheckBox("Extra colors");
        check2.addActionListener( this );
        
        step = new JButton("Step");
        step.addActionListener( this );
        stepBack = new JButton("Prev");
        stepBack.addActionListener( this );
        
        frame.add( playingField );
        frame.add( panel, BorderLayout.SOUTH  );
        
        panel.add( topPanel);
        panel.add( bottomPanel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        
        topPanel.add( reset );
        topPanel.add( goPause );
        topPanel.add( slider );
        topPanel.add( label );
        topPanel.add( check2 );
        
        bottomPanel.add( stepBack );
        bottomPanel.add( step );
        bottomPanel.add( check );
        bottomPanel.add( tSlider );
        bottomPanel.add( label2 );
        
        // set window in the middle of the screen
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int xWindow = gd.getDisplayMode().getWidth()/2 - width/2;
        int yWindow = gd.getDisplayMode().getHeight()/2 - (width+100)/2;
        frame.setLocation( xWindow, yWindow );
        
        frame.setVisible(true);
        
        SwingUtilities.invokeLater( new Runnable() {
            public void run(){
            }
        } );
    }
    
    // actionPerformed definitions
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("Reset")){
            frame.remove(playingField);
            playingField = new PlayingField( width, amount );
            
            // re-add checkbox values to the new PlayingField
            boolean selected = check.getModel().isSelected();
            playingField.setRule(selected);
            
            boolean selected2 = check2.getModel().isSelected();
            playingField.setExtraColors(selected2); // new PlayingField means timer will pause
            
            // add new PlayingField to the frame again and repaint();
            frame.add( playingField );
            playingField.repaint();
            
            frame.invalidate();
            frame.validate();
            goPause.setText("Go");
            
            // re-add slider values to the new PlayingField
            int speed = (1000 - tSlider.getValue());
            playingField.setSpeed( speed );
            
            double num = slider.getValue();
            playingField.setAlpha(num);
            
        } else if (e.getActionCommand().equals("Go")){
            goPause.setText("Pause");
            playingField.startTimer();
            
        } else if (e.getActionCommand().equals("Pause")){
            goPause.setText("Go");
            playingField.pauseTimer();
            
        } else if (e.getActionCommand().equals("Step")){
            playingField.step();
            
        }  else if (e.getActionCommand().equals("Prev")){
            playingField.stepBack();
            
        } else if (e.getSource().equals(check)){
            boolean selected = check.getModel().isSelected();
            playingField.setRule(selected);
            
        } else if (e.getSource().equals(check2)){
            boolean selected2 = check2.getModel().isSelected();
            playingField.setExtraColors(selected2);
            
        }
    }
    
    // StateChanged definitions
    @Override
    public void stateChanged( ChangeEvent e ) {
        if(e.getSource().equals(slider)) {
            double num = slider.getValue();
            
            playingField.setAlpha(num);
            this.label.setText("alpha: " + Double.toString(playingField.getAlpha()));
        }
        
        if(e.getSource().equals(tSlider)) {
            int speed = (1000 - tSlider.getValue());
            playingField.setSpeed( speed );
            
            if ( playingField.isTimerRunning() ){
                playingField.pauseTimer();
                playingField.startTimer();
                // if timer is running, refresh it so the speed automatically adjusts
            }
            this.label2.setText("refresh every " + Integer.toString( speed ) + " ms");
        }
    }
    
    public static void main( String[] a ) {
        (new PrisonersDilemma(800, 50)).buildGUI();
        // parameters: width(px), amount(patches)
    }
}
