package kitchenpos.order.domain;

import kitchenpos.common.event.OrderCreationEvent;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderValidator(
            MenuRepository menuRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.menuRepository = menuRepository;
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

}
