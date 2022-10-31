package kitchenpos.dao.jpa;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderDao extends OrderDao, JpaRepository<Order, Long> {
}
