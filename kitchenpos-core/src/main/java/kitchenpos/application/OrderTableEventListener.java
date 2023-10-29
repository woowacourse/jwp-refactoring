package kitchenpos.application;

import kitchenpos.application.event.AddGroupTableEvent;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.OrderTableDto;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class OrderTableEventListener {

    private final OrderTableRepository orderTableRepository;

    public OrderTableEventListener(final OrderTableRepository orderRepository) {
        this.orderTableRepository = orderRepository;
    }

    @EventListener
    public void handleCreateTableGroup(final AddGroupTableEvent event) {
        final List<OrderTableDto> orderTableDtos = event.getOrderTableDtos();
        final TableGroup tableGroup = event.getTableGroup();
        final List<OrderTable> savedOrderTables = findOrderTables(orderTableDtos);
        for (final OrderTable orderTable : savedOrderTables) {
            orderTable.group(tableGroup);
        }
    }

    private List<OrderTable> findOrderTables(final List<OrderTableDto> orderTableRequest) {
        final List<Long> orderTableIds = orderTableRequest.stream()
                                                          .map(OrderTableDto::getId)
                                                          .toList();
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdsIn(orderTableIds);
        validateOrderTableSize(orderTableIds, orderTables);
        return orderTables;
    }

    private static void validateOrderTableSize(final List<Long> orderTableIds, final List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size() || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }
}
