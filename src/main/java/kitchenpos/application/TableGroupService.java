package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table_group.TableGroup;
import kitchenpos.exception.TableGroupException;
import kitchenpos.repositroy.OrderTableRepository;
import kitchenpos.repositroy.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public TableGroupResponse create(final TableGroupCreateRequest request) {

        final List<OrderTable> candidates = orderTableRepository.findAllById(request.getOrderTables());
        validateCandidates(request.getOrderTables(), candidates);
        final TableGroup tableGroup = new TableGroup(candidates);

        return TableGroupResponse.from(tableGroup);
    }

    private void validateCandidates(final List<Long> requestOrderTables, final List<OrderTable> candidates) {
        if (requestOrderTables.size() != candidates.size()) {
            throw new TableGroupException.NoOrderTableException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        tableGroup.upGroup();
        tableGroupRepository.delete(tableGroup);
    }
}
