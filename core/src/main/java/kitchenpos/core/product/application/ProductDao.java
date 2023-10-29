package kitchenpos.core.product.application;

import java.util.List;
import kitchenpos.core.product.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductDao extends CrudRepository<Product, Long> {
    List<Product> findAll();

    default Product findMandatoryById(Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
