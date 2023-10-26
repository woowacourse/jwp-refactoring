package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.converter.OrderConverter;
import kitchenpos.order.persistence.OrderDataAccessor;
import kitchenpos.order.persistence.dto.OrderDataDto;
import kitchenpos.support.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository extends BaseRepository<Order, OrderDataDto, OrderDataAccessor, OrderConverter> {

    public OrderRepository(final OrderDataAccessor dataAccessor, final OrderConverter converter) {
        super(dataAccessor, converter);
    }

    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        return dataAccessor.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<String> orderStatuses) {
        return dataAccessor.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }
}
