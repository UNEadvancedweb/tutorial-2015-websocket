package model;

/**
 * A nonsense class, just for sending something to the client
 */
public class Gibberish {

    static final String[] SUBJECTS = {
            "Algernon", "Bertie", "Cecily", "Dahlia", "Edwin"
    };

    static final String[] ADVERBS = {
            "quickly", "slowly", "hesitatingly", "grudgingly", "naughtily"
    };

    static final String[] VERBS = {
            "ate", "juggled", "bounced", "dropped", "hid"
    };

    static final String[] ADJECTIVES = {
            "yellow", "polkadot", "small", "smelly", "tasty", "plastic"
    };

    static final String[] OBJS = {
            "toy ducks", "mice", "bananas", "tissues", "shoes"
    };


    public String getSubject() {
        return subject;
    }

    public String getAdverb() {
        return adverb;
    }

    public String getVerb() {
        return verb;
    }

    public String getAdjective() {
        return adjective;
    }

    public String getObj() {
        return obj;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setAdverb(String adverb) {
        this.adverb = adverb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public void setAdjective(String adjective) {
        this.adjective = adjective;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    String subject;
    String adverb;
    String verb;
    String adjective;
    String obj;

    public String allocate(String[] candidates) {
        return candidates[(int)(Math.random() * candidates.length)];
    }

    public Gibberish() {
        this.subject = allocate(SUBJECTS);
        this.adverb = allocate(ADVERBS);
        this.verb = allocate(VERBS);
        this.adjective = allocate(ADJECTIVES);
        this.obj = allocate(OBJS);
     }


    @Override
    public String toString() {
        return subject + " " + adverb + " " + verb + " " + adjective + " " + obj + ".";
    }



}
