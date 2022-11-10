package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.table.dao.OrderTableDao;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final MenuDao menuDao;
    private final OrderTableDao orderTableDao;

    public OrderValidator(MenuDao menuDao, OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderTableDao = orderTableDao;
    }

    public void validator(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        validateEmptyOrderLineItemRequests(orderLineItemRequests);
        validateExistOrderLineItemRequestInMenu(orderLineItemRequests);
        validateOrderTableRequest(orderTableId);
    }

    private void validateOrderTableRequest(final Long orderTableId) {
        orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
    }

    private void validateEmptyOrderLineItemRequests(final List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException("주문 항목이 비어있을 수 없습니다.");
        }
    }

    private void validateExistOrderLineItemRequestInMenu(List<OrderLineItemRequest> orderLineItemRequests) {
        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴는 주문할 수 없습니다.");
        }
    }
}
