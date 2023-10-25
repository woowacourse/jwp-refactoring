package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroup create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        savedTableGroup.addOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    public void ungroup(final Long tableGroupId) {
        Objects.requireNonNull(tableGroupId);
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 테이블 단체를 삭제할 수 없습니다."));

        tableGroup.unGroup();
    }
}
