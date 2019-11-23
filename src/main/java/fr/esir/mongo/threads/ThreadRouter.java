package fr.esir.mongo.threads;

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
public class ThreadRouter extends RouteBuilder {

  private final ThreadGenerator threadGenerator;
  
  @Override
  public void configure() {
    from("timer:user?period={{thread.timer.period}}").routeId("thread-generator")
            .process(threadGenerator)
            .filter(body().isNotNull())
            .to("direct:add-thread");
    
    from("direct:add-thread")
            // TODO
            .to("stream:out")
            .to("mongodb:mongo?database=forum&collection=threads&operation=insert")
            ;
  }

}
