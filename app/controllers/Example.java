package controllers;

import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

public class Example extends Controller {

    public static F.Promise<Result> getTweets() {
       F.Promise<WSResponse> result = WS.url("http://www.twitter.com/timeline").get();
       return result.map(r -> ok("tweets"));
    }
}
