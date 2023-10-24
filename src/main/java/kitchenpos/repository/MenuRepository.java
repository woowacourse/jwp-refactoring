package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m From Menu m LEFT JOIN FETCH m.menuProducts")
    List<Menu> findAllWithFetch();
}
