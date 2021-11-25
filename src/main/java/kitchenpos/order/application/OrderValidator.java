package kitchenpos.order.application;

import java.util.List;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.exception.MenuNotFoundException;
import kitchenpos.order.exception.OrderTableEmptyException;
import kitchenpos.order.exception.OrderTableNotFoundException;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validateOrder(OrderRequest orderRequest) {
        validateTableStatus(orderRequest.getOrderTableId());
        validateExistMenu(orderRequest.getOrderLineItems());
    }

    private void validateTableStatus(Long orderTableId) {
        OrderTable orderTable = findOrderTableById(orderTableId);

        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException(String.format("%s ID OrderTable이 비어있는 상태입니다.", orderTableId));
        }
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new OrderTableNotFoundException(
                String.format("%s ID에 해당하는 OrderTable이 존재하지 않습니다.", orderTableId)
            ));
    }

    private void validateExistMenu(List<OrderLineItemRequest> orderLineItemRequests) {
        for (OrderLineItemRequest request : orderLineItemRequests) {
            validateExistMenu(request.getMenuId());
        }
    }

    private void validateExistMenu(Long menuId) {
        if (menuRepository.existsById(menuId)) {
            return;
        }

        throw new MenuNotFoundException(String.format("%s ID의 Menu가 존재하지 않습니다.", menuId));
    }
}
