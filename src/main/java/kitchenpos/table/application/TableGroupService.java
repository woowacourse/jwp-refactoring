package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.ui.request.CreateTableGroupRequest;
import kitchenpos.table.ui.response.TableGroupResponse;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final CreateTableGroupRequest request) {
        final TableGroup tableGroup = new TableGroup();

        final List<OrderTable> orderTables = getOrderTables(tableGroup, request);

        tableGroup.addOrderTables(orderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(savedTableGroup);
    }

    private List<OrderTable> getOrderTables(TableGroup tableGroup, CreateTableGroupRequest request) {
        return request.getOrderTables()
                      .stream()
                      .map(orderTable -> {
                          OrderTable table = orderTableRepository.findById(orderTable.getId())
                                                                 .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 테이블은 그룹으로 지정할 수 없습니다."));
                          table.assigned(tableGroup);
                          return table;
                      })
                      .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                                                          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        tableGroup.ungroup();
    }
}
