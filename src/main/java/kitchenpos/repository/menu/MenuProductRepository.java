package kitchenpos.repository.menu;

import kitchenpos.domain.menu.MenuProduct;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    @EntityGraph(attributePaths = "menu")
    List<MenuProduct> findAll();
}
