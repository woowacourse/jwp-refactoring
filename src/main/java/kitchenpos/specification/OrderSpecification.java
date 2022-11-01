package kitchenpos.specification;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.repository.order.TableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderSpecification {

    private final TableRepository tableRepository;

    private final MenuRepository menuRepository;

    public OrderSpecification(TableRepository tableRepository,
                              MenuRepository menuRepository) {
        this.tableRepository = tableRepository;
        this.menuRepository = menuRepository;
    }

    public void validateCreate(OrderRequest request) {

        final List<OrderLineItemRequest> orderLineItems = request.getOrderLineItems();

        if (isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어있을 수 없습니다.");
        }

        List<Long> menuIds = extractMenuIds(orderLineItems);

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하는 메뉴에 대해서만 주문이 가능합니다.");
        }
    }

    private List<Long> extractMenuIds(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public void validateChangeOrderStatus(Long orderId, Order order) {

        if (Objects.equals(COMPLETION, order.getOrderStatus())) {
            throw new IllegalArgumentException("이미 계산 완료된 주문의 상태를 변경할 수 없습니다.");
        }
    }
}
