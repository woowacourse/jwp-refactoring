package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.repository.converter.OrderConverter;
import kitchenpos.persistence.dto.OrderDataDto;
import kitchenpos.persistence.specific.OrderDataAccessor;
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
