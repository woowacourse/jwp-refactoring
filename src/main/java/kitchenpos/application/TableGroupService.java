package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.ui.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(TableGroupCreateRequest request) {
        List<OrderTable> orderTables = findOrderTablesByRequest(request.getOrderTableIds());
        TableGroup tableGroup = TableGroup.from();
        tableGroup.addAllOrderTables(orderTables);

        tableGroupRepository.save(tableGroup);

        return tableGroup;
    }

    private List<OrderTable> findOrderTablesByRequest(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException("주문 그룹을 구성하기 위해서는 주문 테이블이 최소 2개 있어야 합니다.");
        }

        return orderTableIds.stream()
                .map(this::findOrderTable)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTable(Long orderTableId) {
        validateOrderTableId(orderTableId);

        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderTableId(Long orderTableId) {
        if (Objects.isNull(orderTableId)) {
            throw new IllegalArgumentException("주문 테이블의 ID 는 존재하지 않을 수 없습니다.");
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = findTableGroup(tableGroupId);
        tableGroup.removeAllOrderTables();
    }

    private TableGroup findTableGroup(Long tableGroupId) {
        validateTableGroupId(tableGroupId);

        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateTableGroupId(Long tableGroupId) {
        if (Objects.isNull(tableGroupId)) {
            throw new IllegalArgumentException("테이블 그룹의 ID 는 존재하지 않을 수 없습니다.");
        }
    }

}
