package igor.shmidt.router.integrational;

import igor.shmidt.router.config.PostgresConfiguration;
import igor.shmidt.router.routing.model.WikiPage;
import igor.shmidt.router.routing.repository.LinksRepository;
import igor.shmidt.router.routing.repository.WikiPageRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = {PostgresConfiguration.class})
public class RepositoryTests {

    @Autowired
    private WikiPageRepository wikiPageRepository;

    @Autowired
    private LinksRepository linksRepository;

    @Test
    @Order(1)
    void saveEntity() {
        WikiPage strikeBack = new WikiPage("STRIKE BACK", "wiki.strike.back");
        strikeBack = wikiPageRepository.save(strikeBack);

        assertNotEquals(0L, strikeBack.getId());
    }

    @Test
    @Order(2)
    void checkInsertCorrection() {
        Optional<WikiPage> same = wikiPageRepository.findById(1L);
        WikiPage selected = same.get();

        assertNotNull(selected);
    }

    @Test
    @Order(3)
    void checkThatEntityFieldsIsNotNull() {
        Optional<WikiPage> same = wikiPageRepository.findById(1L);
        WikiPage selected = same.get();

        assertNotNull(selected.getPageLink());
        assertNotNull(selected.getPageTitle());
    }

    @Test
    @Order(4)
    void checkThatFieldsIsNotEmpty() {
        Optional<WikiPage> same = wikiPageRepository.findById(1L);
        WikiPage selected = same.get();

        assertNotEquals("", selected.getPageTitle());
        assertNotEquals("", selected.getPageLink());
    }

}