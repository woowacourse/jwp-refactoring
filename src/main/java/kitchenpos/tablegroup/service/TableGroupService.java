package kitchenpos.tablegroup.service;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.response.TableGroupResponse;
import kitchenpos.tablegroup.exception.TableGroupNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = getOrderTables(request);
        tableGroup.grouping(orderTables);
        tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> getOrderTables(final TableGroupCreateRequest request) {
        return request.getOrderTables()
                .stream()
                .map(orderTableRepository::getById)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);
        validatePossibleUngrouping(tableGroup);
        tableGroup.ungrouping();
    }

    private void validatePossibleUngrouping(final TableGroup tableGroup) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                tableGroup.getOrderTables(), List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException("조리중이거나 식사 중인 테이블이 있습니다.");
        }
    }
}
