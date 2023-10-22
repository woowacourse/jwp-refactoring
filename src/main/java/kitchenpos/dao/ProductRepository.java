package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제품 ID가 존재하지 않습니다."));
    }
}
