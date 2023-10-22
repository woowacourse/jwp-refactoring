package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    
    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }
    
    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("개별 테이블 수가 2개 미만이면 단체 테이블을 만들 수 없습니다");
        }
        
        final List<Long> orderTableIds = orderTables.stream()
                                                    .map(OrderTable::getId)
                                                    .collect(Collectors.toList());
        
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블이 있습니다");
        }
//비어있지 않은 테이블이거나 다른 테이블 그룹에 속한 테이블은 단체 테이블으로 만들 수 없습니다
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException("다른 테이블 그룹에 속한 테이블은 단체 테이블로 만들 수 없습니다");
            }
            if (!savedOrderTable.isEmpty()) {
                throw new IllegalArgumentException("비어있지 않은 테이블은 단체 테이블으로 만들 수 없습니다");
            }
        }
        
        tableGroup.setCreatedDate(LocalDateTime.now());
        
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        
        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false); //주문을 등록할 수 있게된다.
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);
        
        return savedTableGroup;
    }
    
    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        
        final List<Long> orderTableIds = orderTables.stream()
                                                    .map(OrderTable::getId)
                                                    .collect(Collectors.toList());
        
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("테이블의 상태가 COOKING 혹은 MEAL이면 단체 테이블을 해제할 수 없습니다");
        }
        
        //새로운 orderTable이 생기는 것 아닌가?
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            orderTableDao.save(orderTable);
        }
    }
}
