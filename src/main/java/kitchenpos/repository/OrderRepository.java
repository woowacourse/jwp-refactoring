package kitchenpos.repository;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderDao {

    @Override
    Order save(Order entity);

    @Override
    Optional<Order> findById(Long id);

    @Override
    List<Order> findAll();

    @Override
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    @Override
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
