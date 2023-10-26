package kitchenpos.order;

import kitchenpos.menu.MenuRepository;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;


    public void validate(Long orderTableId, List<OrderLineItem> orderLineItems){
        validateOrderTable(orderTableId);
        validateOrderLineItem(orderLineItems);
    }

    private void validateOrderTable(Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItem(List<OrderLineItem> orderLineItems) {
        validateEmpty(orderLineItems);
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        long menuCount = menuRepository.countByIdIn(menuIds);

        if (orderLineItems.size() != menuCount) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }
}
