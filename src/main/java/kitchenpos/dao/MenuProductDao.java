package kitchenpos.dao;

import kitchenpos.domain.menu.MenuProduct;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MenuProductDao extends Repository<MenuProduct, Long> {
    MenuProduct save(MenuProduct entity);

    Optional<MenuProduct> findBySeq(Long id);

    List<MenuProduct> findAll();
}
