package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Long> {

    List<Order> findAll();

    List<Order> findAllByOrderTableId(final Long orderTableId);
}
