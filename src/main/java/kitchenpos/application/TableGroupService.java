package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderRepository;
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
            throw new IllegalArgumentException();
        }

        return orderTableIds.stream()
                .map(this::findOrderTable)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = findTableGroup(tableGroupId);
        tableGroup.removeAllOrderTables();
    }

    public TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

}
