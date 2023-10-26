package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    private final MenuRepository menuRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository, final MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validate(final Long orderTableId, final OrderLineItems orderLineItems) {
        validateTable(orderTableId);
        validateMenus(orderLineItems);
    }

    private void validateTable(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비었어요");
        }
    }

    private void validateMenus(final OrderLineItems orderLineItems) {
        final List<Long> menuIds = orderLineItems.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        final List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new IllegalArgumentException("없는 메뉴에요");
        }
    }
}
