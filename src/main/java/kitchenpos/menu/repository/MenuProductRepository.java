package kitchenpos.menu.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.data.repository.Repository;

public interface MenuProductRepository extends Repository<MenuProduct, Long> {

    MenuProduct save(MenuProduct entity);

    Optional<MenuProduct> findBySeq(Long id);

    List<MenuProduct> findAll();
}
