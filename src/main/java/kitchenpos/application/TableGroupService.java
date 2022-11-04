package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.collection.OrderTables;
import kitchenpos.domain.collection.Orders;
import kitchenpos.domain.entity.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.jpa.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.ui.jpa.dto.tablegroup.TableGroupCreateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private TableGroupRepository tableGroupRepository;
    private OrderTableRepository orderTableRepository;
    private TableServiceAssistant tableServiceAssistant;
    private OrderService orderService;

    public TableGroupService(TableGroupRepository tableGroupRepository,
                             OrderTableRepository orderTableRepository,
                             TableServiceAssistant tableServiceAssistant,
                             OrderService orderService) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableServiceAssistant = tableServiceAssistant;
        this.orderService = orderService;
    }

    @Transactional
    public TableGroupCreateResponse create(TableGroupCreateRequest tableGroupCreateRequest) {
        OrderTables orderTables = tableServiceAssistant.findOrderTables(tableGroupCreateRequest.getOrderTableIds());
        List<Long> orderTableIds = orderTables.getOrderTableIds();
        OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllById(orderTableIds));
        if (!savedOrderTables.isAbleToGroup()) {
            throw new IllegalArgumentException();
        }

        if (!savedOrderTables.isReadyToGroup(orderTables)) {
            throw new IllegalArgumentException();
        }

        TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);
        savedOrderTables.group(tableGroup);


        return new TableGroupCreateResponse(tableGroup.getId(), tableGroup.getCreatedDate(), savedOrderTables.getOrderTableIds());
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        OrderTables orderTables = tableServiceAssistant.findTablesInGroup(tableGroupId);
        Orders orders = orderService.findOrdersInOrderTables(orderTables);
        if (!orders.isAllCompleted()) {
            throw new IllegalStateException("모든 주문이 완료되지 않았습니다.");
        }

        orderTables.ungroup();
    }
}
