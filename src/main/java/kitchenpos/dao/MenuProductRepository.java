package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MenuProductRepository extends Repository<MenuProduct, Long> {
    MenuProduct save(MenuProduct entity);

    Optional<MenuProduct> findBySeq(Long id);

    List<MenuProduct> findAll();
}
