package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupDto;
import kitchenpos.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, TableRepository tableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
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
        OrderTables orderTables = OrderTables.forUnGrouping(orderOrderTables);
        orderTables.ungroup();
        orderTables.setEmpty();
    }
}
