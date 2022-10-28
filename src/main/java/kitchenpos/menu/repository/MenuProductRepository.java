package kitchenpos.menu.repository;


import org.springframework.data.repository.Repository;

import kitchenpos.menu.domain.MenuProduct;

public interface MenuProductRepository extends Repository<MenuProduct, Long> {
    MenuProduct save(MenuProduct menuProduct);
}
