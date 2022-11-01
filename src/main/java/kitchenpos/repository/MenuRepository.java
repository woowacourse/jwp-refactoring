package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

public interface MenuRepository extends Repository<Menu, Long> {
    Menu save(Menu menu);

    @EntityGraph(attributePaths = {"menuGroup", "menuProducts", "menuProducts.product"})
    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
