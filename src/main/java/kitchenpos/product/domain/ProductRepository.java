package kitchenpos.product.domain;

import java.util.List;
import kitchenpos.product.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

    default Product getProduct(final Long id) {
        return findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    List<Product> findAll();

    @Override
    List<Product> findAllById(Iterable<Long> longs);
}
