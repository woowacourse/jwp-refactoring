package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository,
                             final OrderRepository orderRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        List<OrderTable> orderTables = getOrderTables(request);
        validate(request, orderTables);

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.builder()
                .createdDate(LocalDateTime.now())
                .orderTables(new OrderTables(orderTables))
                .build());

        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        checkOrderStatus(orderTables);
        orderTables.ungroup();
    }

    private List<OrderTable> getOrderTables(final TableGroupCreateRequest request) {
        List<OrderTableRequest> orderTableRequests = request.getOrderTables();
        List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        return orderTableRepository.findAllById(orderTableIds);
    }

    private void validate(final TableGroupCreateRequest request, final List<OrderTable> orderTables) {
        if (request.getOrderTables().size() != orderTables.size()) {
            throw new IllegalArgumentException("주문 테이블은 중복되거나 존재하지 않을 수 없습니다.");
        }
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException("주문 테이블은 활성화되어 있거나 이미 단체 지정되어 있을 수 없습니다.");
            }
        }
    }

    private void checkOrderStatus(final OrderTables orderTables) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTables.getIds(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("아직 조리중이거나 식사중인 주문 테이블이 포함되어 있습니다.");
        }
    }
}
