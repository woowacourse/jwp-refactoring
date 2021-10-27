package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class TableServiceTest {
    @MockBean
    private OrderDao orderDao;

    @MockBean
    private OrderTableDao orderTableDao;

    @Autowired
    TableService tableService;

    @Test
    @DisplayName("테이블을 저장할 수 있다.")
    void create() {
        // given
        OrderTable orderTable = new OrderTable();

        given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(orderTable);

        // when
        OrderTable actual = tableService.create(orderTable);

        // then
        assertThat(actual).isEqualTo(orderTable);
    }

    @Test
    @DisplayName("테이블 목록을 불러올 수 있다.")
    void list() {
        // given
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        List<OrderTable> expected = Arrays.asList(orderTable1, orderTable2);
        given(orderTableDao.findAll())
                .willReturn(expected);

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @ParameterizedTest
    @CsvSource({"true", "false"})
    @DisplayName("테이블이 주문을 받을 수 있는지 없는지를 바꿀 수 있다.")
    void changeEmpty(boolean isEmptyTable) {
        // given
        long targetOrderTableId = 1L;

        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setId(targetOrderTableId);
        targetOrderTable.setEmpty(!isEmptyTable);

        OrderTable inputOrderTable = new OrderTable();
        inputOrderTable.setEmpty(isEmptyTable);

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(targetOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any(List.class)))
                .willReturn(false);
        given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(targetOrderTable);

        // when
        OrderTable actual = tableService.changeEmpty(targetOrderTableId, inputOrderTable);

        // then
        assertThat(actual.isEmpty()).isEqualTo(isEmptyTable);
    }

    @Test
    @DisplayName("테이블의 주문 가능 여부를 변경시킬 때 해당하는 Id의 테이블이 없다면 예외를 발생시킨다.")
    void changeEmptyFailWhenTableIsNotExists() {
        // given
        long targetOrderTableId = 1L;
        long wrongOrderTableId = 2L;

        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setId(targetOrderTableId);

        OrderTable inputOrderTable = new OrderTable();

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(wrongOrderTableId, inputOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 주문 가능 여부를 변경시킬 때 테이블 그룹에 등록되어 있다면 예외를 발생시킨다.")
    void changeEmptyFailWhenGroupIdExists() {
        // given
        long targetOrderTableId = 1L;
        long tableGroupId = 1L;

        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setTableGroupId(tableGroupId);
        targetOrderTable.setId(targetOrderTableId);

        OrderTable inputOrderTable = new OrderTable();

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(targetOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(targetOrderTableId, inputOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 주문 가능 여부를 변경시킬 때 테이블의 주문 상태가 요리중이거나 이미 식사중이라면 바꿀 수 없다.")
    void changeEmptyFailWhenStatusIsEitherCookingOrMeal() {
        // given
        long targetOrderTableId = 1L;

        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setId(targetOrderTableId);

        OrderTable inputOrderTable = new OrderTable();

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(targetOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any(List.class)))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(targetOrderTableId, inputOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 방문한 손님의 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        // given
        int numberOfGuestsToChange = 1;
        OrderTable inputOrderTable = new OrderTable();
        inputOrderTable.setNumberOfGuests(numberOfGuestsToChange);

        long targetOrderTableId = 1L;
        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setId(targetOrderTableId);
        targetOrderTable.setEmpty(false);

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(targetOrderTable));
        given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(targetOrderTable);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(targetOrderTableId, inputOrderTable);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuestsToChange);
    }

    @Test
    @DisplayName("테이블의 방문한 손님의 수를 음수로 변경하려고 할 경우 예외를 발생시킨다.")
    void changeNumberOfGuestsFailWhenInputNumberOfGuestsIsNegativeNumber() {
        // given
        int numberOfGuestsToChange = -1;
        OrderTable inputOrderTable = new OrderTable();
        inputOrderTable.setNumberOfGuests(numberOfGuestsToChange);

        long targetOrderTableId = 1L;

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(targetOrderTableId, inputOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 방문한 손님의 수를 변경할 때 주문이 불가능한 테이블이라면 예외를 발생시킨다.")
    void changeNumberOfGuestsFailWhenTableIsEmpty() {
        // given
        int numberOfGuestsToChange = 1;
        OrderTable inputOrderTable = new OrderTable();
        inputOrderTable.setNumberOfGuests(numberOfGuestsToChange);

        long targetOrderTableId = 1L;
        OrderTable targetOrderTable = new OrderTable();
        targetOrderTable.setId(targetOrderTableId);
        targetOrderTable.setEmpty(true);

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(targetOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(targetOrderTableId, inputOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
