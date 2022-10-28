package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.CustomErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.repository.TableGroupRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.ui.dto.TableGroupCreateRequest;
import kitchenpos.order.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
