package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupDto;
import kitchenpos.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderDao orderDao, TableRepository tableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupDto create(TableGroupDto tableGroupDto) {
        List<Long> orderTableIds = tableGroupDto.getOrderTables();
        List<OrderTable> savedOrderOrderTables = tableRepository.findAllByIdIn(orderTableIds);
        validateNotFoundOrderTable(savedOrderOrderTables.size(), orderTableIds.size());
        OrderTables orderTables = OrderTables.forGrouping(savedOrderOrderTables);
        TableGroup savedTableGroup = saveTableGroup(orderTables);
        return new TableGroupDto(savedTableGroup);
    }

    private TableGroup saveTableGroup(OrderTables orderTables) {
        TableGroup tableGroup = new TableGroup(orderTables.getValues());
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        orderTables.group(savedTableGroup);
        savedTableGroup.setOrderTables(orderTables.getValues());
        return savedTableGroup;
    }

    private void validateNotFoundOrderTable(int requestedOrderTableSize, int savedOrderTableSize) {
        if (requestedOrderTableSize != savedOrderTableSize) {
            throw new OrderTableNotFoundException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderOrderTables = tableRepository.findAllByTableGroupId(tableGroupId);
        validateOrderStatus(orderOrderTables);
        OrderTables orderTables = OrderTables.forUnGrouping(orderOrderTables);
        orderTables.ungroup();
        orderTables.setEmpty();
    }

    private void validateOrderStatus(List<OrderTable> orderOrderTables) {
        List<Long> orderTableIds = orderOrderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중이거나 식사중인 테이블이 포함된 Table Group은 그룹 해제 할 수 없습니다.");
        }
    }
}
