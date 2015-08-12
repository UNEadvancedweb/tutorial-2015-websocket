package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Gibberish;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

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
            an.add(toJson(new Gibberish()));
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


}
