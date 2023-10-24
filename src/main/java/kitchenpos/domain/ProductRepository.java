package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product getById(Long id) {
        return findById(id).orElseThrow(() -> new ProductException("해당하는 상품이 없습니다."));
    }
}
