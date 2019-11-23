package fr.esir.mongo.posts;

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
public class PostRouter extends RouteBuilder {

  private final PostGenerator postGenerator;

  @Override
  public void configure() {
    from("timer:user?period={{post.timer.period}}").routeId("post-generator")
            .process(postGenerator)
            .filter(body().isNotNull())
            .to("direct:add-post");
    
    from("direct:add-post")
            .to("stream:out")
            // TODO manage links between posts <-> threads            
            .to("mongodb:mongo?database=forum&collection=posts&operation=insert")
            ;
  }

}
