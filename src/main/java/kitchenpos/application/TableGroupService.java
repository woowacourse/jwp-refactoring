package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableGroupRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final OrderTableGroupRequest orderTableGroupRequest) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(
            orderTableGroupRequest.getTableGroupIds());

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        validTableGroup(tableGroup);

        tableGroup.ungroup();
    }

    private void validTableGroup(TableGroup tableGroup) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
            tableGroup.getOrderTables(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 완료되지 않아서 그룹을 해제할 수 없습니다.");
        }
    }
}
