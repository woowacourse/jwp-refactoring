package kitchenpos.domain.order;

import java.util.List;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidatorImpl(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateMenuExists(List<OrderLineItem> orderLineItems, List<Long> menuIds) {
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void validateOrderTableEmpty(Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
