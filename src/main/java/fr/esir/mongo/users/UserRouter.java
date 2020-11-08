package fr.esir.mongo.users;

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
public class UserRouter extends EndpointRouteBuilder {

  private static final String ADD_USER = "add-user";
  private final UserGenerator userGenerator;

  @Override
  public void configure() {
    from(timer("user").period("{{user.timer.period}}")).routeId("user-generator")
            .process(userGenerator)
            .filter(body().isNotNull())
            .to(direct("add-user"));

    from(direct(ADD_USER))
            .id(ADD_USER)
            // TODO
            .to(stream("out"))
            .to(mongodb("mongo").database("forum").collection("users").operation(insert));
  }

}
