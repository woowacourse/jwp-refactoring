package kitchenpos.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product getBy(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("그런 상품은 없습니다"));
    }

    default void validateContains(Long id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("그런 상품은 없습니다");
        }
    }
}
