package kitchenpos.ordertable.application;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.domain.ValidResult;
import kitchenpos.common.exception.KitchenPosException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableUngroupValidator;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.event.TableGroupGroupEvent;
import kitchenpos.tablegroup.event.TableGroupUngroupEvent;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableEventListener {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final List<OrderTableUngroupValidator> validators;

    public OrderTableEventListener(
        OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository,
        List<OrderTableUngroupValidator> validators
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.validators = validators;
    }

    @EventListener(TableGroupGroupEvent.class)
    public void group(TableGroupGroupEvent event) {
        TableGroup tableGroup = findTableGroup(event.getTableGroupId());
        List<OrderTable> orderTables = findOrderTables(event.getOrderTableIds());
        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(tableGroup);
        }
    }

    private TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new KitchenPosException("해당 테이블 그룹이 없습니다. tableGroupId=" + tableGroupId));
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

    @EventListener(TableGroupUngroupEvent.class)
    public void ungroup(TableGroupUngroupEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        validateUngroup(orderTables);
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateUngroup(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(toList());
        validators.forEach(validator -> {
            ValidResult result = validator.validate(orderTableIds);
            result.throwIfFailure(KitchenPosException::new);
        });
    }
}
