import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

//awt Menu display
class Menu extends JFrame {
    public static ScrollPane window;
    Color menuColor = new Color(255,255,255);
    static int rDir, gDir, bDir;    //directions to slide Colors (R,G,B)
    private Panel innerFrame;
    private Panel pad;

    Menu(List names){
        window = new ScrollPane(ScrollPane.SCROLLBARS_NEVER);
        window.setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width, GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);
        innerFrame = new Panel();
        innerFrame.setLayout(new GridBagLayout());
        pad = new Panel();
        pad.setLayout(new GridBagLayout());
        GridBagConstraints padCons = new GridBagConstraints();
        padCons.insets = new Insets(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height/2-30, 0, GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height/2-30, 0);
        pad.add(innerFrame, padCons);
        window.add(pad);

        //spawn buttons with rom names
        for(int i=0; i<names.size(); i++){
            JButton b = new JButton((String) names.get(i));
            JPanel bPanel = new JPanel();
            int i_ = i;
            b.addActionListener(e -> {
                try {
                    // this.window.add(new JLabel(new ImageIcon("loading.png")));
                    Main.startRom(i_);
                    // System.out.println("Start game: " +names.get(i_));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            b.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    // System.out.println(e.getKeyCode());
                    switch (e.getKeyCode()){
                        case 40:
                            b.transferFocus();
                            break;
                        case 38:
                            b.transferFocusBackward();
                            break;
                    }
                }
            });

            b.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    scroll();
                }
                @Override
                public void focusLost(FocusEvent e) {}
            });
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            bPanel.add(b);
            bPanel.setOpaque(false);
            GridBagConstraints cons = new GridBagConstraints();
            cons.gridy = i+1;
            innerFrame.add(bPanel, cons);
            // getRootPane().setDefaultButton(b);

        }
        add(window);
        setLayout(null);
        setUndecorated(true);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void setColor(Menu menu) {
        menuColor = new Color(slide(menuColor.getRed(), "red"), slide(menuColor.getGreen(), "green"), slide(menuColor.getBlue(), "blue"), 255);
        innerFrame.setBackground(menuColor);
        pad.setBackground(menuColor);
    }
    //slide RGD values up or down
    private static int slide(int color, String name) {
        int direction;
        switch (name){
            case "red":
                direction = rDir;
                break;
            case "green":
                direction = gDir;
                break;
            case "blue":
                direction = bDir;
                break;
            default:
                direction = 0;
                break;
        }

        if(color == 255) direction = 0;
        else if (color == 150) direction = 1;

        switch (direction){
            case 1:
                color += Math.random()*3;
                if(color > 255) color = 255;
                break;
            case 0:
                color -= Math.random()*2;
                if(color < 150) color = 150;
                break;
        }
        switch (name){
            case "red":
                rDir = direction;
                break;
            case "green":
                gDir = direction;
                break;
            case "blue":
                bDir = direction;
                break;
        }
        return color;
    }

    public void scroll() {
        window.setScrollPosition(this.getFocusOwner().getParent().getX(), this.getFocusOwner().getParent().getY());
    }
}