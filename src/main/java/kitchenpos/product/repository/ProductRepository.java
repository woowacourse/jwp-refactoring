package kitchenpos.product.repository;

import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당하는 상품이 존재하지 않습니다."));
    }
}
