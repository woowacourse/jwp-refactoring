package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.ProductCreateRequest;

public interface ProductService {
    Product create(ProductCreateRequest request);

    List<Product> list();
}
