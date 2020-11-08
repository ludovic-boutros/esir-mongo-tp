package fr.esir.mongo.threads;

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
public class ThreadRouter extends EndpointRouteBuilder {

  private static final String ADD_THREAD = "add-thread";
  private final ThreadGenerator threadGenerator;

  @Override
  public void configure() {
    from(timer("thread").period("{{thread.timer.period}}")).routeId("thread-generator")
            .process(threadGenerator)
            .filter(body().isNotNull())
            .to(direct("add-thread"));

    from(direct(ADD_THREAD))
            .id(ADD_THREAD)
            // TODO
            .to(stream("out"))
            .to(mongodb("mongo").database("forum").collection("threads").operation(insert));
  }

}
