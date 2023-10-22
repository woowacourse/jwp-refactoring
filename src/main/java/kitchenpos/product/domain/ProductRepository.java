package kitchenpos.product.domain;

import java.util.NoSuchElementException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    default Product getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("id가 %d인 상품을 찾을 수 없습니다.".formatted(id)));
    }
}
