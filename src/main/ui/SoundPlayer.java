package ui;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 * Class responsible for playing sounds.
 *  Modified from : https://stackoverflow.com/a/15526361  by @SudoRahul
 */

public class SoundPlayer {
    String chimeFile = "./data/chime-login.wav";

    void playChime() throws IOException,
            UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(chimeFile).getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }

}
