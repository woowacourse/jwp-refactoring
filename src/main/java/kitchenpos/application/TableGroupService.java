package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.ui.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderDao orderDao,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds.getOrderTableIds());
        validateTableGroupInput(orderTableIds.getOrderTableIds(), orderTables);
        validateOrderTablesStatus(orderTables);
        orderTables.forEach(OrderTable::changeToOccupied);
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        return TableGroupResponse.from(savedTableGroup);
    }

    private void validateTableGroupInput(final List<Long> idsInput, final List<OrderTable> savedOrderTables) {
        if (CollectionUtils.isEmpty(idsInput) || idsInput.size() < 2) {
            throw new IllegalArgumentException();
        }
        if (idsInput.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesStatus(final List<OrderTable> orderTables) {
        final long validatedCount = orderTables.stream()
                .filter(OrderTable::isEmpty)
                .filter(orderTable -> Objects.isNull(orderTable.getTableGroup()))
                .count();
        if (validatedCount != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findMandatoryById(tableGroupId);
        final List<OrderTable> orderTables = orderTableRepository.findByTableGroup(tableGroup);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        // TODO: orderRepository 로 변경
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        orderTables.forEach(OrderTable::ungroup);
        orderTableRepository.saveAll(orderTables);
    }
}
