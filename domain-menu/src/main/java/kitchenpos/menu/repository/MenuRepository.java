package kitchenpos.menu.repository;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @EntityGraph(attributePaths = "menuProducts")
    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
