package kitchenpos.ordertable.application;

import kitchenpos.MockServiceTest;
import kitchenpos.ordertable.application.dto.CreateOrderTableDto;
import kitchenpos.ordertable.application.dto.OrderTableDto;
import kitchenpos.ordertable.application.dto.UpdateOrderTableEmptyDto;
import kitchenpos.ordertable.application.dto.UpdateOrderTableGuestNumberDto;
import kitchenpos.ordertable.domain.GuestNumber;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderValidator;
import kitchenpos.ordertable.exception.OrderTableException;
import kitchenpos.ordertable.exception.OrderTableGuestNumberException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

class TableServiceTest extends MockServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderValidator orderValidator;

    @Test
    void 주문테이블_목록을_조회한다() {
        // given
        OrderTable firstOrderTable = new OrderTable(
                new GuestNumber(1),
                true);
        OrderTable secondOrderTable = new OrderTable(
                new GuestNumber(10),
                true);
        BDDMockito.given(orderTableRepository.findAll())
                .willReturn(List.of(firstOrderTable, secondOrderTable));

        // when
        List<OrderTableDto> actual = tableService.list();

        // then
        Assertions.assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void 주문테이블을_추가한다() {
        // given
        OrderTable ordertable = new OrderTable(
                1L,
                new GuestNumber(1),
                true);
        BDDMockito.given(orderTableRepository.save(BDDMockito.any(OrderTable.class)))
                .willReturn(ordertable);

        CreateOrderTableDto createOrderTableDto =
                new CreateOrderTableDto(1, true);

        // when
        OrderTableDto actual = tableService.create(createOrderTableDto);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actual).isNotNull();
        softAssertions.assertThat(actual.getEmpty()).isTrue();
        softAssertions.assertThat(actual.getTableGroupId()).isNull();
        softAssertions.assertThat(actual.getNumberOfGuests()).isEqualTo(1);
        softAssertions.assertAll();
    }

    @Test
    void 주문테이블의_주문_가능_여부를_수정한다() {
        // given
        OrderTable ordertable = new OrderTable(
                1L,
                new GuestNumber(1),
                false);

        BDDMockito.given(orderTableRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(ordertable));
        UpdateOrderTableEmptyDto updateOrderTableEmptyDto
                = new UpdateOrderTableEmptyDto(ordertable.getId(), true);

        // when
        OrderTableDto actual = tableService.changeEmpty(updateOrderTableEmptyDto);

        // then
        Assertions.assertThat(actual.getEmpty()).isTrue();
    }

    @Test
    void 주문테이블의_주문_가능_여부를_수정할_때_해당_주문테이블이_존재하지_않으면_예외를_던진다() {
        // given
        OrderTable ordertable = new OrderTable(
                1L,
                new GuestNumber(1),
                false);

        BDDMockito.given(orderTableRepository.findById(ordertable.getId()))
                .willReturn(Optional.empty());

        UpdateOrderTableEmptyDto updateOrderTableEmptyDto
                = new UpdateOrderTableEmptyDto(ordertable.getId(), true);

        // when, then
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(updateOrderTableEmptyDto))
                .isInstanceOf(OrderTableException.class);
    }

    @Test
    void 주문테이블의_주문_가능_여부를_수정할_때_해당_주문테이블의_테이블_그룹이_존재하면_예외를_던진다() {
        // given
        OrderTable ordertable = new OrderTable(
                1L,
                new GuestNumber(1),
                true);

        ordertable.changeTableGroupId(1L);

        BDDMockito.given(orderTableRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(ordertable));

        UpdateOrderTableEmptyDto updateOrderTableEmptyDto
                = new UpdateOrderTableEmptyDto(ordertable.getId(), true);

        // when, then
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(updateOrderTableEmptyDto))
                .isInstanceOf(OrderTableException.class);
    }

    @Test
    void 주문테이블의_주문_가능_여부를_수정할_때_해당_주문테이블의_주문_중_COOKING_또는_MEAL_상태가_있으면_예외를_던진다() {
        // given
        OrderTable ordertable = new OrderTable(
                1L,
                new GuestNumber(1),
                false);

        BDDMockito.given(orderTableRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(ordertable));
        BDDMockito.willThrow(new OrderTableException("주문테이블에 속한 주문이 요리중 또는 식사중이므로 상태를 변경할 수 없습니다."))
                .given(orderValidator)
                .validateOrder(BDDMockito.anyLong());

        UpdateOrderTableEmptyDto updateOrderTableEmptyDto
                = new UpdateOrderTableEmptyDto(ordertable.getId(), true);

        // when, then
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(updateOrderTableEmptyDto))
                .isInstanceOf(OrderTableException.class);
    }

    @Test
    void 주문테이블의_손님_수_를_수정할_때_해당_주문테이블이_존재하지_않으면_예외를_던진다() {
        // given
        BDDMockito.given(orderTableRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.empty());

        UpdateOrderTableGuestNumberDto updateOrderTableGuestNumberDto
                = new UpdateOrderTableGuestNumberDto(1L, 10);

        // when, then
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(updateOrderTableGuestNumberDto))
                .isInstanceOf(OrderTableException.class);
    }

    @Test
    void 주문테이블의_손님_수_를_수정할_때_손님_수가_0_미만이면_예외를_던진다() {
        // given
        UpdateOrderTableGuestNumberDto updateOrderTableGuestNumberDto
                = new UpdateOrderTableGuestNumberDto(1L, -1);

        // when, then
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(updateOrderTableGuestNumberDto))
                .isInstanceOf(OrderTableGuestNumberException.class);
    }
}
