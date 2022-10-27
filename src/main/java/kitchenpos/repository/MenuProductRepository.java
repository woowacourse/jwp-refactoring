package kitchenpos.repository;


import org.springframework.data.repository.Repository;

import kitchenpos.domain.MenuProduct;

public interface MenuProductRepository extends Repository<MenuProduct, Long> {
    MenuProduct save(MenuProduct menuProduct);
}
