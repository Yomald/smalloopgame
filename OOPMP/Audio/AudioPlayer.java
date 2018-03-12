package Audio;


import javax.sound.sampled.*;

public class AudioPlayer {

 private Clip clip;
// audio player
//reads in audio stream and opens it in a clip object
 public AudioPlayer(String s) {

  try {

   AudioInputStream ais =  AudioSystem.getAudioInputStream(getClass().getResourceAsStream(s));
   AudioFormat baseFormat = ais.getFormat();
   AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
   AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
   clip = AudioSystem.getClip();
   clip.open(dais);
  } catch (Exception e) {
   e.printStackTrace();
  }

 }
// play audio
 public void play() {
  if (clip == null) return;
  stop();
  clip.setFramePosition(0);
  clip.start();
 }
//stop audio
 public void stop() {
  if (clip.isRunning()) clip.stop();
 }
//close the audio
 public void close() {
  stop();
  clip.close();
 }

}