package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.exception.TableGroupNotFoundException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao,
                             final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = getOrderTables(request);
        tableGroup.grouping(orderTables);
        tableGroupDao.save(tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> getOrderTables(final TableGroupCreateRequest request) {
        return request.getOrderTables()
                .stream()
                .map(orderTableDao::getById)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);
        validatePossibleUngrouping(tableGroup);
        tableGroup.ungrouping();
    }

    private void validatePossibleUngrouping(final TableGroup tableGroup) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                tableGroup.getOrderTables(), List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException("조리중이거나 식사 중인 테이블이 있습니다.");
        }
    }
}
