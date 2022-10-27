package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.request.TableGroupCommand;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TableGroupResponse create(final TableGroupCommand tableGroupCommand) {
        List<Long> orderTableIds = tableGroupCommand.orderTableIds();
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateExistOrderTable(orderTableIds, orderTables);

        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), orderTables));
        return TableGroupResponse.from(tableGroup);
    }

    private void validateExistOrderTable(final List<Long> orderTableIds, final List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .toList();

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문 그룹을 분리할 수 없습니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            orderTableRepository.save(orderTable);
        }
    }
}
