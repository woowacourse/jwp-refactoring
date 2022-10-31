package kitchenpos.application;

import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao, final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = findOrderTablesIdIn(request.getOrderTables());

        final TableGroup tableGroup = tableGroupRepository.save(
            new TableGroup(LocalDateTime.now(), new OrderTables(orderTables)));

        return TableGroupResponse.createResponse(tableGroup);
    }

    private List<OrderTable> findOrderTablesIdIn(final List<Long> orderTablesIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTablesIds);
        if (orderTables.size() != orderTablesIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블 입니다.");
        }

        return orderTables;
    }

    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));

        final List<Long> orderTableIds = orderTables.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        orderTables.changeUngroups();
    }
}
