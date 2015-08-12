package controllers;

import model.BCryptExample;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;


public class Application extends Controller {

    public static Result index() {
        int[] arr = new int[5];
        for (int i = 0; i < 5; i++) {
            arr[i] = model.Captcha.randomPhotoIdx();
        }

        return ok(views.html.application.index.render(arr));
    }

    public static Result matches() {
        return internalServerError("Not implemented yet...");
    }

    public static Result captcha() {
        JsonNode values = request().body().asJson();

        return ok(values.toString());
    }


}
