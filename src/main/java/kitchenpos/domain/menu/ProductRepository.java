package kitchenpos.domain.menu;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    default Product getById(Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
