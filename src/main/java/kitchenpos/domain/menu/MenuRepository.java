package kitchenpos.domain.menu;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @EntityGraph(attributePaths = {"menuProducts"})
    List<Menu> findAll();

    int countByIdIn(List<Long> menuIds);
}
