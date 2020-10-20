package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.utils.KitchenPosClassCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Sql("/truncate.sql")
@SpringBootTest
public class TableServiceTest {
    public static final int TEST_ORDER_TABLE_NUMBER_OF_GUESTS = 4;
    public static final int TEST_ORDER_TABLE_NUMBER_OF_GUESTS_CHANGED = 6;
    public static final int TEST_ORDER_TABLE_NUMBER_OF_GUESTS_INVALID = -1;
    private static final long TEST_INVALID_TABLE_ID = 10000L;

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = KitchenPosClassCreator.createOrderTable(TEST_ORDER_TABLE_NUMBER_OF_GUESTS, false);
    }

    @DisplayName("OrderTable 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        OrderTable savedOrderTable = tableService.create(orderTable);

        assertEquals(savedOrderTable.getNumberOfGuests(), orderTable.getNumberOfGuests());
        assertEquals(savedOrderTable.isEmpty(), false);
        assertNull(savedOrderTable.getTableGroupId());
    }

    @DisplayName("OrderTable 전체 목록 요청 시 올바른 값이 반환된다.")
    @Test
    void listTest() {
        OrderTable secondOrderTable = KitchenPosClassCreator.createOrderTable(TEST_ORDER_TABLE_NUMBER_OF_GUESTS, false);
        OrderTable savedOrderTable = tableService.create(orderTable);
        secondOrderTable = tableService.create(secondOrderTable);

        List<OrderTable> foundTables = tableService.list();

        assertThat(foundTables)
                .hasSize(2)
                .extracting("id")
                .containsOnly(savedOrderTable.getId(), secondOrderTable.getId());
    }

    @DisplayName("OrderTable을 빈 테이블로 변경 시, 올바르게 변경된다.")
    @Test
    void changeEmptyTest() {
        OrderTable savedOrderTable = tableService.create(orderTable);
        OrderTable paramOrderTable = KitchenPosClassCreator.createOrderTable(TEST_ORDER_TABLE_NUMBER_OF_GUESTS, true);

        OrderTable emptyTable = tableService.changeEmpty(savedOrderTable.getId(), paramOrderTable);

        assertTrue(emptyTable.isEmpty());
    }

    @DisplayName("예외 테스트 : OrderTable을 빈 테이블로 변경 시, 잘못된 ID를 전달하면 예외가 발생한다.")
    @Test
    void changeEmptyWithInvalidIdTest() {
        OrderTable paramOrderTable = KitchenPosClassCreator.createOrderTable(TEST_ORDER_TABLE_NUMBER_OF_GUESTS, true);

        assertThatThrownBy(() -> tableService.changeEmpty(TEST_INVALID_TABLE_ID, paramOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : OrderTable을 빈 테이블로 변경 시, NULL값인 ID를 전달하면 예외가 발생한다.")
    @Test
    void changeEmptyWithNullIdTest() {
        OrderTable paramOrderTable = KitchenPosClassCreator.createOrderTable(TEST_ORDER_TABLE_NUMBER_OF_GUESTS, true);

        assertThatThrownBy(() -> tableService.changeEmpty(null, paramOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : OrderTable을 빈 테이블로 변경 시, 테이블에 아직 완료되지 않은 주문이 있다면 예외가 발생한다.")
    @Test
    void changeEmptyWithProceedingOrderTest() {
        OrderTable savedOrderTable = tableService.create(orderTable);
        OrderTable paramOrderTable = KitchenPosClassCreator.createOrderTable(TEST_ORDER_TABLE_NUMBER_OF_GUESTS, true);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), paramOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable의 고객 수 변경을 요청 시, 동작이 올바르게 수행된다.")
    @Test
    void changeNumberOfGuestTest() {
        OrderTable savedOrderTable = tableService.create(orderTable);
        OrderTable paramOrderTable = KitchenPosClassCreator
                .createOrderTable(TEST_ORDER_TABLE_NUMBER_OF_GUESTS_CHANGED, false);

        OrderTable changedTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), paramOrderTable);

        assertEquals(changedTable.getNumberOfGuests(), TEST_ORDER_TABLE_NUMBER_OF_GUESTS_CHANGED);
    }

    @DisplayName("예외 테스트 : OrderTable의 고객 수 변경을 요청 시, 고객수가 0 미만이면 예외가 발생한다.")
    @Test
    void changeNumberOfGuestWithNegativeNumberExceptionTest() {
        OrderTable savedOrderTable = tableService.create(orderTable);
        OrderTable paramOrderTable = KitchenPosClassCreator
                .createOrderTable(TEST_ORDER_TABLE_NUMBER_OF_GUESTS_INVALID, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), paramOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : OrderTable의 고객 수 변경을 요청 시, 잘못된 ID가 전달되면 예외가 발생한다.")
    @Test
    void changeNumberOfGuestWithInvalidIdExceptionTest() {
        OrderTable paramOrderTable = KitchenPosClassCreator
                .createOrderTable(TEST_ORDER_TABLE_NUMBER_OF_GUESTS_INVALID, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(TEST_INVALID_TABLE_ID, paramOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
