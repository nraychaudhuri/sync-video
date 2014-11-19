package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Application extends Controller {

    private static List<WebSocket.Out<String>> connections = new CopyOnWriteArrayList<>();

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result video() {
      return ok(video.render());
    }


    public static WebSocket<String> join(String userid) {
        return WebSocket.whenReady((in, out) -> {
          connections.add(out);
          in.onMessage(Application::notifyOthers);
          in.onClose(() -> connections.remove(out));
        });
    }

    public static void notifyOthers(String msg) {
      connections.forEach(out -> out.write(msg));

    }

}
