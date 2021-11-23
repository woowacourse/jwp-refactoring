package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.*;
import kitchenpos.dto.request.CreateTableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository,
            final TableValidator tableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(final CreateTableGroupRequest request) {
        final TableGroup tableGroup = new TableGroup();
        final List<OrderTable> orderTables = getOrderTables(tableGroup, request);
        tableGroup.addOrderTables(orderTables);
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    private List<OrderTable> getOrderTables(TableGroup tableGroup, CreateTableGroupRequest request) {
        return request.getOrderTables()
                      .stream()
                      .map(orderTable -> {
                          final OrderTable table = orderTableRepository.findById(orderTable.getId())
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

        tableGroup.ungroup(tableValidator);
    }
}
