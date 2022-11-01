package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTableRepository;
import kitchenpos.domain.ordertable.TableGroup;
import kitchenpos.domain.ordertable.TableGroupRepository;
import kitchenpos.dto.request.IdRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderDao orderDao;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository,
                             final OrderDao orderDao) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderDao = orderDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<IdRequest> orderTableIdRequests = request.getOrderTables();
        final List<OrderTable> savedOrderTables = orderTableIdRequests.stream()
                .map(this::getOrderTableFrom)
                .collect(Collectors.toList());

        final TableGroup tableGroup = TableGroup.ofNew(savedOrderTables);

        tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        final List<Long> orderTableIds = tableGroup.getOrderTableIds();
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        tableGroup.ungroup();
    }

    private OrderTable getOrderTableFrom(final IdRequest request) {
        return orderTableRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
