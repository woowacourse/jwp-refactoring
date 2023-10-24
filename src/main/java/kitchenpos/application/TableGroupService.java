package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateTableGroupCommand;
import kitchenpos.application.dto.domain.TableGroupDto;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupDto create(final CreateTableGroupCommand command) {
        final List<Long> orderTableIds = command.getOrderTableIds();
        final List<OrderTable> foundOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (invalidOrderTable(orderTableIds, foundOrderTables)) {
            throw new IllegalArgumentException("유효하지 않은 테이블이 있습니다.");
        }

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.changeOrderTables(foundOrderTables);

        return TableGroupDto.from(tableGroup);
    }

    private boolean invalidOrderTable(final List<Long> orderTableIds, final List<OrderTable> foundOrderTables) {
        return orderTableIds.size() != foundOrderTables.size();
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

}
