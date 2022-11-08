package kitchenpos.domain.menu;

import java.util.List;

public interface ProductRepository {

    Product get(Long id);

    Product add(Product product);

    List<Product> getAll();
}
