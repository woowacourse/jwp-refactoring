package kitchenpos.repository;

import java.util.Collection;
import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends Repository<Menu, Long> {
    Menu save(Menu menu);

    @Query("select distinct m from Menu m join fetch m.menuProducts.menuProducts")
    List<Menu> findAll();

    @Query("select new kitchenpos.repository.OrderingMenu(m.id, m.name, m.price, mg.name)"
            + " from Menu m"
            + " join fetch MenuGroup mg on m.menuGroupId = mg.id"
            + " where m.id in :menuIds")
    List<OrderingMenu> findByIdIn(@Param("menuIds") Collection<Long> menuIds);

}
