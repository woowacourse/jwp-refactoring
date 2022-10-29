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

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(request.getOrderTables());

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        tableGroup.changeOrderTablesOfGroups(orderTables);

        return TableGroupResponse.createResponse(tableGroup);
    }

    @Transactional
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
