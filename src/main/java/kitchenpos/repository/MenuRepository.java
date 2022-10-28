package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

public interface MenuRepository extends Repository<Menu, Long> {
    Menu save(Menu menu);

    @EntityGraph(attributePaths = {"menuProducts", "menuProducts.product"})
    Optional<Menu> findById(Long id);

    @EntityGraph(attributePaths = {"menuProducts", "menuProducts.product"})
    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
