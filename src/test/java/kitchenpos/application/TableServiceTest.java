package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class TableServiceTest extends MockServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Test
    void 주문테이블_목록을_조회한다() {
        // given
        OrderTable firstOrderTable = new OrderTable();
        firstOrderTable.setId(1L);
        firstOrderTable.setEmpty(true);
        OrderTable secondOrderTable = new OrderTable();
        secondOrderTable.setId(2L);
        secondOrderTable.setEmpty(false);

        List<OrderTable> expected = List.of(firstOrderTable, secondOrderTable);

        BDDMockito.given(orderTableDao.findAll())
                .willReturn(expected);

        // when
        List<OrderTable> actual = tableService.list();

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 주문테이블을_추가한다() {
        // given
        OrderTable ordertable = new OrderTable();
        ordertable.setEmpty(true);
        ordertable.setNumberOfGuests(10);

        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(1L);
        savedOrderTable.setEmpty(ordertable.isEmpty());
        savedOrderTable.setNumberOfGuests(ordertable.getNumberOfGuests());

        BDDMockito.given(orderTableDao.save(ordertable))
                .willReturn(savedOrderTable);

        // when
        OrderTable actual = tableService.create(ordertable);

        // then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(ordertable);
    }

    @Test
    void 주문테이블의_주문_가능_여부를_수정한다() {
        // given
        OrderTable expected = new OrderTable();
        expected.setId(1L);
        expected.setEmpty(true);

        Long argumentOrderId = 1L;
        OrderTable arguemntOrderTable = new OrderTable();
        arguemntOrderTable.setEmpty(true);

        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(1L);
        savedOrderTable.setEmpty(false);
        BDDMockito.given(orderTableDao.findById(argumentOrderId))
                .willReturn(Optional.of(savedOrderTable));

        BDDMockito.given(orderDao.existsByOrderTableIdAndOrderStatusIn(argumentOrderId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        BDDMockito.given(orderTableDao.save(savedOrderTable))
                .willReturn(savedOrderTable);

        // when
        OrderTable actual = tableService.changeEmpty(argumentOrderId, arguemntOrderTable);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 주문테이블의_주문_가능_여부를_수정할_때_해당_주문테이블이_존재하지_않으면_예외를_던진다() {
        // given
        Long orderId = 1L;
        OrderTable orderTable = new OrderTable();

        BDDMockito.given(orderTableDao.findById(orderId))
                .willReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(orderId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블의_주문_가능_여부를_수정할_때_해당_주문테이블의_테이블_그룹이_존재하면_예외를_던진다() {
        // given
        Long orderId = 1L;
        OrderTable orderTable = new OrderTable();

        OrderTable findOrderTable = new OrderTable();
        findOrderTable.setTableGroupId(1L);
        BDDMockito.given(orderTableDao.findById(orderId))
                .willReturn(Optional.of(findOrderTable));

        // when, then
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(orderId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블의_주문_가능_여부를_수정할_때_해당_주문테이블의_주문_중_COOKING_또는_MEAL_상태가_있으면_예외를_던진다() {
        // given
        Long orderId = 1L;
        OrderTable orderTable = new OrderTable();

        OrderTable findOrderTable = new OrderTable();
        BDDMockito.given(orderTableDao.findById(orderId))
                .willReturn(Optional.of(findOrderTable));

        BDDMockito.given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        // when, then
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(orderId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블의_손님_수_를_수정할_때_해당_주문테이블이_존재하지_않으면_예외를_던진다() {
        // given
        Long orderId = 1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);

        BDDMockito.given(orderTableDao.findById(orderId))
                .willReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블의_손님_수_를_수정할_때_손님_수가_0_미만이면_예외를_던진다() {
        // given
        Long orderId = 1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);

        OrderTable findOrderTable = new OrderTable();
        findOrderTable.setId(1L);
        findOrderTable.setEmpty(true);
        BDDMockito.given(orderTableDao.findById(orderId))
                .willReturn(Optional.of(findOrderTable));

        // when, then
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
