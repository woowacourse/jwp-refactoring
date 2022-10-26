package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
        TableGroupDao tableGroupDao) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = getOrderTables(tableGroupRequest);
        TableGroup tableGroup = TableGroup.group(orderTables, LocalDateTime.now());
        tableGroupDao.save(tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> getOrderTables(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateNotExistTables(orderTableIds, orderTables);
        return orderTables;
    }

    private void validateNotExistTables(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블로 지정할 수 없습니다.");
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = getGroupById(tableGroupId);
        validateNotCompletedOrderExist(tableGroup);
        tableGroup.ungroup();
    }

    private TableGroup getGroupById(Long tableGroupId) {
        return tableGroupDao.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹입니다."));
    }

    private void validateNotCompletedOrderExist(TableGroup tableGroup) {
        if (existNotCompletedOrder(tableGroup)) {
            throw new IllegalArgumentException("완료되지 않은 주문이 있어서 해제할 수 없습니다.");
        }
    }

    private boolean existNotCompletedOrder(TableGroup tableGroup) {
        return orderRepository.existsByOrderTableInAndOrderStatusIn(
            tableGroup.getOrderTables(),
            OrderStatus.listInProgress()
        );
    }
}
