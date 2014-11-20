package actors;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

class Messages {

  public static class Broadcast {
      private JsonNode msg;

      public Broadcast(JsonNode msg) {

          this.msg = msg;
      }

      public JsonNode message() { return msg; }
  }

  public static class SaveLike {
        private String userid;
        private String data;

        public SaveLike(String userid, String data) {
            this.userid = userid;
            this.data = data;
        }
  }


}

public class UserActor extends AbstractActor {

    private String userid;
    private ActorRef out;

    private Optional<String> like = Optional.empty();

    private ActorSelection all = getContext().actorSelection("../../*/handler");


//    @Override
//    public SupervisorStrategy supervisorStrategy() {
//        return OneForOneStrategy.apply(
//           DeciderBuilder
//                   .match(RuntimeException.class, e -> SupervisorStrategy.restart())
//                   .build()
//        );
//    }


    public static Props props(String userid, ActorRef out) {
        return Props.create(UserActor.class, userid, out);
    }

    @Override
    public void postRestart(Throwable reason) {
        System.out.println(">>>>>>>>> Recovered from failure");
    }

    public UserActor(String userid, ActorRef out) {
        this.userid = userid;
        this.out = out;
        receive(ReceiveBuilder
                .match(JsonNode.class, this::takeAction)
                .match(Messages.Broadcast.class, this::sendResponse)
                .build());
    }


    private void takeAction(JsonNode msg) {
        String action = msg.findPath("action").asText();
        String value = msg.findPath("value").asText();
        if(action.equals("like")) {
         like = Optional.of(value);
         saveInfo(userid, like);
       } else {
         out.tell(msg, self());
         all.tell(new Messages.Broadcast(msg), self());
       }
    }

    private void sendResponse(Messages.Broadcast msg) {
        if(sender() != self())
            out.tell(msg.message(), self());
    }


    private void saveInfo(String userid, Optional like) {
       ActorRef db = getContext().actorOf(Db.props());
       db.tell(new Messages.SaveLike(userid, like.toString()), self());
    }
}



class Db extends AbstractActor {

    public static Props props() { return Props.create(Db.class); }

    public Db() {
        receive(ReceiveBuilder
                .match(Messages.SaveLike.class, this::save)
               .build());
    }

    private void save(Messages.SaveLike like) {
        System.out.println("Trying to save...");
        //save to db
        if(1 == 1)
          throw new RuntimeException("Cannot save to db");
        //after successful stop yourself
        getContext().stop(self());
    }
}
