package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    private List<OrderTable> standardTables;
    private OrderTable standardTable;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
        standardTable = new OrderTable();
        standardTable.setId(1L);
        standardTable.setEmpty(true);
        standardTable.setNumberOfGuests(10);
        standardTable.setTableGroupId(1L);

        standardTables = new ArrayList<>();
        standardTables.add(standardTable);
    }

    @DisplayName("테이블을 조회한다.")
    @Test
    void getTables() {
        //given
        given(orderTableDao.findAll()).willReturn(standardTables);

        //when
        List<OrderTable> orderTables = tableService.list();

        //then
        assertThat(orderTables.size()).isEqualTo(1);
    }

    @DisplayName("테이블을 추가한다.")
    @Test
    void createTable() {
        //given
        given(orderTableDao.save(standardTable)).willReturn(standardTable);

        //when
        OrderTable orderTable = tableService.create(standardTable);

        //then
        assertAll(
            () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(10),
            () -> assertThat(orderTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("테이블을 빈 상태로 변경할 시에 테이블이 존재해야만 한다.")
    @Test
    void changeUnexsistedTableEmptyStatus() {
        //given
        given(orderTableDao.findById(1L)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 저장된 테이블 그룹에는 없어야 한다.")
    @Test
    void changeTableEmptyStatusOnSaveOrderTable() {
        //given
        standardTable.setTableGroupId(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.ofNullable(standardTable));

        //when

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요리 중이거나 식사중인 상태의 주문이 없어야 한다.")
    @Test
    void changeTableEmptyStatusWithCookAndMeal() {
        //given
        standardTable.setTableGroupId(null);
        given(orderTableDao.findById(1L)).willReturn(Optional.ofNullable(standardTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        //when

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 빈 상태를 변경한다.")
    @Test
    void changeTableEmptyStatus() {
        //given
        standardTable.setEmpty(false);
        standardTable.setTableGroupId(null);
        given(orderTableDao.findById(1L)).willReturn(Optional.ofNullable(standardTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(standardTable)).willReturn(standardTable);

        //when
        OrderTable orderTable = tableService.changeEmpty(1L, standardTable);

        //then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("테이블의 손님 수 변경 시에 손님이 최소 1명보단 많아야 한다.")
    @Test
    void changeTableNumberOfGuestWithZeroGuest() {
        //given
        standardTable.setNumberOfGuests(0);

        //when

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경 시에 테이블이 존재해야 한다.")
    @Test
    void changeUnexsitedTableNumberOfGuest() {
        //given
        given(orderTableDao.findById(1L)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경 시에 테이블이 비어있어선 안 된다.")
    @Test
    void changeTableNumberOfGuestWithEmpty() {
        //given
        standardTable.setEmpty(true);
        given(orderTableDao.findById(1L)).willReturn(Optional.ofNullable(standardTable));

        //when

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeTableNumberOfGuest() {
        //given
        standardTable.setEmpty(false);
        standardTable.setNumberOfGuests(5);
        given(orderTableDao.findById(1L)).willReturn(Optional.ofNullable(standardTable));
        given(orderTableDao.save(standardTable)).willReturn(standardTable);

        //when
        OrderTable orderTable = tableService.changeNumberOfGuests(1L, standardTable);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5L);
    }
}
