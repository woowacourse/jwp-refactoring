package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface MenuRepository extends Repository<Menu, Long> {
    Menu save(Menu menu);

    @Query("select distinct m from Menu m join fetch m.menuProducts.menuProducts")
    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
