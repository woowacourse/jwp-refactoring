package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderChangeOrderStatusRequest;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemCreateRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        validateAllExistOrderLineItems(request.getOrderLineItems());
        final OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());
        final List<OrderLineItem> orderLineItems = makeOrderLineItems(request.getOrderLineItems());
        final Order order = Order.forSave(OrderStatus.COOKING, orderLineItems);
        orderTable.addOrder(order);

        return OrderResponse.from(orderRepository.save(order));
    }

    private void validateAllExistOrderLineItems(final List<OrderLineItemCreateRequest> orderLineItemIds) {
        final List<Long> menuIds = orderLineItemIds.stream()
            .map(OrderLineItemCreateRequest::getMenuId)
            .collect(Collectors.toList());
        final boolean existsAll = menuRepository.countByIds(menuIds) == orderLineItemIds.size();

        if (!existsAll) {
            throw new IllegalArgumentException("존재하지 않는 주문 항목이 포함되어 있습니다.");
        }
    }

    private List<OrderLineItem> makeOrderLineItems(final List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest request : orderLineItemCreateRequests) {
            final Menu menu = menuRepository.getById(request.getMenuId());
            final OrderLineItem orderLineItem = OrderLineItem.forSave(request.getQuantity(), menu.getName(),
                                                                      menu.getPrice(), menu);
            orderLineItems.add(orderLineItem);
        }

        return orderLineItems;
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeOrderStatusRequest request) {
        final Order order = orderRepository.getById(orderId);
        order.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return OrderResponse.from(order);
    }
}
