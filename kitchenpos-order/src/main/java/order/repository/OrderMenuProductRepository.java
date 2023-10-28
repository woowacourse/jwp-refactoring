package order.repository;

import order.domain.OrderMenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuProductRepository extends JpaRepository<OrderMenuProduct, Long> {
}
