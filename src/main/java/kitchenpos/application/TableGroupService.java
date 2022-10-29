package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableDao orderTableDao;
    private final TableGroupRepository tableGroups;

    public TableGroupService(final OrderTableDao orderTableDao,
                             final TableGroupRepository tableGroups) {
        this.orderTableDao = orderTableDao;
        this.tableGroups = tableGroups;
    }

    @Transactional
    public TableGroup create(final TableGroup request) {
        final List<OrderTable> orderTables = request.getOrderTables();

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        // 중복 테이블이 있으면 안된다 && 테이블이 모두 DB에 존재해야 한다
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        final var tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);

        return tableGroups.add(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final var tableGroup = tableGroups.get(tableGroupId);
        tableGroup.ungroup();

        tableGroups.add(tableGroup);
    }
}
