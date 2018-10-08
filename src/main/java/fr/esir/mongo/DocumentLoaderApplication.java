package fr.esir.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.apache.camel.component.micrometer.MicrometerComponent;
import org.apache.camel.component.micrometer.MicrometerConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DocumentLoaderApplication {

  @Value("mongo.cnx.string")
  private String mongoCnxString;

  /**
   * A main method to start this application.
   *
   * @param args main app argruments
   */
  public static void main(String[] args) {
    SpringApplication.run(DocumentLoaderApplication.class, args);
  }

  @Bean(name = "mongo")
  public Mongo mongoComponent() {
    String[] addressPart = mongoCnxString.split(":");
    return new MongoClient(new ServerAddress(addressPart[0], Integer.parseInt(addressPart[1])));
  }

}
