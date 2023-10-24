package kitchenpos.tablegroup.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.exception.KitchenPosException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(
        TableGroupRepository tableGroupRepository,
        OrderTableRepository orderTableRepository,
        OrderRepository orderRepository
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public TableGroupResponse create(TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds();
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new KitchenPosException("주문 테이블 목록은 2개 이상이어야 합니다.");
        }
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(null, LocalDateTime.now()));
        List<OrderTable> orderTables = findOrderTables(orderTableIds);
        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(tableGroup);
        }
        return TableGroupResponse.of(tableGroup, orderTableIds);
    }

    private List<OrderTable> findOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTables.size() == orderTableIds.size()) {
            return orderTables;
        }
        List<Long> notExistProductIds = getNotExistsOrderTableIds(orderTables, orderTableIds);
        throw new KitchenPosException("존재하지 않는 주문 테이블이 있습니다. notExistOrderTableIds=" + notExistProductIds);
    }

    private List<Long> getNotExistsOrderTableIds(List<OrderTable> orderTables, List<Long> orderTableIds) {
        List<Long> existOrderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(toList());
        List<Long> notExistsOrderTableIds = new ArrayList<>(orderTableIds);
        notExistsOrderTableIds.removeAll(existOrderTableIds);
        return notExistsOrderTableIds;
    }

    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateUngroup(orderTables);
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateUngroup(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(toList());
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        for (Order order : orders) {
            if (!order.isCompletion()) {
                throw new KitchenPosException("계산 완료 상태가 아닌 주문이 있는 테이블은 그룹을 해제할 수 없습니다.");
            }
        }
    }
}
