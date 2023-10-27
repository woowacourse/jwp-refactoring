package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.converter.OrderLineItemConverter;
import kitchenpos.order.persistence.OrderLineItemDataAccessor;
import kitchenpos.order.persistence.dto.OrderLineItemDataDto;
import kitchenpos.support.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderLineItemRepository extends
        BaseRepository<OrderLineItem, OrderLineItemDataDto, OrderLineItemDataAccessor, OrderLineItemConverter> {

    public OrderLineItemRepository(final OrderLineItemDataAccessor dataAccessor,
                                   final OrderLineItemConverter converter) {
        super(dataAccessor, converter);
    }

    public List<OrderLineItem> findAllByOrderId(final Long orderId) {
        return converter.dataToEntity(dataAccessor.findAllByOrderId(orderId));
    }
}
