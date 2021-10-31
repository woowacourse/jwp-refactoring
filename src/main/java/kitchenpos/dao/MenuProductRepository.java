package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    List<MenuProduct> findALlByMenu(Menu menu);
}
