package igor.shmidt.router.routing.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "page")
public class WikiPage {

    @Id
    @Column(name = "page_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_title")
    private String pageTitle;

    @Column(name = "page_link")
    private String pageLink;

    public WikiPage(String pageTitle, String pageLink) {
        this.pageTitle = pageTitle;
        this.pageLink = pageLink;
    }

    public WikiPage() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WikiPage wikiPage = (WikiPage) o;
        return Objects.equals(id, wikiPage.id) && Objects.equals(pageTitle, wikiPage.pageTitle) && Objects.equals(pageLink, wikiPage.pageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pageTitle, pageLink);
    }
}
