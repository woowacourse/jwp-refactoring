package kitchenpos.order.specification;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.presentation.dto.request.OrderTableRequest;
import kitchenpos.order.presentation.dto.request.TableGroupRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.TableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupSpecification {

    private final TableRepository tableRepository;
    private final OrderRepository orderRepository;

    public TableGroupSpecification(TableRepository tableRepository, OrderRepository orderRepository) {
        this.tableRepository = tableRepository;
        this.orderRepository = orderRepository;
    }

    public void validateCreate(TableGroupRequest request, List<OrderTable> savedTables) {

        final List<OrderTableRequest> requestOrderTables = request.getOrderTables();

        if (CollectionUtils.isEmpty(requestOrderTables) || requestOrderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블이 2개이상이어야 그룹화가 가능합니다.");
        }

        if (requestOrderTables.size() != savedTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블에 대해 그룹화를 할 수 없습니다.");
        }

        for (final OrderTable orderTable : savedTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException("주문 테이블이 비어있지 않거나 이미 그룹화가 되어 있을 경우 그룹화를 할 수 없습니다.");
            }
        }
    }

    public void validateUngroup(List<OrderTable> orderTables) {

        List<Long> orderTableIds = orderTableIds(orderTables);

        if (orderRepository.existsByIdInAndStatusIn(orderTableIds, List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException("조리 중이거나 식사 중인 주문 테이블이 있는 경우 그룹을 해제할 수 없습니다.");
        }
    }

    private List<Long> orderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
