package kitchenpos.order.domain.service;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemCreateRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional(readOnly = true)
public class OrderCreateService {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderCreateService(final OrderTableRepository orderTableRepository, final MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public Order create(final OrderCreateRequest request) {
        final OrderTable orderTable = findOrderTable(request);
        final List<OrderLineItem> orderLineItems = createOrderLineItems(request);

        return new Order(orderTable.getId(), orderLineItems);
    }

    private OrderTable findOrderTable(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블입니다.");
        }

        return orderTable;
    }

    private List<OrderLineItem> createOrderLineItems(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (final OrderLineItemCreateRequest createRequest : request.getOrderLineItems()) {
            final Menu menu = menuRepository.findById(createRequest.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 항목입니다."));
            final OrderLineItem orderLineItem = new OrderLineItem(
                    menu.getId(), menu.getName(), menu.getPrice(), createRequest.getQuantity());

            orderLineItems.add(orderLineItem);
        }

        return orderLineItems;
    }
}
