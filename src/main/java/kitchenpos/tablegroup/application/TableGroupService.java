package kitchenpos.tablegroup.application;

import kitchenpos.exception.NotFoundTableGroupException;
import kitchenpos.tablegroup.domain.OrderTableManager;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.tablegroup.ui.dto.OrderTableDto;
import kitchenpos.tablegroup.ui.dto.TableGroupRequest;
import kitchenpos.tablegroup.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableManager orderTableManager;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final OrderTableManager orderTableManager
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableManager = orderTableManager;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = tableGroupRequest.toEntity(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        final List<Long> orderTableIds = convertToIds(tableGroupRequest.getOrderTables());
        orderTableManager.addOrderTables(tableGroup, orderTableIds);

        return TableGroupResponse.from(savedTableGroup);
    }

    private List<Long> convertToIds(final List<OrderTableDto> orderTableDtos) {
        return orderTableDtos.stream()
                             .map(OrderTableDto::getId)
                             .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup =
                tableGroupRepository.findById(tableGroupId)
                                    .orElseThrow(() -> new NotFoundTableGroupException("해당 단체 지정이 존재하지 않습니다."));
        orderTableManager.ungroup(tableGroup);
    }
}
