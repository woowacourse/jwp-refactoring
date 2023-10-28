package kitchenpos.order.domain;

import kitchenpos.exception.InvalidOrderTableToOrder;
import kitchenpos.exception.NotFoundOrDuplicateMenuToOrderExcpetion;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository, final MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validate(final Order order) {
        validateOrderTableAvailability(order.getOrderTableId());
        validateMenuAvailability(order.getOrderLineItems());
    }

    private void validateOrderTableAvailability(final Long OrderTableId) {
        final OrderTable orderTable =
                orderTableRepository.findById(OrderTableId)
                                    .orElseThrow(() -> new NotFoundOrderTableException("해당 주문 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new InvalidOrderTableToOrder("주문 테이블이 비어 있어 주문이 불가능합니다.");
        }
    }

    private void validateMenuAvailability(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                                                 .map(OrderLineItem::getMenuId)
                                                 .collect(Collectors.toList());
        final List<Menu> menus = menuRepository.findAllById(menuIds);

        if (orderLineItems.size() != menus.size()) {
            throw new NotFoundOrDuplicateMenuToOrderExcpetion("존재하지 않는 혹은 중복된 메뉴를 주문 항목으로 설정했습니다.");
        }
    }
}
