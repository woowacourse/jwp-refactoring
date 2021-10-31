package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Table Service 테스트")
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("OrderTable을 저장할 때 TableGroupId가 Null인 상태로 저장된다.")
    @Test
    void createWithTableGroupIdNull() {
        // given
        OrderTable orderTable = new OrderTable();

        // when
        OrderTable savedProduct = tableService.create(orderTable);

        // then
        assertThat(orderTableDao.findById(savedProduct.getId())).isPresent();
        assertThat(savedProduct.getTableGroupId()).isNull();
        assertThat(savedProduct).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(savedProduct);
    }

    @DisplayName("모든 OrderTable을 조회한다.")
    @Test
    void findAll() {
        // given
        List<OrderTable> beforeSavedOrderTable = tableService.list();

        beforeSavedOrderTable.add(tableService.create(new OrderTable()));
        beforeSavedOrderTable.add(tableService.create(new OrderTable()));
        beforeSavedOrderTable.add(tableService.create(new OrderTable()));

        // when
        List<OrderTable> afterSavedOrderTable = tableService.list();

        // then
        assertThat(afterSavedOrderTable).hasSize(beforeSavedOrderTable.size());
        assertThat(afterSavedOrderTable).usingRecursiveComparison()
            .isEqualTo(beforeSavedOrderTable);
    }
    
    @DisplayName("OrderTable의 상태를 Empty로 변경할 때")
    @Nested
    class ChangeStatusEmpty {

        @DisplayName("OrderTableId와 일치하는 OrderTable이 존재하지 않는 경우 예외가 발생한다.")
        @Test
        void orderTableNotFoundException() {
            // given
            TableGroup tableGroup = tableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = orderTableDao.save(OrderTable을_생성한다(tableGroup.getId()));

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(-1L, orderTable))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTable에 TableGroupId가 등록되어 있는 경우 예외가 발생한다.")
        @Test
        void tableGroupIdNonNullException() {
            // given
            TableGroup tableGroup = tableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = orderTableDao.save(OrderTable을_생성한다(tableGroup.getId()));

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }
        
        @DisplayName("OrderTableId가 일치하고, COOKING 상태인 Order가 존재하는 경우 예외가 발생한다.")
        @Test
        void existOrderTableIdMatchAndCookingStatusOrder() {
            // given
            OrderTable orderTable = orderTableDao.save(OrderTable을_생성한다());
            Order order = orderDao.save(Order를_생성한다(orderTable.getId(), COOKING));

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTableId가 일치하고, MEAL 상태인 Order가 존재하는 경우 예외가 발생한다.")
        @Test
        void existOrderTableIdMatchAndMealStatusOrder() {
            // given
            OrderTable orderTable = orderTableDao.save(OrderTable을_생성한다());
            Order order = orderDao.save(Order를_생성한다(orderTable.getId(), MEAL));

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTableId가 일치하고, COMPLETION 상태인 Order가 존재하는 경우 상태가 변경된다.")
        @Test
        void existOrderTableIdMatchAndCompletionStatusOrder() {
            // given
            OrderTable orderTable = orderTableDao.save(OrderTable을_생성한다());
            Order order = orderDao.save(Order를_생성한다(orderTable.getId(), COMPLETION));

            OrderTable newOrderTable = OrderTable을_생성한다();
            newOrderTable.setEmpty(true);

            // when
            OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), newOrderTable);

            // then
            assertThat(changedOrderTable.isEmpty()).isTrue();
        }
    }
    
    @DisplayName("손님 수를 변경할 때")
    @Nested
    class ChangeNumberOfGuests {

        @DisplayName("손님 수가 음수면 예외가 발생한다.")
        @Test
        void numberOfGuestsNegativeException() {
            // given
            OrderTable orderTable = orderTableDao.save(OrderTable을_생성한다());
            OrderTable newOrderTable = OrderTable을_생성한다();
            newOrderTable.setNumberOfGuests(-1);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("orderTableId와 일치하는 OrderTable이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void noExistOrderTableByIdException() {
            // given
            OrderTable newOrderTable = OrderTable을_생성한다();
            newOrderTable.setNumberOfGuests(3);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, newOrderTable))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTable이 이미 비어있는 상태일 경우 예외가 발생한다.")
        @Test
        void alreadyEmptyStatusOrderTableException() {
            // given
            OrderTable orderTable = OrderTable을_생성한다();
            orderTable.setEmpty(true);
            OrderTable savedOrderTable = orderTableDao.save(orderTable);

            OrderTable newOrderTable = OrderTable을_생성한다();
            newOrderTable.setNumberOfGuests(5);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTable이 비어있는 상태가 아니고, ID를 통해 조회할 수 있으며 손님 수가 음수가 아닌 경우 변경에 성공한다.")
        @Test
        void success() {
            // given
            OrderTable orderTable = orderTableDao.save(OrderTable을_생성한다());
            OrderTable newOrderTable = OrderTable을_생성한다();
            newOrderTable.setNumberOfGuests(5);

            // when
            OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable);

            assertThat(changedOrderTable.isEmpty()).isFalse();
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(newOrderTable.getNumberOfGuests());
        }
    }

    private OrderTable OrderTable을_생성한다() {
        return OrderTable을_생성한다(null);
    }

    private OrderTable OrderTable을_생성한다(Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        
        return orderTable;
    }

    private TableGroup TableGroup을_생성한다() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        return tableGroup;
    }

    private Order Order를_생성한다(Long orderTableId, OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }
}