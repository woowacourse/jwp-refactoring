package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = toOrderTables(tableGroupRequest.getOrderTables());
        validateMoreThanTwoOrderTable(orderTables);
        validateIsEmptyAndNotContainTableGroup(orderTables);
        validateNotDuplicatedOrNotExist(orderTables, tableGroupRequest.getOrderTables());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        orderTables.forEach(orderTable -> orderTable.registerTableGroup(tableGroup));

        return tableGroupRepository.save(tableGroup);
    }

    private void validateNotDuplicatedOrNotExist(List<OrderTable> orderTables, List<OrderTableRequest> requests) {
        if (orderTables.size() != requests.size()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> toOrderTables(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
                .map(orderTableRequest -> orderTableRepository.findById(orderTableRequest.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        final OrderTables orderTables = new OrderTables(
                        orderTableRepository.findAllByTableGroupId(tableGroup.getId())
        );

        if (orderTables.hasOrderTableWhichStatusIsCookingOrMeal()) {
            throw new IllegalArgumentException();
        }

        orderTables.unregisterTableGroup();
    }

    private void validateMoreThanTwoOrderTable(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIsEmptyAndNotContainTableGroup(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateOrderTableIsEmptyAndNotContainTableGroup(orderTable);
        }
    }

    private void validateOrderTableIsEmptyAndNotContainTableGroup(OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }
}
