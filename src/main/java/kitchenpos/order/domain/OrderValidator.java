package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.infrastructure.OrderTableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableValidator orderTableValidator;

    public OrderValidator(MenuRepository menuRepository, OrderTableValidator orderTableValidator) {
        this.menuRepository = menuRepository;
        this.orderTableValidator = orderTableValidator;
    }

    public void validate(Long orderTableId, OrderLineItems orderLineItems) {
        orderTableValidator.validateEmptyTable(orderTableId);

        if (orderLineItems.size() != menuRepository.countByIdIn(orderLineItems.getMenuIds())) {
            throw new IllegalArgumentException("주문항목의 수와 메뉴의 수가 일치하지 않습니다.");
        }
    }
}
