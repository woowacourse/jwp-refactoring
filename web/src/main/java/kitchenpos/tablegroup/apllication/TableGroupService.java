package kitchenpos.tablegroup.apllication;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.response.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository, TableGroupValidator tableGroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    public TableGroupResponse create(final TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds();
        final List<OrderTable> savedOrderTables = findOrderTables(orderTableIds);
        TableGroup newTableGroup = tableGroupValidator.create(savedOrderTables);
        return TableGroupResponse.of(tableGroupRepository.save(newTableGroup));
    }

    private List<OrderTable> findOrderTables(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재 하지 않는 주문 테이블이 있습니다.");
        }
        return savedOrderTables;
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroup(tableGroupId);
        tableGroupValidator.ungroup(tableGroup);
    }

    private TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹입나다"));
    }
}
