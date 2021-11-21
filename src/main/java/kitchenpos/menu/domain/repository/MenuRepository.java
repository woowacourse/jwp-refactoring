package kitchenpos.menu.domain.repository;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("select distinct menu " +
            "from Menu as menu " +
            "left join fetch menu.menuProducts ")
    List<Menu> findAllFetchJoinMenuProducts();
}
