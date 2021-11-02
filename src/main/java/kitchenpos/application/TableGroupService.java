package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.exception.KitchenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        List<OrderTable> orderTables = request.getOrderTableIds().stream()
            .map(orderTableId -> orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new KitchenException("존재하지 않는 테이블이 포함되어 있습니다.")))
            .collect(Collectors.toList());

        TableGroup tableGroup = new TableGroup(orderTables);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        savedTableGroup.updateOrderTablesTableGroup();
        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository
            .findAllByTableGroupId(tableGroupId);

        if (orderTables.stream().anyMatch(OrderTable::hasCookingOrMeal)) {
            throw new KitchenException("완료되지 않은 주문 테이블이 존재합니다.");
        }

        orderTables.forEach(OrderTable::ungroup);
    }
}
