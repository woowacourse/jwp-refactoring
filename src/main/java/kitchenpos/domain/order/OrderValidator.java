package kitchenpos.domain.order;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validate(Order order) {
        if (!orderTableRepository.existsById(order.getOrderTableId())) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }

        List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 존재하지 않습니다.");
        }

        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴는 등록할 수 없습니다.");
        }
    }
}
