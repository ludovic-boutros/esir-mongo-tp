package fr.esir.mongo.threads;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author lboutros
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
public class Thread {
  @EqualsAndHashCode.Include
  private final String _id;
  
  private final String title;
  
  private final String userId;

  // Keeping all post can be huge => OOM
  // Usually, paginated
  // Again, this is not for production env.
  @Builder.Default
  private final List<String> postIds = new ArrayList<>();

  public void addPost(String postId) {
    postIds.add(postId);
  }
}
