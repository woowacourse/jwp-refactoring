//package kitchenpos.application;
//
//import kitchenpos.dao.OrderDao;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderLineItem;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//@ServiceTest
//class TableServiceTest {
//
//    @Autowired
//    private TableService tableService;
//
//    @Autowired
//    private OrderDao orderDao;
//
//    @Test
//    void 주문_테이블을_생성한다() {
//        // given
//        OrderTable orderTable = new OrderTable(3, true);
//
//        // when
//        OrderTable createdOrderTable = tableService.create(orderTable);
//
//        // then
//        assertThat(createdOrderTable).usingRecursiveComparison()
//                .ignoringFields("id", "tableGroupId").isEqualTo(orderTable);
//    }
//
//    @Test
//    void 주문_테이블_리스트를_조회한다() {
//        // given
//        OrderTable orderTable = new OrderTable(3, true);
//        tableService.create(orderTable);
//
//        // when
//        List<OrderTable> orderTables = tableService.list();
//
//        // then
//        assertThat(orderTables).usingRecursiveComparison()
//                .ignoringFields("id", "tableGroupId")
//                .isEqualTo(List.of(new OrderTable(0, true), orderTable));
//    }
//
//    @Test
//    void 주문_테이블의_empty_상태를_변경한다() {
//        // given
//        OrderTable orderTable = tableService.create(new OrderTable(3, true));
//
//        orderDao.save(new Order(null, orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), List.of(new OrderLineItem())));
//
//        // when
//        OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), new OrderTable(false));
//
//        // then
//        assertThat(changedOrderTable.isEmpty()).isFalse();
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"MEAL", "COOKING"})
//    void 주문_테이블의_주문_상태가_MEAL_이나_COOKING이면_empty_상태_변경_요청시_예외_발생(String status) {
//        // given
//        OrderTable orderTable = tableService.create(new OrderTable(3, true));
//
//        orderDao.save(new Order(null, orderTable.getId(), status, LocalDateTime.now(), List.of(new OrderLineItem())));
//
//        // when, then
//        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable(false)))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문_테이블의_게스트_인원_수를_변경한다() {
//        // given
//        OrderTable orderTable = tableService.create(new OrderTable(3, false));
//
//        // when
//        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(),
//                new OrderTable(10, orderTable.isEmpty()));
//
//        // then
//        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(10);
//    }
//
//    @Test
//    void 변경할_게스트_인원이_음수면_인원_수_변경_요청_시_예외_발생() {
//        // given
//        OrderTable orderTable = tableService.create(new OrderTable(3, false));
//
//        // when, then
//        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
//                new OrderTable(-1, orderTable.isEmpty())))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문_테이블의_상태가_빈_상태면_게스트_인원_수_변경_요청_시_예외_발생() {
//        // given
//        OrderTable orderTable = tableService.create(new OrderTable(3, true));
//
//        // when, then
//        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
//                new OrderTable(1, orderTable.isEmpty())))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//}
