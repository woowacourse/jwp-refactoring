package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> savedOrderTables = findOrderTables(request);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    private List<OrderTable> findOrderTables(TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
        return savedOrderTables;
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
        tableGroup.ungroupOrderTables();
    }
}
