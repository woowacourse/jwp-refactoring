package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.NotFoundException;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = mapToOrderTables(request.getOrderTableIds());
        return TableGroupResponse.from(
                tableGroupRepository.save(new TableGroup(orderTables, LocalDateTime.now()))
        );
    }

    private List<OrderTable> mapToOrderTables(final List<Long> orderTableIds) {
        return orderTableIds.stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(final Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CustomErrorCode.TABLE_NOT_FOUND_ERROR));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundException(CustomErrorCode.TABLE_GROUP_NOT_FOUND_ERROR));
        tableGroup.ungroup();
    }
}
