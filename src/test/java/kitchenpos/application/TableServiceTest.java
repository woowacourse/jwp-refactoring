package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = new OrderTable();

        given(orderTableDao.save(orderTable))
            .willReturn(new OrderTable() {{
                setId(1L);
            }});

        // when
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isEqualTo(1L);
    }

    @DisplayName("주문 테이블을 비어 있는 상태로 바꾼다.")
    @Test
    void changeEmpty() {
        // given
        final Long orderTableId = 1L;
        final OrderTable orderTable = new OrderTable() {{
            setId(orderTableId);
            setEmpty(true);
        }};

        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.of(new OrderTable() {{
                setId(orderTableId);
                setEmpty(false);
            }}));

        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(OrderStatus.COOKING.name(),
                                                                                  OrderStatus.MEAL.name())))
            .willReturn(false);

        given(orderTableDao.save(any(OrderTable.class)))
            .willReturn(new OrderTable() {{
                setId(orderTableId);
                setEmpty(true);
            }});

        // when
        final OrderTable savedOrderTable = tableService.changeEmpty(orderTableId, orderTable);

        // then
        assertThat(savedOrderTable.isEmpty()).isTrue();
        assertThat(savedOrderTable.getId()).isEqualTo(orderTableId);
    }

    @DisplayName("주문 테이블의 tableGroupId 가 존재하면 예외가 발생한다.")
    @Test
    void changeEmpty_failExistTableGroupId() {
        // given
        final Long orderTableId = 1L;
        final OrderTable orderTable = new OrderTable() {{
            setId(orderTableId);
            setEmpty(true);
        }};

        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.of(new OrderTable() {{
                setId(orderTableId);
                setEmpty(false);
                setTableGroupId(1L);
            }}));

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 COMPLETION 상태가 아니면 예외가 발생한다.")
    @Test
    void changeEmpty_failNotCompletionStatus() {
        // given
        final Long orderTableId = 1L;
        final OrderTable orderTable = new OrderTable() {{
            setId(orderTableId);
            setEmpty(true);
        }};

        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.of(new OrderTable() {{
                setId(orderTableId);
                setEmpty(false);
            }}));

        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(OrderStatus.COOKING.name(),
                                                                                  OrderStatus.MEAL.name())))
            .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final Long orderTableId = 1L;
        final OrderTable orderTable = new OrderTable() {{
            setId(orderTableId);
            setNumberOfGuests(5);
        }};

        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.of(new OrderTable() {{
                setId(orderTableId);
                setNumberOfGuests(1);
                setEmpty(false);
            }}));

        given(orderTableDao.save(any(OrderTable.class)))
            .willReturn(new OrderTable() {{
                setId(orderTableId);
                setEmpty(false);
                setNumberOfGuests(5);
            }});

        // when
        final OrderTable savedOrderTable = tableService.changeNumberOfGuests(orderTableId, orderTable);

        // then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(5);
        assertThat(savedOrderTable.getId()).isEqualTo(orderTableId);
    }

    @DisplayName("손님 수가 음수이면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_failNegativeNumberOfGuests() {
        // given
        final Long orderTableId = 1L;
        final OrderTable orderTable = new OrderTable() {{
            setId(orderTableId);
            setNumberOfGuests(-1);
        }};

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 테이블을 수정하려고 하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_failNotExistOrderTable() {
        // given
        final Long notExistedTableId = 0L;
        final OrderTable orderTable = new OrderTable() {{
            setId(notExistedTableId);
            setNumberOfGuests(5);
        }};

        given(orderTableDao.findById(notExistedTableId))
            .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistedTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_failEmptyTable() {
        // given
        final Long orderTableId = 1L;
        final OrderTable orderTable = new OrderTable() {{
            setId(orderTableId);
            setNumberOfGuests(5);
        }};

        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.of(new OrderTable() {{
                setId(orderTableId);
                setNumberOfGuests(1);
                setEmpty(true);
            }}));

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
