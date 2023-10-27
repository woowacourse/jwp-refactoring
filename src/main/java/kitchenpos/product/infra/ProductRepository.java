package kitchenpos.product.infra;

import java.util.NoSuchElementException;
import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
    }
}
