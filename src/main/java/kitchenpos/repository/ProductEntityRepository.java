package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Product;

public interface ProductEntityRepository {
    List<Product> getAllByIdIn(List<Long> toProductIds);
}
