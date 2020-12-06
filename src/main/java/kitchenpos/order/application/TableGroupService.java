package kitchenpos.order.application;

import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.request.TableGroupCreateRequest;
import kitchenpos.order.dto.response.TableGroupResponse;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByIdIn(request.getOrderTableIds()));

        if (!orderTables.isNotSameSizeWith(request.getOrderTableIds().size())) {
            throw new IllegalArgumentException("존재하지 않는 테이블을 단체로 지정할 수 없습니다.");
        }

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        orderTables.groupBy(tableGroup);

        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        orderTables.ungroup();
    }
}
