package igor.shmidt.router.routing.repository;

import igor.shmidt.router.routing.model.WikiPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WikiPageRepository extends JpaRepository<WikiPage, Long> {

}
