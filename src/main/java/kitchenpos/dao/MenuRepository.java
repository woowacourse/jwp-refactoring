package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @EntityGraph(attributePaths = {"menuProducts"}, type = EntityGraphType.LOAD)
    List<Menu> findAll();
}
