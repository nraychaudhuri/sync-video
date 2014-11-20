package controllers;

import actors.UserActor;
import com.fasterxml.jackson.databind.JsonNode;
import play.*;
import play.mvc.*;

import views.html.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Application extends Controller {

    //private static List<WebSocket.Out<String>> connections = new CopyOnWriteArrayList<>();

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result video() {
      return ok(video.render());
    }


    public static WebSocket<JsonNode> join(String userid) {
        return WebSocket.withActor(out -> UserActor.props(userid, out));
    }

//    public static void notifyOthers(String msg) {
//      connections.forEach(out -> out.write(msg));
//
//    }

}
