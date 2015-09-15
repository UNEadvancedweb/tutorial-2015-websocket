package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a simple publish-subscribe list. It maintains a list of listeners, and whenever it receives a call to
 * <code>send</code>, it calls <code>receiveGibberish</code> on every registered listener.
 */
public class GibberishHub {

    List<GibberishListener> listeners;

    static final GibberishHub instance = new GibberishHub();

    public static GibberishHub getInstance() {
        return instance;
    }

    protected GibberishHub() {
        this.listeners = Collections.synchronizedList(new ArrayList<>());
    }

    public void send(Gibberish g) {
        for (GibberishListener listener : listeners) {
            listener.receiveGibberish(g);
        }
    }

    public void addListener(GibberishListener l) {
        this.listeners.add(l);
    }

    public void removeListener(GibberishListener l) {
        this.listeners.remove(l);
    }

}
