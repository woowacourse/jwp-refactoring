package kitchenpos.product.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    default List<Product> getAllById(List<Long> productIds) {
        return productIds.stream()
                .map(id -> findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. id : " + id)))
                .collect(Collectors.toUnmodifiableList());
    }
}
