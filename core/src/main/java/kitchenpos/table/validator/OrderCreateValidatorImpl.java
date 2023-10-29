package kitchenpos.table.validator;

import kitchenpos.menu.MenuRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderCreateValidator;
import kitchenpos.order.OrderLineItem;
import kitchenpos.table.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderCreateValidatorImpl implements OrderCreateValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderCreateValidatorImpl(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    public void validate(Order order) {
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("한번의 주문에서 중복 메뉴를 주문할 수 없습니다.");
        }

        if (!orderTableRepository.existsById(order.getOrderTableId())) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }

        if (orderTableRepository.getById(order.getOrderTableId()).isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있으면 주문할 수 없습니다.");
        }
    }
}
