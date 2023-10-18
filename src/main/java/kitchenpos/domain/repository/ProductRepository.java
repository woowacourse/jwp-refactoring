package kitchenpos.domain.repository;

import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product getById(Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
