package kitchenpos.persistence;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT distinct m FROM Menu m JOIN FETCH m.menuProducts.values")
    List<Menu> findAllByFetch();

    int countByIdIn(List<Long> menuIds);
}
