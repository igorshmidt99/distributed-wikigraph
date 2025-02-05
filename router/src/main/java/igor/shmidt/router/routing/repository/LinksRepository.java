package igor.shmidt.router.routing.repository;

import java.util.List;

public interface LinksRepository {

    List<String> findLinksOnPage(Long pageId);

    int addLinksToPage(Long pageId, List<Long> links);

}
