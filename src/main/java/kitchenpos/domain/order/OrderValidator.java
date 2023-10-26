package kitchenpos.domain.order;

import kitchenpos.application.order.request.OrderLineItemCreateRequest;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
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


    public void validate(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItemRequests){
        validateOrderTable(orderTableId);
        validateOrderLineItem(orderLineItemRequests);
    }

    private void validateOrderTable(Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItem(List<OrderLineItemCreateRequest> orderLineItemRequests) {
        validateEmpty(orderLineItemRequests);
        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
        long menuCount = menuRepository.countByIdIn(menuIds);

        if (orderLineItemRequests.size() != menuCount) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmpty(List<OrderLineItemCreateRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
    }
}
