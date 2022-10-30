package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.application.dto.request.ChangeNumberOfGuestRequest;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블을 생성한다")
    @Test
    void create() {
        final OrderTableResponse response = tableService.create(new OrderTableRequest(0, true));

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("주문 테이블 전체 목록을 조회한다")
    @Test
    void findAll() {
        final List<OrderTableResponse> responses = tableService.list();

        assertThat(responses).hasSize(8);
    }

    @DisplayName("빈 테이블로 변경할 테이블이 존재하지 않다면, 예외를 발생한다")
    @Test
    void does_not_exist_order_table_exception() {
        assertThatThrownBy(() -> tableService.changeEmpty(0L, new OrderTableRequest(0, true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테이블이 존재하지 않습니다.");
    }

    @DisplayName("빈 테이블로 변경할 때 해당 테이블이 단체로 지정되어 있다면, 예외를 발생한다")
    @Test
    void grouped_table_exception() {
        // given
        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(
                Arrays.asList(new OrderTable(10, false), new OrderTable(10, false))));

        final OrderTable orderTable = new OrderTable(savedTableGroup.getId(), 10, false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTableRequest(0, true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 테이블은 변경할 수 없습니다.");
    }

    @DisplayName("빈 테이블로 변경할 때 주문 상테가 cooking 또는 meal 이라면, 예외를 발생한다")
    @ValueSource(strings = {"COOKING", "MEAL"})
    @ParameterizedTest
    void order_status_not_completion_exception(final String status) {
        final Order order = orderDao.save(new Order(1L, status, LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L))));

        assertThatThrownBy(() -> tableService.changeEmpty(order.getOrderTableId(), new OrderTableRequest(0, true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 또는 식사 중이라면 테이블 상태를 변경할 수 없습니다.");
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다")
    @Test
    void changeEmptyTable() {
        final OrderTable savedOrderTable = orderTableDao.save(new OrderTable(2, false));

        orderDao.save(new Order(1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L))));

        final OrderTableResponse response = tableService.changeEmpty(savedOrderTable.getId(),
                new OrderTableRequest(2, true));

        assertThat(response.isEmpty()).isTrue();
    }

    @DisplayName("변경할 손님 수가 0보자 작은 경우, 예외를 발생한다")
    @Test
    void negative_number_of_guests_exception() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new ChangeNumberOfGuestRequest(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("변경할 손님의 수는 0이상이어야 합니다.");
    }

    @DisplayName("손님 수를 변경할 때 존재하지 않는 테이블인 경우, 예외를 발생한다")
    @Test
    void notfound_order_table_exception() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, new ChangeNumberOfGuestRequest(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 존재하지 않습니다.");
    }

    @DisplayName("손님 수를 변경할 때 빈 테이블인 경우, 예외를 발생한다")
    @Test
    void empty_order_table_exception() {
        final OrderTable orderTable = orderTableDao.save(new OrderTable(2, true));

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), new ChangeNumberOfGuestRequest(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블의 손님 수는 변경할 수 없습니다.");
    }

    @DisplayName("주문 테이블 손님 수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        final OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));

        final OrderTableResponse response = tableService.changeNumberOfGuests(orderTable.getId(),
                new ChangeNumberOfGuestRequest(20));

        assertThat(response.getNumberOfGuests()).isEqualTo(20);
    }
}
