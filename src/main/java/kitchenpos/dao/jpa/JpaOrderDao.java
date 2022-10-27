package kitchenpos.dao.jpa;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface JpaOrderDao extends OrderDao, JpaRepository<Order, Long> {
}
