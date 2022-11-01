package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableGroupSaveRequest;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private static final List<String> NONE_UNGROUP_ORDER_STATUS = List.of(COOKING.name(), OrderStatus.MEAL.name());

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao,
                             final OrderTableDao orderTableDao,
                             final TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupSaveRequest request) {
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(request.getOrderTables()));
        return new TableGroupResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup savedTableGroup = tableGroupRepository.getById(tableGroupId);
        List<Long> orderTableIds = toOrderTableIds(tableGroupId);
        savedTableGroup.ungroup(() ->
                orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, NONE_UNGROUP_ORDER_STATUS));
    }

    private List<Long> toOrderTableIds(final Long tableGroupId) {
        return orderTableDao.findAllByTableGroupId(tableGroupId)
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
