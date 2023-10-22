package kitchenpos.domain.repository;

import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product getById(final Long productId) {
        return findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("id가 " + productId + "인 Product를 찾을 수 없습니다!"));
    }
}
