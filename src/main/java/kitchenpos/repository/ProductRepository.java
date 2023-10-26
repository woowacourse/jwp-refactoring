package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByIdIn(List<Long> ids);

    default List<Product> findAllByIdInThrow(List<Long> ids) {
        final List<Product> products = findAllByIdIn(ids);
        if (products.size() != ids.size()) {
            throw new IllegalArgumentException("존재하지 않는 상품 번호가 존재합니다.");
        }
        return products;
    }
}
