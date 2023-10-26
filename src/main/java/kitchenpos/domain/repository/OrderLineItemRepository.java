package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.repository.converter.OrderLineItemConverter;
import kitchenpos.persistence.dto.OrderLineItemDataDto;
import kitchenpos.persistence.specific.OrderLineItemDataAccessor;
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
