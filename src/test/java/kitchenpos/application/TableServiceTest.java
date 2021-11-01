package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DisplayName("TableService를 테스트한다.")
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderTableDao.deleteAll();
    }

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() {
        //given
        OrderTable orderTable = new OrderTable(null, null, 0, false);

        //when
        OrderTable result = tableService.create(orderTable);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(0);
        assertThat(result.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        //given
        OrderTable orderTable = new OrderTable(null, null, 0, false);
        tableService.create(orderTable);

        //when
        List<OrderTable> results = tableService.list();

        //then
        assertThat(results).hasSize(1);
        OrderTable result = results.get(0);
        assertThat(result.getNumberOfGuests()).isEqualTo(0);
        assertThat(result.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 비움 상태를 수정한다.")
    @Test
    void changeEmpty() {
        //given
        OrderTable orderTable = new OrderTable(null, null, 0, false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        //when
        tableService.changeEmpty(savedOrderTable.getId(), new OrderTable(null, null, 0, true));

        //then
        OrderTable result = tableService.list().get(0);
        assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블 비움 상태 수정시 주문 테이블이 존재하지 않으면 예외를 던진다.")
    @Test
    void changeEmpty_not_exist_order_table_exception() {
        OrderTable orderTable = new OrderTable(null, null, 0, false);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable(null, null, 0, true)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 비움 상태 수정시 단체 지정 테이블이면 예외를 던진다.")
    @Test
    void changeEmpty_group_table_exception() {
        OrderTable orderTable = new OrderTable(null, 1L, 0, true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable(null, null, 0, true)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 비움 상태 수정시 주문 테이블의 주문 상태가 '조리'라면 예외를 던진다.")
    @Test
    void changeEmpty_order_status_cooking_exception() {
        OrderTable orderTable = new OrderTable(null, 1L, 0, true);
        OrderTable savedOrderTable = tableService.create(orderTable);
        Order order = new Order(null, savedOrderTable.getId(), OrderStatus.COOKING.toString(), LocalDateTime.now(), null);
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable(null, null, 0, true)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 비움 상태 수정시 주문 테이블의 주문 상태가 '식사'라면 예외를 던진다.")
    @Test
    void changeEmpty_order_status_meal_exception() {
        OrderTable orderTable = new OrderTable(null, 1L, 0, true);
        OrderTable savedOrderTable = tableService.create(orderTable);
        Order order = new Order(null, savedOrderTable.getId(), OrderStatus.MEAL.toString(), LocalDateTime.now(), null);
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable(null, null, 0, true)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 방문한 손님 수를 수정한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = new OrderTable(null, null, 0, false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        //when
        tableService.changeNumberOfGuests(savedOrderTable.getId(), new OrderTable(null, null, 10, true));

        //then
        OrderTable result = tableService.list().get(0);
        assertThat(result.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("테이블을 방문한 손님 수를 수정시 손님수가 0이상이 아니라면 예외를 던진다.")
    @Test
    void changeNumberOfGuests_guest_number_exception() {
        OrderTable orderTable = new OrderTable(null, null, 0, false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), new OrderTable(null, null, -10, true)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 방문한 손님 수를 수정시 주문 테이블이 존재하지 않으면 예외를 던진다.")
    @Test
    void changeNumberOfGuests_not_exist_table_exception() {
        OrderTable orderTable = new OrderTable(null, null, 0, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(null, null, 10, true)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}