package kitchenpos.domain.menu.repository;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findAllByMenu(Menu menu);
}
