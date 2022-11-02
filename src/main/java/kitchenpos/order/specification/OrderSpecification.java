package kitchenpos.order.specification;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import java.util.Objects;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.presentation.dto.request.OrderLineItemRequest;
import kitchenpos.order.presentation.dto.request.OrderRequest;
import kitchenpos.order.repository.TableRepository;
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

        List<OrderLineItemRequest> requestOrderLineItems = request.getOrderLineItems();

        if (isEmpty(requestOrderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어있을 수 없습니다.");
        }

        if (!tableRepository.existsById(request.getOrderTableId())) {
            throw new IllegalArgumentException("존재하는 주문 테이블에 대해서만 주문이 가능합니다.");
        }

        if (requestOrderLineItems.size() != menuRepository.countByIdIn(request.menuIds())) {
            throw new IllegalArgumentException("존재하는 메뉴에 대해서만 주문이 가능합니다.");
        }
    }

    public void validateChangeOrderStatus(Order order) {

        if (Objects.equals(COMPLETION, order.getOrderStatus())) {
            throw new IllegalArgumentException("이미 계산 완료된 주문의 상태를 변경할 수 없습니다.");
        }
    }
}
