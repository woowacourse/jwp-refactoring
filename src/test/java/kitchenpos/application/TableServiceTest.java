//package kitchenpos.application;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import java.util.List;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.TableGroup;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//class TableServiceTest extends IntegrationTest {
//
//    @Autowired
//    private TableService tableService;
//
//    @Test
//    void create_order_table() {
//        // given
//        final OrderTable orderTable = new OrderTable();
//        orderTable.setNumberOfGuests(0);
//        orderTable.setEmpty(true);
//
//        // when
//        final OrderTable createdOrderTable = tableService.create(orderTable);
//
//        // then
//        assertThat(createdOrderTable.getId()).isNotNull();
//    }
//
//    @Test
//    void list() {
//        // given
//        generateOrderTable(3);
//        generateOrderTable(2);
//        generateOrderTable(1);
//
//        // when
//        final List<OrderTable> findAll = tableService.list();
//
//        // then
//        assertThat(findAll).hasSize(3);
//    }
//
//    @Test
//    void change_empty_success() {
//        // given
//        final OrderTable orderTable = generateOrderTableWithOutTableGroup(1, true);
//        generateOrder(OrderStatus.COMPLETION, orderTable);
//        generateOrder(OrderStatus.COMPLETION, orderTable);
//
//        final OrderTable requestOrderTable = new OrderTable();
//        requestOrderTable.setNumberOfGuests(1);
//        requestOrderTable.setEmpty(false);
//
//        // when
//        final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), requestOrderTable);
//
//        // then
//        assertThat(changedOrderTable.isEmpty()).isFalse();
//    }
//
//    @Nested
//    class change_empty_failure {
//
//        @Test
//        void order_table_is_not_exist() {
//            // given
//            final OrderTable orderTable = new OrderTable();
//
//            // when & then
//            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Test
//        void order_table_is_still_group_by_table_group() {
//            // given
//            final TableGroup savedTableGroup = generateTableGroup();
//            final OrderTable orderTable = generateOrderTable(1, true, savedTableGroup);
//
//            // when & then
//            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Test
//        void any_order_in_order_table_status_is_not_completion() {
//            // given
//            final OrderTable orderTable = generateOrderTableWithOutTableGroup(1, true);
//            generateOrder(OrderStatus.COOKING, orderTable);
//            generateOrder(OrderStatus.COMPLETION, orderTable);
//
//            // when & then
//            assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//    }
//
//    @Test
//    void change_number_of_guests_success() {
//        // given
//        final OrderTable orderTable = generateOrderTableWithOutTableGroup(1, false);
//
//        final OrderTable requestOrderTable = new OrderTable();
//        requestOrderTable.setNumberOfGuests(20);
//
//        // when
//        final OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), requestOrderTable);
//
//        // then
//        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(20);
//    }
//
//    @Nested
//    class change_number_of_guests_failure {
//
//        @Test
//        void change_number_is_less_than_zero() {
//            // given
//            final OrderTable orderTable = new OrderTable();
//            orderTable.setNumberOfGuests(-2);
//
//            // when
//            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Test
//        void order_table_is_not_exist() {
//            // given
//            final OrderTable orderTable = new OrderTable();
//            orderTable.setId(1L);
//            orderTable.setNumberOfGuests(22);
//
//            // when
//            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Test
//        void order_table_is_empty() {
//            // given
//            final OrderTable orderTable = generateOrderTableWithOutTableGroup(1, true);
//
//            // when
//            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//    }
//}
