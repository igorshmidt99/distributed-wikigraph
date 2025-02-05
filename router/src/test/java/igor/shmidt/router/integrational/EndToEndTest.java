package igor.shmidt.router.integrational;

import igor.shmidt.router.config.PostgresConfiguration;
import igor.shmidt.router.config.TestcontainersConfiguration;
import igor.shmidt.router.routing.model.WikiPage;
import igor.shmidt.router.routing.repository.LinksRepository;
import igor.shmidt.router.routing.repository.WikiPageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Import({PostgresConfiguration.class, TestcontainersConfiguration.class})
public class EndToEndTest {

    @Autowired
    private WikiPageRepository wikiPageRepository;

    @Autowired
    private LinksRepository linksRepository;

    @BeforeEach
    void init() {
        initData();
    }

    @Test
    void selectLinks() {
        List<String> linksOnPage = linksRepository.findLinksOnPage(1L);
        assertFalse(linksOnPage.isEmpty());
    }


    void initData() {
        WikiPage smesharikiPage = new WikiPage("смешарики", "wiki.смешарики");
        WikiPage kroshPage = new WikiPage("крош", "wiki.смешарики.крош");
        WikiPage barashPage = new WikiPage("бараш", "wiki.смешарики.бараш");
        WikiPage kopatichPage = new WikiPage("копатыч", "wiki.смешарики.копатыч");
        List<WikiPage> smeshariki = List.of(smesharikiPage, kroshPage, barashPage, kopatichPage);
        smeshariki = wikiPageRepository.saveAll(smeshariki);

        List<Long> linksId = smeshariki
                .stream()
                .map(WikiPage::getId)
                .toList()
                .subList(1, smeshariki.size());

        int i = linksRepository.addLinksToPage(smesharikiPage.getId(), linksId);
    }

}
