package model;

/**
 * A Single Abstract Method (SAM) interface for things that want to hear about messages
 */
public interface GibberishListener {

    public void receiveGibberish(Gibberish g);

}
