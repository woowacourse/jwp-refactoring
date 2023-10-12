package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void beforeEach() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(null);
    }

    @DisplayName("order table을 생성한다.")
    @Test
    void orderTable을_생성() {
        //given
        final OrderTable expected = orderTable;
        given(orderTableDao.save(expected))
                .willReturn(expected);

        //when
        final OrderTable actual = tableService.create(expected);

        //then
        assertSoftly(softly -> {
            softly.assertThat(actual).isEqualTo(expected);
            softly.assertThat(actual.getTableGroupId()).isNull();
        });
    }

    @DisplayName("전체 orderTable을 조회한다.")
    @Test
    void 전체_orderTable_조회() {
        //given
        final List<OrderTable> orderTables = List.of(orderTable);
        given(orderTableDao.findAll())
                .willReturn(orderTables);

        //when
        final List<OrderTable> actual = tableService.list();

        assertSoftly(softly -> {
            softly.assertThat(actual.size()).isEqualTo(orderTables.size());
        });
    }

    @DisplayName("orderTable의 empty를 변경한다.")
    @Test
    void orderTable_empty_변경() {
        //given
        given(orderTableDao.findById(orderTable.getId()))
                .willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);
        given(orderTableDao.save(orderTable))
                .willReturn(orderTable);

        final boolean expected = !orderTable.isEmpty();
        final OrderTable parameter = new OrderTable();
        parameter.setEmpty(expected);

        //when
        final OrderTable actual = tableService.changeEmpty(orderTable.getId(), parameter);

        //then
        assertSoftly(softly -> {
            softly.assertThat(actual.isEmpty()).isEqualTo(expected);
        });
    }

    @DisplayName("orderTable의 empty를 변경시 tableGroupId가 null이 아니면 실패한다.")
    @Test
    void tableGroupId가_null이_아니면_예외() {
        //given
        given(orderTableDao.findById(orderTable.getId()))
                .willReturn(Optional.of(orderTable));

        final boolean expected = !orderTable.isEmpty();
        final OrderTable parameter = new OrderTable();
        parameter.setEmpty(expected);
        orderTable.setTableGroupId(1L);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), parameter))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderTable의 empty를 변경시 order 상태가 cooking이거나 meal이면 예외를 던진다.")
    @Test
    void order상태가_cooking이거나_meal이면_예외() {
        //given
        given(orderTableDao.findById(orderTable.getId()))
                .willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        final boolean expected = !orderTable.isEmpty();
        final OrderTable parameter = new OrderTable();
        parameter.setEmpty(expected);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), parameter))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderTable의 guest 수를 변경한다.")
    @Test
    void orderTable의_guest_수_변경() {
        //given
        given(orderTableDao.findById(orderTable.getId()))
                .willReturn(Optional.of(orderTable));
        given(orderTableDao.save(orderTable))
                .willReturn(orderTable);
        final OrderTable parameter = orderTable;
        final int expected = 3;
        parameter.setNumberOfGuests(expected);

        //when
        final OrderTable actual = tableService.changeNumberOfGuests(parameter.getId(), parameter);

        //then
        assertSoftly(softly -> {
            softly.assertThat(actual.getNumberOfGuests()).isEqualTo(expected);
        });
    }

    @DisplayName("orderTable의 guest 수를 변경시 음수면 예외를 던진다.")
    @Test
    void orderTable의_guest_수가_음수면_예외() {
        //given
        final OrderTable parameter = orderTable;
        final int expected = -10;
        parameter.setNumberOfGuests(expected);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(parameter.getId(), parameter))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderTable의 guest 수를 변경시 isEmpty가 true면 예외를 던진다.")
    @Test
    void orderTable의_isEmpty가_true면_예외() {
        //given
        final OrderTable expected = orderTable;
        expected.setEmpty(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(expected.getId(), expected))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
