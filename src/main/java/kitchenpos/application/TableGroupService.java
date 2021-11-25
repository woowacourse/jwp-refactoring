package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.tablegroup.TableGroupRequestDto;
import kitchenpos.application.dto.response.table.TableGroupResponseDto;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponseDto create(TableGroupRequestDto tableGroupRequestDto) {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = tableGroupRequestDto.getOrderTables()
            .stream()
            .map(orderTable -> convert(orderTable.getId(), tableGroup))
            .collect(Collectors.toList());
        tableGroup.updateOrderTables(orderTables);
        return TableGroupResponseDto.from(tableGroupRepository.save(tableGroup));
    }

    private OrderTable convert(Long orderTableId, TableGroup tableGroup) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("요청한 OrderTable이 저장되어있지 않습니다."));
        orderTable.toTableGroup(tableGroup);
        return orderTable;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("요청한 TableGroup이 존재하지 않습니다."));
        tableGroup.ungroup();
    }
}
