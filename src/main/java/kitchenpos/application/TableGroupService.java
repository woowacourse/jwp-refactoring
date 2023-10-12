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
    public TableGroup create(final TableGroup tableGroup) { // OrderTable Domain 의 id 들만 들어있는 것을 가져온다.
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) { // null, 사이즈가 2 미만인 경우 예외 발생
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList()); // OrderTable 들의 ID 를 가져온다.

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds); // 해당 값을 ID 들을 기준으로 OrderTable 들을 가져온다.

        if (orderTables.size() != savedOrderTables.size()) { // 없는 OrderTable 은 존재하면 안된다.
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) { // 이제 저장한다. 찾아온 OrderTable 들을 기준으로
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) { // orderTable 의 empty 가 false 인 경우 실패, 그리고 tableGroupId 이 null 이 아닌 경우도 실패
                throw new IllegalArgumentException();
            } // 주문을 할 수 없는 상태이거나, 이미 tableGroup 에 할당이 되었거나이다.
        }

        tableGroup.setCreatedDate(LocalDateTime.now()); // tableGroup 을 만드는 과정

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup); // 이제 tableGroup 저장

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables); // 이제 만든 orderTables 를 저장하고 진행

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId); // 관련된 OrderTables 를 모두 불러온다.

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList()); // 또 이제 가져온 것들을 기준으로 진행한다.

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) { // 현재 요리 중이거나, 식사 중이면 안된다.
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            orderTableDao.save(orderTable);
        } // orderTable table group 을 해제하고 empty 에 false 를 설정해준다, empty 는 주문을 등록할 수 없는 table 이라는 뜻
    }
}
