package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.order.OrderStatus;
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
        List<Long> tableIds = tableGroupRequest.getOrderTableIds();
        List<Table> tables = tableRepository.findAllByIdIn(tableIds);
        TableGroup tableGroup = TableGroupAssembler.assemble(tableGroupRequest, tables);

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<Table> tables = tableRepository.findAllByTableGroup_Id(tableGroupId);

        if (orderRepository.existsByTableInAndOrderStatusIn(tables,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        for (final Table table : tables) {
            table.setTableGroup(null);
            table.setEmpty(false);
            tableRepository.save(table);
        }
    }
}
