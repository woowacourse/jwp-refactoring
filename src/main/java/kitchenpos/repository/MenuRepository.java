package kitchenpos.repository;

import kitchenpos.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("select distinct m from Menu m join fetch m.menuProducts.menuProducts")
    List<Menu> findAllWithMenuProducts();
}
