package kitchenpos.domain.menu;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findAllByMenu(Menu menu);
}
