package kitchenpos.repository.menu;

import java.util.List;
import kitchenpos.domain.menu.Product;

public interface ProductRepositoryCustom {

    boolean existsByIn(List<Product> products);
}
