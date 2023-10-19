package kitchenpos.application;

import kitchenpos.application.dto.CreateOrderTableDto;
import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.application.dto.UpdateOrderTableEmptyDto;
import kitchenpos.application.dto.UpdateOrderTableGuestNumberDto;
import kitchenpos.domain.order.GuestNumber;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.exception.OrderTableException;
import kitchenpos.exception.OrderTableGuestNumberException;
import kitchenpos.repository.OrderTableRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class TableServiceTest extends MockServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderTableRepository orderTableRepository;

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
        Order order = new Order(LocalDateTime.now());
        order.completeOrder();
        OrderTable ordertable = new OrderTable(
                1L,
                new GuestNumber(1),
                false);
        ordertable.addOrder(order);

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
                false);

        Order order = new Order(LocalDateTime.now());
        order.completeOrder();
        ordertable.addOrder(order);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        ordertable.changeTableGroup(tableGroup);

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

        Order order = new Order(LocalDateTime.now());
        ordertable.addOrder(order);

        BDDMockito.given(orderTableRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(ordertable));

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
