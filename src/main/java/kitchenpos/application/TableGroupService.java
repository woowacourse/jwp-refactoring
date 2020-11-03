package kitchenpos.application;

import static kitchenpos.domain.order.OrderStatus.NOT_COMPLETION_ORDER_STATUSES;

import java.util.List;
import kitchenpos.domain.table.Table;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.repository.TableRepository;
import kitchenpos.ui.dto.TableGroupAssembler;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
        final TableRepository tableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Table> tables = getTables(tableGroupRequest);
        TableGroup tableGroup = TableGroupAssembler.assemble(tables);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.of(savedTableGroup);
    }

    private List<Table> getTables(TableGroupRequest tableGroupRequest) {
        List<Long> tableIds = tableGroupRequest.getOrderTableIds();
        List<Table> tables = tableRepository.findAllByIdIn(tableIds);
        if (tableIds.size() != tables.size()) {
            throw new IllegalArgumentException("올바르지 않은 테이블 ID가 포함되어 있습니다.");
        }
        return tables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<Table> tables = tableRepository.findAllByTableGroup_Id(tableGroupId);
        validateOrderCompletion(tables);

        for (final Table table : tables) {
            table.unGroup();
        }
    }

    private void validateOrderCompletion(List<Table> tables) {
        if (orderRepository
            .existsByTableInAndOrderStatusIn(tables, NOT_COMPLETION_ORDER_STATUSES)) {
            throw new IllegalArgumentException("진행중인 주문건이 있습니다.");
        }
    }
}
