package menu.repository;

import java.util.List;
import menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Long countByIdIn(final List<Long> ids);
}
