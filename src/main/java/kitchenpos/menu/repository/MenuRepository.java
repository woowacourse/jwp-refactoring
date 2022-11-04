package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query(value = "SELECT COUNT(*) FROM menu WHERE menu_id IN (:menuIds)", nativeQuery = true)
    int countExistMenu(List<Long> menuIds);
}
