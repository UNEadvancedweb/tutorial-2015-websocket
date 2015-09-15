package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Gibberish;
import model.GibberishHub;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

public class JsonExample extends Controller {

    public static ObjectNode toJson(Gibberish g) {
        ObjectNode n = Json.newObject();
        n.put("subject", g.getSubject());
        n.put("adverb", g.getAdverb());
        n.put("verb", g.getVerb());
        n.put("adjective", g.getAdjective());
        n.put("object", g.getObj());
        return n;
    }

    public static Result getGibberish(int n) {
        ObjectNode[] arr = new ObjectNode[n];

        ArrayNode an = Json.newArray();
        for (int i = 0; i < n; i++) {
            Gibberish g = new Gibberish();
            an.add(toJson(g));

            // Send the gibberish to the GibberishHub
            GibberishHub.getInstance().send(g);

        }

        return ok(an);
    }

    public static Result postGibberish() {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            Gibberish g = new Gibberish();
            g.setSubject(json.findPath("subject").textValue());
            g.setAdverb(json.findPath("adverb").textValue());
            g.setVerb(json.findPath("verb").textValue());
            g.setAdjective(json.findPath("adjective").textValue());
            g.setObj(json.findPath("object").textValue());

            System.out.println(g);

            return ok("printed it");
        }
    }

    /**
     * Our WebSockets endpoint. We'll assume we're sending String messages for now
     */
    public static WebSocket<String> socket(String topic) {

        /*
         Play framework provides an Actor for client automatically. That's the <code>out</code> argument below.

         We need to tell Play what kind of actor we want on the server side. We do that with a "Props" object, because
         Play will ask Akka to create the actor for us.

         We want a GibberishWebSocketActor, and we want to pass the client actor and the topic as constructor arguments.
         GibberishWebSocketActor.props(topic, out) will produce a Props object saying that.
         */
        return WebSocket.<String>withActor((out) -> GibberishWebSocketActor.props(topic, out));
    }

}
