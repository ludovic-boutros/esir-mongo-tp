package fr.esir.mongo.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDBConfiguration {
  @Value("${mongo.cnx.string}")
  private String mongoCnxString;

  @Bean(name = "mongo")
  public MongoClient mongoComponent() {
    return MongoClients.create(mongoCnxString);
  }
}
