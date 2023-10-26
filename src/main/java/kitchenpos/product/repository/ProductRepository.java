package kitchenpos.product.repository;

import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.exception.ProductException.NotExistsProductException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByDeletedFalse();

    Long countByIdIn(List<Long> ids);

    default Product getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new NotExistsProductException(id));
    }
}
