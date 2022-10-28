package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.ApplicationTest;
import kitchenpos.application.request.OrderTableEmptyRequest;
import kitchenpos.application.request.OrderTableGuestModifyRequest;
import kitchenpos.application.request.OrderTableRequest;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void create() {
        OrderTableRequest request = new OrderTableRequest(10, false);

        OrderTableResponse response = tableService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void list() {
        List<OrderTableResponse> response = tableService.list();

        assertThat(response.size()).isEqualTo(8);
    }

    @Test
    void changeEmpty() {
        OrderTableEmptyRequest request = new OrderTableEmptyRequest(false);

        OrderTable changeOrderTable = tableService.changeEmpty(1L, request);

        assertThat(changeOrderTable.isEmpty()).isFalse();
    }

    @Test
    void changeEmptyThrowExceptionWhenStillCookingOrderTable() {
        OrderTableEmptyRequest request = new OrderTableEmptyRequest(false);
        orderDao.save(Order.of(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>()));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 중이거나 식사 중인 테이블이 존재합니다.");
    }

    @Test
    void changeEmptyThrowExceptionWhenStillMeal() {
        OrderTableEmptyRequest request = new OrderTableEmptyRequest(false);
        orderDao.save(Order.of(1L, OrderStatus.MEAL.name(), LocalDateTime.now(), new ArrayList<>()));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, request
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 중이거나 식사 중인 테이블이 존재합니다.");
    }

    @Test
    void changeNumberOfGuests() {
        int numberOfGuests = 2;
        OrderTableGuestModifyRequest request = new OrderTableGuestModifyRequest(numberOfGuests);
        orderTableDao.save(OrderTable.of(1L, null, 0, false));

        OrderTableResponse response = tableService.changeNumberOfGuests(1L, request);

        assertThat(response.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    void changeNumberOfGuestsThrowExceptionWhenNumberOfGuestIsNative() {
        OrderTable savedOrderTable = orderTableDao.save(OrderTable.of(1L, null, 0, false));
        OrderTableGuestModifyRequest request = new OrderTableGuestModifyRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Guest는 0명 미만일 수 없습니다.");
    }

    @Test
    void changeNumberOfGuestsThrowExceptionWhenNotExistOrderTable() {
        OrderTableGuestModifyRequest request = new OrderTableGuestModifyRequest(3);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuestsThrowExceptionWhenEmptyOrderTable() {
        OrderTableGuestModifyRequest request = new OrderTableGuestModifyRequest(3);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블입니다.");
    }
}
