package kitchenpos.menu.domain.repository;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    int countByIdIn(List<Long> menuIds);

    @EntityGraph(attributePaths = "menuProducts")
    List<Menu> findAll();
}
