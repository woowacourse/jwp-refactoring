package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.entity.Menu;
import kitchenpos.domain.entity.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    List<MenuProduct> findAllByMenu(final Menu menu);
}
