package kitchenpos.dao;

import kitchenpos.domain.Order;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface OrderDao extends CrudRepository<Order, Long> {
    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);

    default Order findMandatoryById(final Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
