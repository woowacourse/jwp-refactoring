package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableIdRequest;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTables().stream().map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());
        validateEmptyOrderTables(orderTableIds);
        final List<OrderTable> savedOrderTables = orderTableRepository.getAllById(orderTableIds);
        final TableGroup tableGroup = tableGroupRepository.save(TableGroup.forSave(savedOrderTables));
        final OrderTables orderTables = new OrderTables(savedOrderTables);
        orderTables.registerTableGroup(tableGroup);

        return TableGroupResponse.from(tableGroup);
    }

    private void validateEmptyOrderTables(final List<Long> orderTableIds) {
        if (orderTableIds.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 없습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        final OrderTables orderTables = new OrderTables(tableGroup.getOrderTables());

        orderTables.ungroup();
    }
}
