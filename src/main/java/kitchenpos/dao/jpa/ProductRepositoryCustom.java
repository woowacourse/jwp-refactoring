package kitchenpos.dao.jpa;

import java.util.List;
import kitchenpos.domain.Product;

public interface ProductRepositoryCustom {

    boolean existsByIn(List<Product> products);
}
