package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);

        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("유효하지 않은 OrderTable을 포함하고 있습니다.");
        }

        final TableGroup tableGroup = new TableGroup(orderTables);
        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByGroupId(tableGroupId);

        // TODO: 2023/10/17 Order repository 만들고 작업
//
//        final List<Long> orderTableIds = orderTables.stream()
//                .map(OrderTable::getId)
//                .collect(Collectors.toList());
//        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
//                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
//            throw new IllegalArgumentException("그룹해제 할수 없는 상태의 테이블을 포함하고 있습니다.");
//        }
        orderTables.forEach(OrderTable::ungroup);
    }
}
