package kitchenpos.order.application;

import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderCreateRequest.OrderLineRequest;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderLineItemRepository;
import kitchenpos.order.OrderRepository;
import kitchenpos.order.OrderTable;
import kitchenpos.order.OrderTableRepository;
import java.util.List;
import kitchenpos.menu.application.dto.MenuEventDto;
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

    public Order toOrderTables(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());

        final Order order = orderRepository.save(new Order(orderTable));

        for (OrderLineRequest orderLineItem : request.getOrderLineItems()) {
            final MenuEventDto menuEventDto = new MenuEventDto();
            menuEventDto.setId(orderLineItem.getMenuId());
            publisher.publishEvent(menuEventDto);

            orderLineItemRepository.save(new OrderLineItem(order, menuEventDto.getId(), orderLineItem.getQuantity()));
        }

        return order;
    }

    public List<OrderTable> toOrderTables(List<Long> ids){
        return orderTableRepository.findAllByIdIn(ids);
    }
}
