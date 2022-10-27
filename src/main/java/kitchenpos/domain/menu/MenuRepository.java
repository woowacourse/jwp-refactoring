package kitchenpos.domain.menu;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @EntityGraph(attributePaths = "menuProducts")
    List<Menu> findAll();
}
