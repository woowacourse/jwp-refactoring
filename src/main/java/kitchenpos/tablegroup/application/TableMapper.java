package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest.OrderTableRequest;
import org.springframework.stereotype.Component;
import suppoert.domain.BaseEntity;

@Component
public class TableMapper {

    private final OrderTableRepository orderTableRepository;

    public TableMapper(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public List<Long> toOrderTableIds(final List<OrderTableRequest> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());

        final List<Long> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds)
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());

        validateMatchingSizes(orderTables, savedOrderTables);

        return savedOrderTables;
    }

    public List<Long> toOrderTableIds(final Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId)
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
    }

    private void validateMatchingSizes(
            final List<OrderTableRequest> orderTables,
            final List<Long> savedOrderTables
    ) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }
    }
}
