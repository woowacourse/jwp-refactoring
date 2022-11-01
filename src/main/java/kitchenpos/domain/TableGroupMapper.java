package kitchenpos.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupMapper {
    private static final int MINIMUM_TABLE_GROUP_SIZE = 2;

    private final OrderTableRepository orderTableRepository;

    public TableGroupMapper(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public TableGroup mapFrom(final TableGroupCreateRequest request) {
        final var requestIds = request.ids();
        validateRequestIdsSize(requestIds);

        final var orderTables = orderTableRepository.findAllByIdInAndEmptyIsTrueAndTableGroupIdIsNull(requestIds);
        final var orderTableIds = extractOrderTableIds(orderTables);
        validateFoundTableSize(requestIds, orderTableIds);

        return new TableGroup(orderTableIds);
    }

    private void validateRequestIdsSize(final Set<Long> requestIds) {
        if (CollectionUtils.isEmpty(requestIds) || requestIds.size() < MINIMUM_TABLE_GROUP_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> extractOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void validateFoundTableSize(final Set<Long> orderTableIds, final List<Long> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }
}
