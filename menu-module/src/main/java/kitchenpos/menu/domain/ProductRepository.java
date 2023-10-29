package kitchenpos.menu.domain;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product findProductById(final Long productId) {
        return findById(productId).orElseThrow(() -> new EmptyResultDataAccessException("상품 식별자값으로 상품을 조회할 수 없습니다.", 1));
    }
}
