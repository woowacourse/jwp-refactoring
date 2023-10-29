package kitchenpos.repository;

import kitchenpos.domain.OrderMenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuProductRepository extends JpaRepository<OrderMenuProduct, Long> {
}
