package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.application.dto.tablegroup.TableGroupCreateResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.persistence.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupCreateResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = request.getOrderTables().stream()
                .map(orderTableRequest -> new OrderTable(orderTableRequest.getId()))
                .collect(toList());

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }

        final TableGroup tableGroup = new TableGroup(savedOrderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupCreateResponse.of(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(
                tableGroupRepository.findById(tableGroupId).get()
        );

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }
}
