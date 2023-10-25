package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.TableGroupCreateRequest;
import kitchenpos.application.response.TableGroupResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreateRequest tableGroupCreateRequest) {
        List<Long> tableIds = tableGroupCreateRequest.getOrderTables();

        List<OrderTable> foundOrderTables = orderTableRepository.findAllByIdIn(tableIds);
        if (foundOrderTables.isEmpty() || foundOrderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹화를 위한 테이블이 2개 이상 필요합니다.");
        }

        for (OrderTable foundOrderTable : foundOrderTables) {
            validateTableStatus(foundOrderTable);
        }

        TableGroup tableGroup = TableGroup.builder()
                .orderTables(foundOrderTables)
                .build();
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(savedTableGroup);
    }

    private void validateTableStatus(OrderTable foundOrderTable) {
        if (!foundOrderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있지 않은 테이블은 그룹화할 수 없습니다.");
        }

        if (foundOrderTable.isGrouped()) {
            throw new IllegalArgumentException("이미 그룹화된 테이블은 그룹화할 수 없습니다.");
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("요리중이거나 식사중인 테이블이 존재합니다.");
        }

        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
        tableGroupRepository.deleteById(tableGroupId);
    }
}
