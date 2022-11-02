package kitchenpos.repository.entity;

import java.util.List;
import kitchenpos.domain.product.Product;

public interface ProductEntityRepository {
    List<Product> getAllByIdIn(List<Long> toProductIds);
}
