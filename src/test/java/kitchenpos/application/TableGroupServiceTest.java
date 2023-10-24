//package kitchenpos.application;
//
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.dao.TableGroupDao;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderLineItem;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.TableGroup;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ServiceTest
//class TableGroupServiceTest {
//
//    @Autowired
//    private TableGroupService tableGroupService;
//
//    @Autowired
//    private OrderDao orderDao;
//
//    @Autowired
//    private OrderTableDao orderTableDao;
//
//    @Autowired
//    private TableGroupDao tableGroupDao;
//
//    @Test
//    void 테이블_그룹을_생성한다() {
//        // given
//        OrderTable orderTable1 = orderTableDao.save(new OrderTable(3, true));
//        OrderTable orderTable2 = orderTableDao.save(new OrderTable(2, true));
//
//        // when
//        TableGroup tableGroup = tableGroupService.create(new TableGroup(List.of(orderTable1, orderTable2)));
//
//        // then
//        assertThat(tableGroup.getOrderTables()).usingRecursiveComparison()
//                .ignoringFields("id", "tableGroupId")
//                .isEqualTo(List.of(new OrderTable(3, false), new OrderTable(2, false)));
//    }
//
//    @Test
//    void 하나의_테이블은_그룹화할_수_없다() {
//        // given
//        OrderTable orderTable = orderTableDao.save(new OrderTable(3, true));
//
//        // when, then
//        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(List.of(orderTable))))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 테이블_하나라도_비어있지_않으면_그룹화할_수_없다() {
//        // given
//        TableGroup tableGroup = tableGroupDao.save(new TableGroup(null, LocalDateTime.now(), Collections.emptyList()));
//        OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, tableGroup.getId(), 3, true));
//        OrderTable orderTable2 = orderTableDao.save(new OrderTable(3, true));
//
//
//        // when, then
//        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(List.of(orderTable1, orderTable2))))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 테이블_그룹을_해제한다() {
//        // given
//        OrderTable orderTable1 = orderTableDao.save(new OrderTable(3, true));
//        OrderTable orderTable2 = orderTableDao.save(new OrderTable(2, true));
//
//        TableGroup tableGroup = tableGroupService.create(new TableGroup(List.of(orderTable1, orderTable2)));
//
//        // when
//        tableGroupService.ungroup(tableGroup.getId());
//
//        OrderTable unGroupedOrderTable1 = orderTableDao.findById(orderTable1.getId()).get();
//        OrderTable unGroupedOrderTable2 = orderTableDao.findById(orderTable2.getId()).get();
//
//        // then
//        assertAll(
//                () -> assertEquals(unGroupedOrderTable1.getTableGroupId(), null),
//                () -> assertEquals(unGroupedOrderTable1.isEmpty(), false),
//                () -> assertEquals(unGroupedOrderTable2.getTableGroupId(), null),
//                () -> assertEquals(unGroupedOrderTable2.isEmpty(), false)
//        );
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"MEAL", "COOKING"})
//    void 그룹으로_묶여있는_테이블의_주문_상태가_하나라도_MEAL_이나_COOKING_상태면_그룹화를_해제할_수_없다(String status) {
//        // given
//        OrderTable orderTable1 = orderTableDao.save(new OrderTable(3, true));
//        OrderTable orderTable2 = orderTableDao.save(new OrderTable(2, true));
//
//        TableGroup tableGroup = tableGroupService.create(new TableGroup(List.of(orderTable1, orderTable2)));
//
//        orderDao.save(new Order(null, orderTable1.getId(), status, LocalDateTime.now()
//                , List.of(new OrderLineItem(3L, 3))));
//
//        // when, then
//        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//}
