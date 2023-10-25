package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.menuproduct.MenuProduct;
import org.springframework.data.repository.Repository;

public interface MenuProductDao extends Repository<MenuProduct, Long> {

    MenuProduct save(MenuProduct entity);

    Optional<MenuProduct> findById(Long id);

    List<MenuProduct> findAll();
}
