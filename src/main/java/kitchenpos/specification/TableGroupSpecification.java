package kitchenpos.specification;

import java.util.List;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.repository.order.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupSpecification {

    private final OrderTableRepository orderTableRepository;

    public TableGroupSpecification(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreate(TableGroupRequest request, List<OrderTable> savedOrderTables) {

        final List<OrderTableRequest> requestOrderTables = request.getOrderTables();

        if (CollectionUtils.isEmpty(requestOrderTables) || requestOrderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블이 2개이상이어야 그룹화가 가능합니다.");
        }

        if (requestOrderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블에 대해 그룹화를 할 수 없습니다.");
        }
    }
}
