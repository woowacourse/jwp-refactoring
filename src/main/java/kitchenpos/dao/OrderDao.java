package kitchenpos.dao;

import kitchenpos.dto.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    OrderDto save(OrderDto entity);

    Optional<OrderDto> findById(Long id);

    List<OrderDto> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
