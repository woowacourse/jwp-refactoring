package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderDao extends JpaRepository<Order, Long>, OrderDao {
}
