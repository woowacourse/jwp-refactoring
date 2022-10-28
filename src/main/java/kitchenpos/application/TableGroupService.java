package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.TableGroupCommand;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.domain.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(OrderDao orderDao, OrderTableDao orderTableDao,
                             OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository,
                             TableGroupValidator tableGroupValidator) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroup create(final TableGroupCommand tableGroupCommand) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIn(tableGroupCommand.getOrderTableId());
        tableGroupValidator.validateCreate(tableGroupCommand, orderTables);
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        addOrderTable(orderTables, savedTableGroup);
        return savedTableGroup;
    }

    private void addOrderTable(List<OrderTable> orderTables, TableGroup savedTableGroup) {
        Long tableGroupId = savedTableGroup.getId();
        for (OrderTable savedOrderTable : orderTables) {
            savedOrderTable.cleanTableAndFillTableGroup(tableGroupId);
        }
        savedTableGroup.setOrderTables(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("계산이 완료되어야 합니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            orderTableDao.save(orderTable);
        }
    }
}
