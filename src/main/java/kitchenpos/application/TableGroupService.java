package kitchenpos.application;

import java.time.LocalDateTime;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.Orders;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final OrderTables orderTables = OrderTables
                .create(tableGroupCreateRequest.getOrderTables());
        final OrderTables savedOrderTables = OrderTables
                .create(orderTableRepository.findAllByIdIn(orderTables.getOrderTableIds()));
        orderTables.validateSameSize(savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create(LocalDateTime.now()));
        savedOrderTables.group(savedTableGroup);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("tableGroupId : " + tableGroupId + "는 존재하지 않는 테이블그룹입니다."));
        final OrderTables orderTables = OrderTables.create(orderTableRepository.findAllByTableGroup(tableGroup));
        for (OrderTable orderTable : orderTables.getOrderTables()) {
            Orders orders = Orders.create(orderRepository.findAllByTableGroup(orderTable));
            orders.validateCompleted();
        }
        orderTables.ungroup();
    }
}
