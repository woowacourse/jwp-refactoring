package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable(0, true);

        OrderTable savedOrderTable = tableService.create(orderTable);

        OrderTable findOrderTable = orderTableDao.findById(savedOrderTable.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(findOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(findOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @DisplayName("1명 이상의 손님과 함께 빈 테이블로 등록할 수 없다.")
    @Test
    void createException1() {
        int numberOfGuests = 1;
        OrderTable orderTable = new OrderTable(numberOfGuests, true);

        assertThatThrownBy(() -> tableService.create(orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%d명 : 1명 이상의 손님과 함께 빈 테이블로 등록할 수 없습니다.", numberOfGuests);
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void list() {
        orderTableDao.save(new OrderTable(0, true));
        orderTableDao.save(new OrderTable(0, true));
        orderTableDao.save(new OrderTable(0, true));

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(3);
    }

    @DisplayName("빈 테이블 설정 또는 해지할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"true,false", "false,true"})
    void changeEmpty(boolean input, boolean result) {
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(0, input));
        OrderTable changedOrderTable = new OrderTable(0, result);

        OrderTable emptyTable = tableService.changeEmpty(savedOrderTable.getId(), changedOrderTable);

        assertThat(emptyTable.isEmpty()).isEqualTo(result);
    }

    @DisplayName("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
    @ParameterizedTest
    @CsvSource(value = {"true", "false"})
    void changeEmptyException1(boolean isEmpty) {
        OrderTable orderTable = new OrderTable(0, true);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Collections.singletonList(orderTable));
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        orderTable.setTableGroupId(savedTableGroup.getId());

        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        OrderTable changedOrderTable = new OrderTable(0, isEmpty);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), changedOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
    }

    @DisplayName("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
    @ParameterizedTest
    @CsvSource(value = {"COOKING,true", "MEAL,true", "COOKING,false", "MEAL,false"})
    void changeEmptyException2(OrderStatus orderStatus, boolean isEmpty) {
        OrderTable orderTable = new OrderTable(0, true);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Order order = new Order(savedOrderTable.getId(), orderStatus.name(), LocalDateTime.now(), new ArrayList<>());
        orderDao.save(order);

        OrderTable changedOrderTable = new OrderTable(0, isEmpty);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), changedOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
    }

    @DisplayName("방문한 손님 수를 입력할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(0, false));
        OrderTable tenGuestOrderTable = orderTableDao.save(new OrderTable(10, true));

        OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), tenGuestOrderTable);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(tenGuestOrderTable.getNumberOfGuests());
    }

    @DisplayName("방문한 손님 수가 0명 미만이면 입력할 수 없다.")
    @Test
    void changeNumberOfGuestsException1() {
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(0, false));
        OrderTable wrongOrderTable = orderTableDao.save(new OrderTable(-1, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), wrongOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%d명 : 방문한 손님 수가 0명 미만이면 입력할 수 없습니다.", wrongOrderTable.getNumberOfGuests());
    }

    @DisplayName("빈 테이블은 방문한 손님 수를 입력할 수 없다.")
    @Test
    void changeNumberOfGuestsException2() {
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(0, true));
        OrderTable wrongOrderTable = orderTableDao.save(new OrderTable(5, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), wrongOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블은 방문한 손님 수를 입력할 수 없다.");
    }

    @AfterEach
    void tearDown() {
        orderDao.deleteAll();
        orderTableDao.deleteAll();
    }
}