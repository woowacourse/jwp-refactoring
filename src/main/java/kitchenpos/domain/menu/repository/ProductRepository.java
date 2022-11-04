package kitchenpos.domain.menu.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.menu.Product;
import org.springframework.data.repository.Repository;

public interface ProductRepository extends Repository<Product, Long> {

    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    default Product get(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }
}
