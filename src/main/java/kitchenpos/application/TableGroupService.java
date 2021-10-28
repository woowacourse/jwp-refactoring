package kitchenpos.application;

import kitchenpos.domain.Orders;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
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
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.orderTableIds();

        // TODO valid로 옮기기
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        List<OrderTable> all = orderTableRepository.findAll();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup());
        List<OrderTable> orderTables = new ArrayList<>();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.addTableGroup(savedTableGroup);
            savedOrderTable.changeEmpty(false);
            orderTables.add(orderTableRepository.save(savedOrderTable));
        }
        return TableGroupResponse.of(savedTableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 TableGroup이 존재하지 않습니다."));

        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (OrderTable orderTable : orderTables) {
            List<Orders> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
            orders.stream()
                    .filter(Orders::isNotCompleted)
                    .findAny()
                    .ifPresent(order -> {
                        throw new IllegalArgumentException("아직 조리 혹은 식사 중인 주문이 존재합니다.");
                    });
            orderTable.ungroup();
        }
    }
}
