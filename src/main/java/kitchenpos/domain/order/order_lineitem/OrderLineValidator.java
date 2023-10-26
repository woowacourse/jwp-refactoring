package kitchenpos.domain.order.order_lineitem;

import kitchenpos.exception.OrderException;
import kitchenpos.repositroy.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderLineValidator {

    private final MenuRepository menuRepository;

    public OrderLineValidator(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(final OrderLineItem orderLineItem) {
        if (!menuRepository.existsById(orderLineItem.getMenu().getId())) {
            throw new OrderException.NoMenuException();
        }
    }
}
