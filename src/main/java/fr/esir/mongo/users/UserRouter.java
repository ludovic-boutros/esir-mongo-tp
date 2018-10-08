package fr.esir.mongo.users;

import io.micrometer.core.instrument.Tags;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.micrometer.MicrometerConstants;
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

  private final Random r = new Random();
  private final UserGenerator userGenerator;
  
  @Override
  public void configure() {
    from("timer:user?period={{user.timer.period}}").routeId("user-generator")
            .process(userGenerator)
            .filter(body().isNotNull())
            .setHeader(MicrometerConstants.HEADER_METRIC_TAGS, constant(Tags.of("phase", "prepare")))            
            .to("micrometer:counter:user.counter?increment=1")            
            .to("direct:add-user");
    
    from("direct:add-user")
            // TODO
            .choice()
            .when(e -> throwError())
              .setHeader(MicrometerConstants.HEADER_METRIC_TAGS, constant(Tags.of("phase", "error")))
              .to("micrometer:counter:user.counter?increment=1")            
              // TODO throw error
            .endChoice()
            .otherwise()
              .to("stream:out")
              .to("mongodb3:mongo?database=forum&collection=users&operation=insert")
              .setHeader(MicrometerConstants.HEADER_METRIC_TAGS, constant(Tags.of("phase", "done")))            
              .to("micrometer:counter:user.counter?increment=1")
            .endChoice()
            .end()
            ;
  }

  private boolean throwError() {
    return r.nextBoolean() && r.nextBoolean() && r.nextBoolean();
  }
}
