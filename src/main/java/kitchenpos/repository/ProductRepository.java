package kitchenpos.repository;

import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product findMandatoryById(final Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
