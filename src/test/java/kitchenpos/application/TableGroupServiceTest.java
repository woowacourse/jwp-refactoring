package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.ApplicationTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.dao.OrderDao;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.request.TableGroupRequest;
import kitchenpos.table.application.response.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.dao.OrderTableDao;
import kitchenpos.table.domain.dao.TableGroupDao;
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
        Long savedId1 = orderTableDao.save(OrderTable.of(null, 10, true))
                .getId();
        Long savedId2 = orderTableDao.save(OrderTable.of(null, 10, true))
                .getId();
        TableGroupRequest request = new TableGroupRequest(List.of(savedId1, savedId2));

        TableGroupResponse response = tableGroupService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void createThrowExceptionNotCollectOrderTableSize() {
        TableGroupRequest tableGroup = new TableGroupRequest(new ArrayList<>());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 테이블은 2 테이블 이상부터 가능합니다.");
    }

    @Test
    void createThrowExceptionAlreadyReservationOrderTable() {
        Long tableGroupId = tableGroupDao.save(new TableGroup(LocalDateTime.now(), new ArrayList<>()))
                .getId();
        OrderTable orderTable1 = orderTableDao.save(OrderTable.of(tableGroupId, 10, true));
        OrderTable orderTable2 = orderTableDao.save(OrderTable.of(tableGroupId, 10, true));

        TableGroupRequest request = new TableGroupRequest(List.of(orderTable1.getId(), orderTable2.getId()));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 테이블이거나 주문 테이블이 비어있지 않습니다.");
    }

    @Test
    void createThrowExceptionNotEmptyTable() {
        Long tableGroupId = tableGroupDao.save(new TableGroup(LocalDateTime.now(), new ArrayList<>()))
                .getId();
        OrderTable orderTable1 = orderTableDao.save(OrderTable.of(tableGroupId, 10, false));
        OrderTable orderTable2 = orderTableDao.save(OrderTable.of(tableGroupId, 10, true));

        TableGroupRequest request = new TableGroupRequest(List.of(orderTable1.getId(), orderTable2.getId()));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 테이블이거나 주문 테이블이 비어있지 않습니다.");
    }

    @Test
    void ungroup() {
        OrderTable orderTable1 = orderTableDao.save(OrderTable.of(null, 10, true));
        OrderTable orderTable2 = orderTableDao.save(OrderTable.of(null, 10, true));
        Long tableGroupId = tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)))
                .getId();

        tableGroupService.ungroup(tableGroupId);

        OrderTable orderTable = orderTableDao.findById(orderTable1.getId())
                .get();
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @Test
    void ungroupThrowExceptionWhenStillCookingOrderTable() {
        Long tableGroupId = tableGroupDao.save(new TableGroup(LocalDateTime.now(), new ArrayList<>()))
                .getId();
        OrderTable orderTable = orderTableDao.save(OrderTable.of(tableGroupId, 10, true));
        orderDao.save(new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 중이거나 식사 중인 테이블이 존재합니다.");
    }

    @Test
    void ungroupThrowExceptionWhenStillMeal() {
        Long tableGroupId = tableGroupDao.save(new TableGroup(LocalDateTime.now(), new ArrayList<>()))
                .getId();
        OrderTable orderTable = orderTableDao.save(OrderTable.of(tableGroupId, 10, true));
        orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), new ArrayList<>()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 중이거나 식사 중인 테이블이 존재합니다.");
    }
}
