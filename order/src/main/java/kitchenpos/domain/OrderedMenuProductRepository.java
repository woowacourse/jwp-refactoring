package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedMenuProductRepository extends JpaRepository<OrderedMenuProduct, Long> {
}
