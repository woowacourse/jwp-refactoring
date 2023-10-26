package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuEventDto;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderCreateRequest.OrderLineRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public OrderMapper(
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
    }

    public Order toDomain(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());

        final Order order = orderRepository.save(new Order(orderTable));

        for (OrderLineRequest orderLineItem : request.getOrderLineItems()) {
            MenuEventDto menuEventDto = new MenuEventDto();
            menuEventDto.setId(orderLineItem.getMenuId());
            publisher.publishEvent(menuEventDto);

            orderLineItemRepository.save(new OrderLineItem(order, menuEventDto.getId(), orderLineItem.getQuantity()));
        }

        return order;
    }
}
