package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.tableGroup.TableGroupCreateRequest;
import kitchenpos.dto.tableGroup.TableGroupCreateResponse;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupCreateResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        TableGroup tableGroup = tableGroupCreateRequest.toEntity();
        OrderTables orderTables = tableGroup.getOrderTables();

        validOrderTablesIsNotEmptyAndSizeIsTwoOrMore(orderTables);

        List<Long> orderTableIds = orderTables.extractOrderTableIds();

        OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));

        validOrderTablesAreExist(orderTables, savedOrderTables);
        validOrderTableIsEmptyAndTableGroupIdIsNull(savedOrderTables.getOrderTables());

        tableGroup.setCreatedDateNow();

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        associateOrderTablesAndTableGroup(savedOrderTables, savedTableGroup);

        return new TableGroupCreateResponse(savedTableGroup);
    }

    private void validOrderTablesIsNotEmptyAndSizeIsTwoOrMore(OrderTables orderTables) {
        if (orderTables.getOrderTables() == null || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validOrderTablesAreExist(OrderTables orderTables, OrderTables savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validOrderTableIsEmptyAndTableGroupIdIsNull(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void associateOrderTablesAndTableGroup(OrderTables savedOrderTables, TableGroup savedTableGroup) {
        savedOrderTables.setTableGroupId(savedTableGroup.getId());
        savedOrderTables.setEmpty(false);
        orderTableRepository.saveAll(savedOrderTables.getOrderTables());
        savedTableGroup.setOrderTables(savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));

        final List<Long> orderTableIds = orderTables.extractOrderTableIds();

        validOrderStatusIsNotCookingOrMeal(orderTableIds);

        orderTables.setTableGroupId(null);
        orderTables.setEmpty(false);
        orderTableRepository.saveAll(orderTables.getOrderTables());
    }

    private void validOrderStatusIsNotCookingOrMeal(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
