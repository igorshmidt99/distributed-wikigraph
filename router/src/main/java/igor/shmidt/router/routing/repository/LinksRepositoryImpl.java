package igor.shmidt.router.routing.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class LinksRepositoryImpl implements LinksRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> findLinksOnPage(Long pageId) {
        String statement = "SELECT page_link " +
                "FROM page p " +
                "JOIN (SELECT link_id FROM links WHERE page_id = ?) l ON p.page_id = l.link_id";
        return (List<String>) entityManager
                .createNativeQuery(statement)
                .setParameter(1, pageId)
                .getResultList();
    }

    @Override
    @Transactional
    public int addLinksToPage(Long pageId, List<Long> links) {
        Query query = createInsert(pageId, links);
        return query.executeUpdate();
    }

    private Query createInsert(Long pageId, List<Long> links) {
        String statement = createStatement(links);
        Query query = entityManager.createNativeQuery(statement);
        return setParameters(query, links, pageId);
    }

    private Query setParameters(Query query, List<Long> links, Long pageId) {
        int i = 0;
        for (Long linkId : links) {
            query = query.setParameter(++i, pageId)
                    .setParameter(++i, linkId);
        }
        return query;
    }

    private String createStatement(List<Long> links) {
        // add on conflict strategy
        StringBuilder statement = new StringBuilder("INSERT INTO links VALUES ");
        for (int i = 0; i < links.size(); i++) {
            if (i < links.size() - 1) {
                statement.append("(?, ?), ");
            } else {
                statement.append("(?, ?)");
            }
        }
        return statement.toString();
    }

}