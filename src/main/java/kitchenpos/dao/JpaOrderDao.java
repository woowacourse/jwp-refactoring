package kitchenpos.dao;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderDao extends JpaRepository<Order, Long>, OrderDao {
}
