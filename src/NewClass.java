
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class NewClass {

    public static void main(String[] args) {

        String feel = "Eerie";

        List<String> names = new ArrayList<>();
//        try {
//            URL link = new URL("http://incompetech.com/music/royalty-free/index.html?feels[]=" + feel);
//            Scanner sc = new Scanner(new InputStreamReader(link.openStream()));
//
//            String ln;
//            byte i = 0;
//
//            while (sc.hasNextLine()) {
//                ln = sc.nextLine();
//
//                switch (i) {
//                    case 0:
//                        if (ln.equals("<!-- Main Table -->")) {
//                            i = 1;
//                        }
//                        break;
//                    case 1:
//                        if (ln.startsWith("<BR><a href=\"javascript:html5play('")) {
//                            names.add(ln.split("'")[1].replaceFirst(" ", "%20"));
//                        } else {
//                            if (ln.equals("<P>")) {
//                                i = 2;
//                            }
//                        }
//                        break;
//                }
//            }
//            sc.close();
//        } catch (IOException ioe) {
//            System.err.println(ioe);
//        }
        
        names = getNames(feel);

        for (String name : names) {
            System.out.println(name);
        }

//        try {
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new URL("http://www.incompetech.com/music/royalty-free/mp3-royaltyfree/" + names.get(0)));
//            Clip clip = AudioSystem.getClip();
//            clip.open(audioInputStream);
//            clip.start();
//        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
//            System.err.println(ex);
//        }
        try {
            // Open an audio input stream.
            URL url = new URL("http://www.incompetech.com/music/royalty-free/mp3-royaltyfree/" + names.get(0));
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    private static List<String> getNames(String feel) {
        List<String> names = new ArrayList<>();
        try {
            URL link = new URL("http://incompetech.com/music/royalty-free/index.html?feels[]=" + feel);
            Scanner sc = new Scanner(new InputStreamReader(link.openStream()));

            String ln;
            byte i = 0;

            while (sc.hasNextLine()) {
                ln = sc.nextLine();

                switch (i) {
                    case 0:
                        if (ln.equals("<!-- Main Table -->")) {
                            i = 1;
                        }
                        break;
                    case 1:
                        if (ln.startsWith("<BR><a href=\"javascript:html5play('")) {
                            names.add(ln.split("'")[1].replaceFirst(" ", "%20"));
                        } else {
                            if (ln.equals("<P>")) {
                                i = 2;
                            }
                        }
                        break;
                }
            }
            sc.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        return names;
    }
}
