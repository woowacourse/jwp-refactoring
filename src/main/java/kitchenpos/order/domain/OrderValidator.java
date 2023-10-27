package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.common.event.OrderCreationEvent;
import kitchenpos.common.event.OrderTableChangeEmptyEvent;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private static final List<OrderStatus> UNCHANGEABLE_STATUS = List.of(OrderStatus.MEAL, OrderStatus.COOKING);

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderValidator(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            ApplicationEventPublisher eventPublisher
    ) {

        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    public void validate(OrderLineItems orderLineItems, Long orderTableId) {
        validateOrderLineItems(orderLineItems);
        eventPublisher.publishEvent(new OrderCreationEvent(orderTableId));
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        boolean hasNotExistingMenu = orderLineItems.getOrderLineItems()
                .stream()
                .noneMatch(orderLineItem ->
                        menuRepository.existsByNameAndPrice(orderLineItem.getName(), orderLineItem.getPrice()));

        if (hasNotExistingMenu) {
            throw new IllegalArgumentException("주문 상품에 존재하지 않는 메뉴가 존재합니다.");
        }
    }

    @EventListener
    public void validateChangeableEmpty(OrderTableChangeEmptyEvent event) {
        boolean isUnableChangeEmpty =
                orderRepository.existsByOrderTableIdAndOrderStatusIn(event.getOrderTableId(), UNCHANGEABLE_STATUS);

        if (isUnableChangeEmpty) {
            throw new IllegalArgumentException("Completion 상태가 아닌 주문 테이블은 주문 가능 여부를 변경할 수 없습니다.");
        }
    }

}
