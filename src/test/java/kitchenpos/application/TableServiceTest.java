//package kitchenpos.application;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.dao.TableGroupDao;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.TableGroup;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.EnumSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.annotation.DirtiesContext.ClassMode;
//
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
//@SpringBootTest
//class TableServiceTest {
//
//    public static final Long INVALID_ID = 100L;
//
//    @Autowired
//    private TableService tableService;
//
//    @Autowired
//    private TableGroupDao tableGroupDao;
//
//    @Autowired
//    private OrderTableDao orderTableDao;
//
//    @Autowired
//    private OrderDao orderDao;
//
//    @DisplayName("[성공] 테이블 생성")
//    @Test
//    void create_Success() {
//        // given
//        OrderTable orderTable = newOrderTable();
//
//        // when
//        OrderTable createdOrderTable = tableService.create(orderTable);
//
//        // then
//        assertThat(createdOrderTable.getId()).isNotNull();
//        assertThat(createdOrderTable)
//            .extracting("tableGroupId", "numberOfGuests", "empty")
//            .containsExactly(null, 0, true);
//    }
//
//    @DisplayName("[성공] 테이블 전체 조회")
//    @Test
//    void list_Success() {
//        // given
//        int previousSize = tableService.list().size();
//        tableService.create(newOrderTable());
//
//        // when
//        List<OrderTable> result = tableService.list();
//
//        // then
//        assertThat(result).hasSize(previousSize + 1);
//    }
//
//    @DisplayName("테이블 비어있는 여부 변경 테스트")
//    @Nested
//    class OrderTableChangeEmpty {
//
//        @DisplayName("[성공] 비어있지 않은 테이블로 변경")
//        @Test
//        void changeEmpty_toNotEmpty_Success() {
//            // given
//            OrderTable orderTable = tableService.create(newOrderTable());
//
//            // when
//            OrderTable result =
//                tableService.changeEmpty(orderTable.getId(), notEmptyOrderTable());
//
//            // then
//            assertThat(result.getId()).isNotNull();
//            assertThat(result)
//                .extracting("tableGroupId", "numberOfGuests", "empty")
//                .containsExactly(null, 0, false);
//        }
//
//        @DisplayName("[성공] 비어있는 테이블로 변경")
//        @Test
//        void changeEmpty_Empty_Success() {
//            // given
//            OrderTable notEmptyTable = newOrderTable();
//            notEmptyTable.setEmpty(false);
//
//            OrderTable orderTable = tableService.create(notEmptyTable);
//
//            // when
//            OrderTable result =
//                tableService.changeEmpty(orderTable.getId(), newOrderTable());
//
//            // then
//            assertThat(result.getId()).isNotNull();
//            assertThat(result)
//                .extracting("tableGroupId", "numberOfGuests", "empty")
//                .containsExactly(null, 0, true);
//        }
//
//        @DisplayName("[실패] 없는 테이블이면 예외 발생")
//        @Test
//        void changeEmpty_invalidTableId_ExceptionThrown() {
//            // given
//            // when
//            // then
//            assertThatThrownBy(() -> tableService.changeEmpty(INVALID_ID, notEmptyOrderTable()))
//                .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("[실패] 그룹 테이블에 속한 테이블이면 예외 발생")
//        @Test
//        void changeEmpty_NotNullGroupId_ExceptionThrown() {
//            // given
//            OrderTable defaultTable = tableService.create(newOrderTable());
//            OrderTable orderTable = tableService.create(newOrderTable());
//            TableGroup tableGroup = saveAndReturnTableGroup(orderTable, defaultTable);
//            orderTable.setTableGroupId(tableGroup.getId());
//            orderTableDao.save(orderTable);
//
//            // when
//            // then
//            assertThatThrownBy(() ->
//                tableService.changeEmpty(orderTable.getId(), notEmptyOrderTable())
//            ).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        private TableGroup saveAndReturnTableGroup(OrderTable... orderTables) {
//            TableGroup tableGroup = new TableGroup();
//            tableGroup.setCreatedDate(LocalDateTime.now());
//            tableGroup.setOrderTables(Arrays.asList(orderTables));
//
//            return tableGroupDao.save(tableGroup);
//        }
//
//        @DisplayName("[실패] 요리중/먹는중 테이블이면 예외 발생")
//        @ParameterizedTest
//        @EnumSource(value = OrderStatus.class, names = {"MEAL", "COOKING"})
//        void changeEmpty_invalidTableId_ExceptionThrown(OrderStatus orderStatus) {
//            // given
//
//            OrderTable orderTable = tableService.create(newOrderTable());
//            saveOrderWithStatus(orderTable.getId(), orderStatus);
//
//            // when
//            // then
//            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), newOrderTable()))
//                .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        private void saveOrderWithStatus(Long tableId, OrderStatus status) {
//            Order order = new Order();
//            order.setOrderTableId(tableId);
//            order.setOrderStatus(status.name());
//            order.setOrderedTime(LocalDateTime.now());
//
//            orderDao.save(order);
//        }
//    }
//
//    @DisplayName("테이블 손님 수 변경 테스트")
//    @Nested
//    class OrderTableChangeNumberOfGuests {
//
//        @DisplayName("[성공] 테이블의 손님 수를 양수로 변경")
//        @Test
//        void changeNumberOfGuests_PositiveNum_Success() {
//            // given
//            OrderTable targetGuestNum = newOrderTable();
//            targetGuestNum.setNumberOfGuests(2);
//
//            OrderTable notEmptyOrderTable = tableService.changeEmpty(
//                tableService.create(newOrderTable()).getId(), notEmptyOrderTable()
//            );
//
//            // when
//            OrderTable result =
//                tableService.changeNumberOfGuests(notEmptyOrderTable.getId(), targetGuestNum);
//
//            // then
//            assertThat(result.getId()).isNotNull();
//            assertThat(result.getNumberOfGuests()).isEqualTo(2);
//        }
//
//        @DisplayName("[성공] 테이블의 손님 수를 0로 변경")
//        @Test
//        void changeNumberOfGuests_ZeroNum_Success() {
//            // given
//            OrderTable targetGuestNum = newOrderTable();
//            targetGuestNum.setNumberOfGuests(0);
//
//            OrderTable notEmptyOrderTable = tableService.changeEmpty(
//                tableService.create(newOrderTable()).getId(), notEmptyOrderTable()
//            );
//
//            // when
//            OrderTable result =
//                tableService.changeNumberOfGuests(notEmptyOrderTable.getId(), targetGuestNum);
//
//            // then
//            assertThat(result.getId()).isNotNull();
//            assertThat(result.getNumberOfGuests()).isZero();
//        }
//
//        @DisplayName("[실패] 테이블의 손님 수를 음수로 변경 시 예외 발생")
//        @Test
//        void changeNumberOfGuests_NegativeNum_ExceptionThrown() {
//            // given
//            OrderTable targetGuestNum = newOrderTable();
//            targetGuestNum.setNumberOfGuests(-10);
//
//            OrderTable notEmptyOrderTable = tableService.changeEmpty(
//                tableService.create(newOrderTable()).getId(), notEmptyOrderTable()
//            );
//
//            // when
//            // then
//            assertThatThrownBy(() ->
//                tableService.changeNumberOfGuests(notEmptyOrderTable.getId(), targetGuestNum)
//            ).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("[실패] 존재하지 않는 주문 테이블 이면 예외 발생")
//        @Test
//        void changeNumberOfGuests_InvalidOrderTable_ExceptionThrown() {
//            // given
//            OrderTable targetGuestNum = newOrderTable();
//            targetGuestNum.setNumberOfGuests(2);
//
//            // when
//            // then
//            assertThatThrownBy(() ->
//                tableService.changeNumberOfGuests(INVALID_ID, targetGuestNum)
//            ).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("[실패] 비어있는 주문 테이블 이면 예외 발생")
//        @Test
//        void changeNumberOfGuests_EmptyOrderTable_ExceptionThrown() {
//            // given
//            OrderTable targetGuestNum = newOrderTable();
//            targetGuestNum.setNumberOfGuests(2);
//
//            OrderTable emptyOrderTable = tableService.create(newOrderTable());
//
//            // when
//            // then
//            assertThatThrownBy(() ->
//                tableService.changeNumberOfGuests(emptyOrderTable.getId(), targetGuestNum)
//            ).isInstanceOf(IllegalArgumentException.class);
//        }
//    }
//
//    private OrderTable notEmptyOrderTable() {
//        OrderTable orderTable = newOrderTable();
//        orderTable.setEmpty(false);
//
//        return orderTable;
//    }
//
//    private OrderTable newOrderTable() {
//        OrderTable orderTable = new OrderTable();
//        orderTable.setNumberOfGuests(0);
//        orderTable.setEmpty(true);
//
//        return orderTable;
//    }
//}
