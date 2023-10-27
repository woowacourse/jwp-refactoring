package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedMenuProductRepository extends JpaRepository<OrderedMenuProduct, Long> {
}
