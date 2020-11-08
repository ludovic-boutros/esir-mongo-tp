package fr.esir.mongo.posts;

import lombok.AllArgsConstructor;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.endpoint.dsl.MongoDbEndpointBuilderFactory.MongoDbOperation.insert;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints
 * to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
@AllArgsConstructor
public class PostRouter extends EndpointRouteBuilder {

  private static final String ADD_POST = "add-post";
  private final PostGenerator postGenerator;

  @Override
  public void configure() {
    from(timer("post").period("{{post.timer.period}}")).routeId("post-generator")
            .process(postGenerator)
            .filter(body().isNotNull())
            .to(direct("add-post"));

    from(direct(ADD_POST))
            .id(ADD_POST)
            .to(stream("out"))
            // TODO manage links between posts <-> threads            
            .to(mongodb("mongo").database("forum").collection("posts").operation(insert));
  }

}
