package kitchenpos.prodcut;

import kitchenpos.prodcut.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, Long> {

}
