//package kitchenpos.application;
//
//import static kitchenpos.fixtures.TableGroupFixture.TABLE_GROUP;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.TableGroup;
//import kitchenpos.support.TestSupporter;
//import org.junit.jupiter.api.DisplayNameGeneration;
//import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
//
//@SuppressWarnings("NonAsciiCharacters")
//@DisplayNameGeneration(ReplaceUnderscores.class)
//@Sql("/truncate.sql")
//@SpringBootTest
//class TableGroupServiceTest {
//
//    @Autowired
//    private TableGroupService tableGroupService;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private OrderTableDao orderTableDao;
//
//    @Autowired
//    private TestSupporter testSupporter;
//
//    @Test
//    void 테이블_그룹을_생성한다() {
//        // given
//        final OrderTable orderTable1 = testSupporter.createOrderTable(true);
//        final OrderTable orderTable2 = testSupporter.createOrderTable(true);
//        final TableGroup tableGroup = TABLE_GROUP(List.of(orderTable1, orderTable2));
//
//        // when
//        final TableGroup actual = tableGroupService.create(tableGroup);
//
//        // then
//        assertThat(actual).usingRecursiveComparison()
//                          .ignoringExpectedNullFields()
//                          .ignoringFields("orderTables.empty")
//                          .isEqualTo(tableGroup);
//    }
//
//    @Test
//    void 테이블_그룹을_생성할_때_주문_테이블이_2_테이블_미만이라면_예외가_발생한다() {
//        // given
//        final OrderTable orderTable = testSupporter.createOrderTable(true);
//        final TableGroup tableGroup = TABLE_GROUP(List.of(orderTable));
//
//        // when & then
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 테이블_그룹을_생성할_때_주문_테이블들_중_하나라도_비어있지_않다면_예외가_발생한다() {
//        // given
//        final OrderTable orderTable1 = testSupporter.createOrderTable(true);
//        final OrderTable orderTable2 = testSupporter.createOrderTable(false);
//        final TableGroup tableGroup = TABLE_GROUP(List.of(orderTable1, orderTable2));
//
//        // when & then
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 테이블_그룹을_해제한다() {
//        // given
//        final OrderTable orderTable1 = testSupporter.createOrderTable(true);
//        final OrderTable orderTable2 = testSupporter.createOrderTable(true);
//
//        final TableGroup tableGroup = TABLE_GROUP(List.of(orderTable1, orderTable2));
//        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);
//
//        final Order order1 = testSupporter.createOrder(orderTable1);
//        order1.setOrderStatus(OrderStatus.COMPLETION.name());
//        orderService.changeOrderStatus(order1.getId(), order1);
//
//        final Order order2 = testSupporter.createOrder(orderTable2);
//        order2.setOrderStatus(OrderStatus.COMPLETION.name());
//        orderService.changeOrderStatus(order2.getId(), order2);
//
//        // when
//        tableGroupService.ungroup(savedTableGroup.getId());
//
//        // then
//        final List<OrderTable> orderTables = orderTableDao.findAll();
//        final List<OrderTable> actual = orderTables.stream()
//                                                   .filter(orderTable -> orderTable.getTableGroupId() != null)
//                                                   .filter(OrderTable::isEmpty)
//                                                   .collect(Collectors.toList());
//        assertThat(actual).isEmpty();
//    }
//
//    @ParameterizedTest(name = "주문 상태가 {0}일때 예외가 발생한다.")
//    @ValueSource(strings = {"COOKING", "MEAL"})
//    void 테이블_그룹을_해제할_때_주문의_상태가_하나라도_COMPLETION_이_아니라면_예외가_발생한다(final String orderStatus) {
//        // given
//        final OrderTable orderTable1 = testSupporter.createOrderTable(true);
//        final OrderTable orderTable2 = testSupporter.createOrderTable(true);
//
//        final TableGroup tableGroup = TABLE_GROUP(List.of(orderTable1, orderTable2));
//        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);
//
//        final Order order1 = testSupporter.createOrder(orderTable1);
//        order1.setOrderStatus(orderStatus);
//        orderService.changeOrderStatus(order1.getId(), order1);
//
//        final Order order2 = testSupporter.createOrder(orderTable2);
//        order2.setOrderStatus(orderStatus);
//        orderService.changeOrderStatus(order2.getId(), order2);
//
//        // when & then
//        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//}