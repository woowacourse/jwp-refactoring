package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableDao;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.domain.table.TableGroupValidator;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupRequest.TableGroupInnerOrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupValidator tableGroupValidator;
    private final OrderTableDao orderTableDao;
    private final OrderTableRepository orderTables;
    private final TableGroupRepository tableGroups;
    private final OrderRepository orders;

    public TableGroupService(final TableGroupValidator tableGroupValidator,
                             final OrderTableDao orderTableDao,
                             final OrderTableRepository orderTables,
                             final TableGroupRepository tableGroups,
                             final OrderRepository orders) {
        this.tableGroupValidator = tableGroupValidator;
        this.orderTables = orderTables;
        this.orderTableDao = orderTableDao;
        this.tableGroups = tableGroups;
        this.orders = orders;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = mapToOrderTables(request.getOrderTables());
        final var tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        return tableGroups.add(tableGroup);
    }

    // - 비즈니스 오류 (x)
    // - 논리적 오류 (o)
    private List<OrderTable> mapToOrderTables(final List<TableGroupInnerOrderTable> requests) {
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(collectIds(requests));
        if (requests.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
        return savedOrderTables;
    }

    private List<Long> collectIds(final List<TableGroupInnerOrderTable> requests) {
        return requests.stream()
                .map(TableGroupInnerOrderTable::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final var tableGroup = tableGroups.get(tableGroupId);
        tableGroup.ungroup(tableGroupValidator);

        orderTables.addAll(tableGroup.getOrderTables());
    }
}
