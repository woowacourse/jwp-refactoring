package kitchenpos.menu.domain;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @EntityGraph(attributePaths = "menuProducts")
    List<Menu> findAll();

    int countByIdIn(@Param("menuIds") List<Long> menuIds);
}
