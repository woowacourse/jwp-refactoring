package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.ApplicationTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void create() {
        OrderTable orderTable1 = orderTableDao.save(OrderTable.of(null, 10, true));
        OrderTable orderTable2 = orderTableDao.save(OrderTable.of(null, 10, true));
        TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThat(savedTableGroup).isNotNull();
    }

    @Test
    void createThrowExceptionNotCollectOrderTableSize() {
        TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), List.of());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 테이블은 2 테이블 이상부터 가능합니다.");
    }

    @Test
    void createThrowExceptionNotCollectOrderTableInfo() {
        OrderTable orderTable1 = OrderTable.of(1L, 1L, 10, false);
        OrderTable orderTable2 = OrderTable.of(0L, 2L, 10, false);
        TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 정보가 실제 주문한 정보와 일치하지 않습니다.");
    }

    @Test
    void createThrowExceptionAlreadyReservationOrderTable() {
        Long tableGroupId = tableGroupDao.save(TableGroup.of(LocalDateTime.now(), new ArrayList<>()))
                .getId();
        OrderTable orderTable1 = orderTableDao.save(OrderTable.of(tableGroupId, 10, true));
        OrderTable orderTable2 = orderTableDao.save(OrderTable.of(tableGroupId, 10, true));

        TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 테이블이거나 주문 테이블이 비어있지 않습니다.");
    }

    @Test
    void createThrowExceptionNotEmptyTable() {
        Long tableGroupId = tableGroupDao.save(TableGroup.of(LocalDateTime.now(), new ArrayList<>()))
                .getId();
        OrderTable orderTable1 = orderTableDao.save(OrderTable.of(tableGroupId, 10, false));
        OrderTable orderTable2 = orderTableDao.save(OrderTable.of(tableGroupId, 10, true));

        TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 테이블이거나 주문 테이블이 비어있지 않습니다.");
    }

    @Test
    void ungroup() {
        OrderTable orderTable1 = orderTableDao.save(OrderTable.of(null, 10, true));
        OrderTable orderTable2 = orderTableDao.save(OrderTable.of(null, 10, true));
        Long tableGroupId = tableGroupDao.save(TableGroup.of(LocalDateTime.now(), List.of(orderTable1, orderTable2)))
                .getId();

        tableGroupService.ungroup(tableGroupId);

        OrderTable orderTable = orderTableDao.findById(orderTable1.getId())
                .get();
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @Test
    void ungroupThrowExceptionWhenStillCookingOrderTable() {
        Long tableGroupId = tableGroupDao.save(TableGroup.of(LocalDateTime.now(), List.of()))
                .getId();
        OrderTable orderTable = orderTableDao.save(OrderTable.of(tableGroupId, 10, true));
        orderDao.save(Order.of(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), List.of()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 중이거나 식사 중인 테이블이 존재합니다.");
    }

    @Test
    void ungroupThrowExceptionWhenStillMeal() {
        Long tableGroupId = tableGroupDao.save(TableGroup.of(LocalDateTime.now(), List.of()))
                .getId();
        OrderTable orderTable = orderTableDao.save(OrderTable.of(tableGroupId, 10, true));
        orderDao.save(Order.of(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), List.of()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 중이거나 식사 중인 테이블이 존재합니다.");
    }
}
