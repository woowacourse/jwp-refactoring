package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.DomainService;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;

@DomainService
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final MenuRepository menuRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        validateOrderTableEmpty(orderTableId);
        validateMenus(orderLineItems);
    }

    private void validateOrderTableEmpty(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 OrderTable 입니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalStateException("해당 OrderTable이 empty 상태 입니다.");
        }
    }

    private void validateMenus(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        if (menuIds.size() != menuRepository.countMenuByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 Menu가 존재합니다.");
        }
    }
}
