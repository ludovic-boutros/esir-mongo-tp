package fr.esir.mongo.users;

import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints
 * to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
@AllArgsConstructor
public class UserRouter extends RouteBuilder {

  private final UserGenerator userGenerator;
  
  @Override
  public void configure() {
    from("timer:user?period={{user.timer.period}}").routeId("user-generator")
            .process(userGenerator)
            .filter(body().isNotNull())
            .to("direct:add-user");
    
    from("direct:add-user")
            // TODO
            .to("stream:out")            
            .to("mongodb:mongo?database=forum&collection=users&operation=insert")
            ;
  }

}
