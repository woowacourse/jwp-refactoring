package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.OrderValidator;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderValidator orderValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderValidator orderValidator, final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderValidator = orderValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final TableGroup tableGroup = TableGroup.forSave();
        final OrderTables orderTables = getOrderTables(request.getOrderTables());
        tableGroupRepository.save(tableGroup);
        orderTables.join(tableGroup.getId());
        return TableGroupResponse.from(tableGroup, orderTables);
    }

    private OrderTables getOrderTables(final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("실제 존재하지 않은 주문 테이블이 포함되어 있습니다.");
        }
        return new OrderTables(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹입니다."));
        final OrderTables orderTables = new OrderTables(orderTableRepository.findByTableGroupId(tableGroupId));
        orderValidator.validateOrderStatusInCookingOrMeal(orderTables);
        orderTables.ungroup();
    }
}
