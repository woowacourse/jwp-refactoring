package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.exception.TableGroupServiceException.ExistsNotCompletionOrderException;
import kitchenpos.application.exception.TableGroupServiceException.NotExistsOrderTableException;
import kitchenpos.application.exception.TableGroupServiceException.NotExistsTableGroupException;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private static final List<OrderStatus> CANNOT_UNGROUP_ORDER_STATUSES = List.of(OrderStatus.COOKING,
            OrderStatus.MEAL);

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final List<OrderTable> orderTables) {
        TableGroup tableGroup = TableGroup.from(orderTables);

        validateTableGroup(tableGroup);

        return tableGroupRepository.save(tableGroup);
    }

    private void validateTableGroup(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderTables.size() != orderTableRepository.countByIdIn(orderTableIds)) {
            throw new NotExistsOrderTableException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotExistsTableGroupException(tableGroupId));

        validateUngroup(tableGroup);

        tableGroup.ungroup();
    }

    private void validateUngroup(final TableGroup tableGroup) {
        // 아래의 검증 로직을 객체 필드로 가져오면 쿼리 조회횟수가 많아지기에 성능이 떨어지기에 서비스에서 이를 처리
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(tableGroup.getOrderTables(),
                CANNOT_UNGROUP_ORDER_STATUSES)) {
            throw new ExistsNotCompletionOrderException();
        }
    }
}
