//package kitchenpos.application;
//
//import static kitchenpos.fixtures.OrderTableFixture.ORDER_TABLE;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import java.util.List;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderTable;
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
//class TableServiceTest {
//
//    @Autowired
//    private TableService tableService;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private TestSupporter testSupporter;
//
//    @Test
//    void 주문_테이블을_생성한다() {
//        // given
//        final OrderTable orderTable = ORDER_TABLE();
//
//        // when
//        final OrderTable actual = tableService.create(orderTable);
//
//        // then
//        assertThat(actual).usingRecursiveComparison()
//                          .ignoringExpectedNullFields()
//                          .isEqualTo(orderTable);
//    }
//
//    @Test
//    void 주문_테이블에_대해_전체_조회한다() {
//        // given
//        testSupporter.createOrderTable(false);
//
//        // when
//        final List<OrderTable> orderTables = tableService.list();
//
//        // then
//        assertThat(orderTables).hasSize(1);
//    }
//
//    @Test
//    void 주문_테이블의_empty_속성을_변경한다() {
//        // given
//        final OrderTable orderTable = testSupporter.createOrderTable(true);
//        orderTable.setEmpty(false);
//
//        // when
//        final OrderTable actual = tableService.changeEmpty(orderTable.getId(), orderTable);
//
//        // then
//        assertThat(actual.isEmpty()).isFalse();
//    }
//
//    @Test
//    void 주문_테이블의_empty_속성을_변경할_때_주문_테이블이_실재하지_않다면_예외가_발생한다() {
//        // given
//        final OrderTable orderTable = testSupporter.createOrderTable(true);
//        orderTable.setEmpty(false);
//
//        // when & then
//        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId() + 1, orderTable))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문_테이블의_empty_속성을_변경할_때_단쳬_지정_식별자가_존재한다면_예외가_발생한다() {
//        // given
//        final OrderTable orderTable1 = testSupporter.createOrderTable(true);
//        final OrderTable orderTable2 = testSupporter.createOrderTable(true);
//        testSupporter.createTableGroup(List.of(orderTable1, orderTable2));
//        final OrderTable savedOrderTable = tableService.list().get(0);
//        savedOrderTable.setEmpty(true);
//
//        // when & then
//        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @ParameterizedTest(name = "주문 상태가 {0}일때 예외가 발생한다.")
//    @ValueSource(strings = {"COOKING", "MEAL"})
//    void 주문_테이블의_empty_속성을_변경할_때_주문_상태가_COMPLETION_이_아니라면_예외가_발생한다(final String orderStatus) {
//        // given
//        final Order order = testSupporter.createOrder();
//        order.setOrderStatus(orderStatus);
//        orderService.changeOrderStatus(order.getId(), order);
//
//        final OrderTable orderTable = tableService.list().get(0);
//
//        // when & then
//        orderTable.setEmpty(false);
//        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문_테이블의_방문한_손님_수를_변경한다() {
//        // given
//        final OrderTable orderTable = testSupporter.createOrderTable(false);
//
//        // when
//        int numberOfGuest = 3;
//        orderTable.setNumberOfGuests(numberOfGuest);
//        final OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
//
//        // then
//        assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuest);
//    }
//
//    @Test
//    void 주문_테이블의_방문한_손님_수가_음수라면_예외가_발생한다() {
//        // given
//        final OrderTable orderTable = testSupporter.createOrderTable(false);
//
//        // when & then
//        int numberOfGuest = -10;
//        orderTable.setNumberOfGuests(numberOfGuest);
//        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 비어있는_주문_테이블의_방문한_손님_수를_변경하면_예외가_발생한다() {
//        // given
//        final OrderTable orderTable = testSupporter.createOrderTable(true);
//
//        // when & then
//        int numberOfGuest = 3;
//        orderTable.setNumberOfGuests(numberOfGuest);
//        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//}