//package kitchenpos.application;
//
//import fixture.OrderBuilder;
//import fixture.OrderTableBuilder;
//import fixture.TableGroupBuilder;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.dao.TableGroupDao;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.TableGroup;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.assertj.core.api.SoftAssertions.assertSoftly;
//
//class TableGroupServiceTest extends ServiceTest {
//
//    @Autowired
//    TableGroupService tableGroupService;
//
//    @Autowired
//    OrderTableDao orderTableDao;
//
//    @Autowired
//    TableGroupDao tableGroupDao;
//
//    @Autowired
//    OrderDao orderDao;
//
//    @Test
//    void 테이블_그룹을_생성한다() {
//        TableGroup tableGroup = TableGroupBuilder.init().build();
//
//        TableGroup created = tableGroupService.create(tableGroup);
//
//        assertThat(created.getId()).isNotNull();
//    }
//
//    @Test
//    void 주문_테이블이_2개_이하면_예외를_발생한다() {
//        TableGroup tableGroup = TableGroupBuilder.init()
//                .orderTables(List.of(OrderTableBuilder.init().id(1L).build()))
//                .build();
//
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 테이블_그룹이_비어_있으면_예외를_발생한다() {
//        TableGroup tableGroup = TableGroupBuilder.init()
//                .orderTables(List.of())
//                .build();
//
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문테이블아이디들로_주문테이블을_조회해서_개수가_맞지_않으면_예외를_발생한다() {
//        TableGroup tableGroup = TableGroupBuilder.init()
//                .orderTables(
//                        List.of(
//                                OrderTableBuilder.init().id(1L).build(),
//                                OrderTableBuilder.init().id(100L).build()
//                        ))
//                .build();
//
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문테이블중_빈_주문테이블이_있으면_예외를_발생한다() {
//        TableGroup tableGroup = TableGroupBuilder.init()
//                .orderTables(
//                        List.of(
//                                OrderTableBuilder.init().id(1L).build(),
//                                OrderTableBuilder.init().id(9L).build()
//                        ))
//                .build();
//
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 테이블그룹의_주문테이블들을_빈_테이블로_변경한다() {
//        tableGroupService.ungroup(1L);
//
//        List<OrderTable> allByTableGroupId = orderTableDao.findAllByTableGroupId(1L);
//        assertSoftly(softAssertions -> {
//            for (OrderTable orderTable : allByTableGroupId) {
//                softAssertions.assertThat(orderTable.isEmpty()).isTrue();
//            }
//        });
//    }
//
//    @Test
//    void 주문_테이블의_상태가_빈_상태면_주문_테이블_고객수를_변경하지_못한다() {
//        TableGroup tableGroup = TableGroupBuilder.init()
//                .build();
//        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
//        OrderTable orderTable1 = OrderTableBuilder.init()
//                .build();
//        OrderTable orderTable2 = OrderTableBuilder.init()
//                .build();
//        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
//        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
//        Order order1 = OrderBuilder.init()
//                .orderTable(savedOrderTable1.getId())
//                .orderStatus(OrderStatus.MEAL.name())
//                .build();
//        Order order2 = OrderBuilder.init()
//                .orderTable(savedOrderTable2.getId())
//                .orderStatus(OrderStatus.COMPLETION.name())
//                .build();
//        orderDao.save(order1);
//        orderDao.save(order2);
//
//        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId())).isInstanceOf(IllegalArgumentException.class);
//    }
//}
