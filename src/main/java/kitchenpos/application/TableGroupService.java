package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.OrderTablesRequest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.exception.OrderTableException.NotExistsOrderTableException;
import kitchenpos.domain.exception.TableGroupException.ExistsNotCompletionOrderException;
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
    public TableGroup create(final OrderTablesRequest orderTablesRequest) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTablesRequest.getOrderTableIds());
        validateTableGroup(orderTables, orderTablesRequest.getOrderTableIds());
        return tableGroupRepository.save(TableGroup.from(orderTables));
    }

    private void validateTableGroup(final List<OrderTable> orderTables, final List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new NotExistsOrderTableException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);

        validateUngroup(tableGroup);

        tableGroup.ungroup();
    }

    private void validateUngroup(final TableGroup tableGroup) {
        // 아래의 검증 로직을 객체 필드로 가져오면 쿼리 조회횟수가 많아져 성능이 떨어지기에 서비스에서 이를 처리
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                tableGroup.getOrderTables(), CANNOT_UNGROUP_ORDER_STATUSES)) {
            throw new ExistsNotCompletionOrderException();
        }
    }
}
