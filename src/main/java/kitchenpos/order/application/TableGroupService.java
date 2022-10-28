package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.CustomErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.repository.TableGroupRepository;
import kitchenpos.order.repository.TableRepository;
import kitchenpos.order.ui.dto.TableGroupCreateRequest;
import kitchenpos.order.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableRepository tableRepository;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final TableRepository tableRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = request.getOrderTableIds().stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());
        return TableGroupResponse.from(
                tableGroupRepository.save(new TableGroup(orderTables, LocalDateTime.now()))
        );
    }

    private OrderTable findOrderTableById(final Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CustomErrorCode.TABLE_NOT_FOUND_ERROR));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundException(CustomErrorCode.TABLE_GROUP_NOT_FOUND_ERROR));
        tableGroup.ungroup();
    }
}
