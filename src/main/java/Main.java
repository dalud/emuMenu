import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {
    static String emu = "C:\\Nes\\Jnes\\Jnes.exe";   //path to emulator.exe
    static File path = new File("C:\\Nes\\Roms");   //path to Rom folder
    static String[] games = path.list();    //raw File names
    static GraphicsDevice vc;   //default video controller
    private static Controllers gamepads;

    public static void main(String[] args){
        List<Object> names = beautify(Arrays.stream(games));
        Menu menu = new Menu(names);

        vc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        vc.setFullScreenWindow(menu);
        Menu.window.setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getFullScreenWindow().getHeight());

        gamepads = new Controllers();
        gamepads.init();

        //render loop
        int counter = 0;
        while (true){
            // Set color
            if(counter > 100000){
                menu.setColor(menu);
                counter = 0;
            }
            counter++;

            // Poll controllers
            if(gamepads.getState(0).dpadDownJustPressed) {
                // menu.scrollDown((JButton)menu.getFocusOwner(), (JPanel)menu.getFocusOwner().getParent());
                // System.out.println(menu.getFocusOwner().getAccessibleContext());
            }
        }
    }

    // Parse rom names into prettier format
    public static List<Object> beautify(Stream<String> s){
        return s.map(n -> n.replaceAll("\\.nes", ""))
                .map(n -> n.replaceAll("\\(U\\)", ""))
                .map(n -> n.replaceAll("\\[!\\]", ""))
                .map(n -> n.replaceAll("\\[p1\\]", ""))
                .map(n -> n.replaceAll(", The", ""))
                .map(n -> n.replaceAll("\\(PRG 0\\)", ""))
                .map(n -> n.replaceAll("\\(Unl\\)", ""))
                .map(n -> n.replaceAll("\\(Japan\\)", ""))
                .collect(Collectors.toList());
    }

    public static void startRom(int i) throws IOException {
        System.out.println(games[i]);
        // Process p = new ProcessBuilder(emu, path +"\\" +games[i] +"/fullscreen").start();
        ProcessBuilder builder = new ProcessBuilder(emu, path+"\\"+games[i]);
        Process p = builder.start();
    }
}
