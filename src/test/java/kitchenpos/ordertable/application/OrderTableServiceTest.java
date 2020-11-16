package kitchenpos.ordertable.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.repository.TableGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderTableServiceTest {

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        OrderTableCreateRequest request = new OrderTableCreateRequest(0, true);

        OrderTable savedOrderTable = orderTableService.create(request);

        OrderTable findOrderTable = orderTableRepository.findById(savedOrderTable.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(findOrderTable).usingRecursiveComparison().isEqualTo(savedOrderTable);
    }

    @DisplayName("1명 이상의 손님과 함께 빈 테이블로 등록할 수 없다.")
    @Test
    void createException1() {
        int numberOfGuests = 1;
        OrderTableCreateRequest request = new OrderTableCreateRequest(numberOfGuests, true);

        assertThatThrownBy(() -> orderTableService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%d명 : 1명 이상의 손님과 함께 빈 테이블로 등록할 수 없습니다.", numberOfGuests);
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void list() {
        orderTableRepository.save(new OrderTable(0, true));
        orderTableRepository.save(new OrderTable(0, true));
        orderTableRepository.save(new OrderTable(0, true));

        List<OrderTable> orderTables = orderTableService.list();

        assertThat(orderTables).hasSize(3);
    }

    @DisplayName("빈 테이블 설정 또는 해지할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"true,false", "false,true"})
    void changeEmpty(boolean input, boolean result) {
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(0, input));
        OrderTable changedOrderTable = new OrderTable(0, result);

        OrderTable emptyTable = orderTableService.changeEmpty(savedOrderTable.getId(), changedOrderTable);

        assertThat(emptyTable.isEmpty()).isEqualTo(result);
    }

    @DisplayName("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
    @ParameterizedTest
    @CsvSource(value = {"true", "false"})
    void changeEmptyException1(boolean isEmpty) {
        OrderTable orderTable = new OrderTable(0, true);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        orderTable.setTableGroup(savedTableGroup);

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        OrderTable changedOrderTable = new OrderTable(0, isEmpty);

        assertThatThrownBy(() -> orderTableService.changeEmpty(savedOrderTable.getId(), changedOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
    }

    @DisplayName("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
    @ParameterizedTest
    @CsvSource(value = {"COOKING,true", "MEAL,true", "COOKING,false", "MEAL,false"})
    void changeEmptyException2(OrderStatus orderStatus, boolean isEmpty) {
        OrderTable orderTable = new OrderTable(0, true);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        Order order = new Order(savedOrderTable, orderStatus);
        orderRepository.save(order);

        OrderTable changedOrderTable = new OrderTable(0, isEmpty);

        assertThatThrownBy(() -> orderTableService.changeEmpty(savedOrderTable.getId(), changedOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
    }

    @DisplayName("방문한 손님 수를 입력할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(0, false));
        OrderTable tenGuestOrderTable = orderTableRepository.save(new OrderTable(10, false));

        OrderTable changedOrderTable = orderTableService.changeNumberOfGuests(savedOrderTable.getId(), tenGuestOrderTable);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(tenGuestOrderTable.getNumberOfGuests());
    }

    @DisplayName("방문한 손님 수가 0명 미만이면 입력할 수 없다.")
    @Test
    void changeNumberOfGuestsException1() {
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(0, false));
        OrderTable wrongOrderTable = orderTableRepository.save(new OrderTable(-1, true));

        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(savedOrderTable.getId(), wrongOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%d명 : 방문한 손님 수가 0명 미만이면 입력할 수 없습니다.", wrongOrderTable.getNumberOfGuests());
    }

    @DisplayName("빈 테이블은 방문한 손님 수를 입력할 수 없다.")
    @Test
    void changeNumberOfGuestsException2() {
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(0, true));
        OrderTable wrongOrderTable = orderTableRepository.save(new OrderTable(5, false));

        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(savedOrderTable.getId(), wrongOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블은 방문한 손님 수를 입력할 수 없다.");
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
    }
}