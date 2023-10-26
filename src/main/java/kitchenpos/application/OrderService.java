package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ChangeOrderDto;
import kitchenpos.application.dto.CreateOrderDto;
import kitchenpos.application.dto.ReadOrderDto;
import kitchenpos.application.exception.MenuNotFoundException;
import kitchenpos.application.exception.OrderNotFoundException;
import kitchenpos.application.exception.OrderTableNotFoundException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.CreateOrderLineItemRequest;
import kitchenpos.ui.dto.request.CreateOrderRequest;
import kitchenpos.ui.dto.request.UpdateOrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public CreateOrderDto create(final CreateOrderRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                                                          .orElseThrow(OrderTableNotFoundException::new);
        final List<OrderLineItem> orderLineItems = findOrderLineItems(request);
        final Order order = new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        final Order persistOrder = orderRepository.save(order);

        return new CreateOrderDto(persistOrder);
    }

    private List<OrderLineItem> findOrderLineItems(final CreateOrderRequest request) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (final CreateOrderLineItemRequest orderLineItemRequest : request.getOrderLineItems()) {
            final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                                            .orElseThrow(MenuNotFoundException::new);

            orderLineItems.add(
                    new OrderLineItem(menu.getId(), menu.name(), menu.price(), orderLineItemRequest.getQuantity())
            );
        }

        return orderLineItems;
    }

    public List<ReadOrderDto> list() {
        return orderRepository.findAll()
                .stream()
                .map(ReadOrderDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChangeOrderDto changeOrderStatus(final Long orderId, final UpdateOrderStatusRequest request) {
        final Order persistOrder = orderRepository.findById(orderId)
                                                  .orElseThrow(OrderNotFoundException::new);
        persistOrder.updateOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return new ChangeOrderDto(persistOrder);
    }
}
