package fr.esir.mongo.users;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 *
 * @author lboutros
 */
@Component
public class UserGenerator implements Processor {

  private final static Random RANDOM = new Random(System.currentTimeMillis());

  @Value("${user.max.age}")
  private int userMaxAge;

  @Value("${user.min.age}")
  private int userMinAge;

  @Value("${user.max.id}")
  private int userMaxId;

  @Value("${user.id.mask}")
  private String userIdMask;

  @Value("classpath:super-heroes.txt")
  private Resource heroNameResource;

  private String[] heroNames;

  // This is a dummy example, you should NEVER do that for a production app
  private final ConcurrentHashMap<String, User> knownUsers = new ConcurrentHashMap<>();

  @Override
  public void process(Exchange exchange) throws Exception {
    exchange.getIn().setBody(generateUser());
  }

  @PostConstruct
  private void initSuperHeroes() throws IOException {
    List<String> heroNameList = IOUtils.readLines(heroNameResource.getInputStream(), StandardCharsets.UTF_8.name());
    heroNames = heroNameList.toArray(new String[heroNameList.size()]);
  }

  private User generateUser() {
    String nickname = heroNames[RANDOM.nextInt(heroNames.length)];
    String id = nickname + "|" + String.format(userIdMask, RANDOM.nextInt(userMaxId));

    User user = User.builder()
            .age(Math.max(userMinAge, RANDOM.nextInt(userMaxAge)))
            ._id(id)
            .nickname(nickname)
            .build();

    User oldUser = knownUsers.putIfAbsent(id, user);
    if (oldUser == null) {
      return user;
    } else {
      return null;
    }
  }

  public User getRandomKnownUser() {
    // TODO improve this method (we should use another collection to get random user in a faster way)
    Iterator<User> iterator = knownUsers.values().iterator();

    User retValue = null;
    if (!knownUsers.isEmpty()) {
      int nextPos = RANDOM.nextInt(knownUsers.size());

      for (int i = 0; i <= nextPos; i++) {
        retValue = iterator.next();
      }
    }

    return retValue;
  }
}
