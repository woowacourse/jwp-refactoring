package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.request.OrderTableChangeRequestDto;
import kitchenpos.application.dto.request.OrderTableCreateRequestDto;
import kitchenpos.application.dto.response.OrderTableResponseDto;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TableService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("OrderTable을 저장 및 반환한다.")
        @Test
        void it_saves_and_returns_order_table() {
            // given
            OrderTableCreateRequestDto orderTableCreateRequestDto =
                new OrderTableCreateRequestDto(10, false);
            OrderTable expected = new OrderTable(1L, new TableGroup(), 10, false, new ArrayList<>());
            given(orderTableRepository.save(any(OrderTable.class))).willReturn(expected);

            // when
            OrderTableResponseDto response = tableService.create(orderTableCreateRequestDto);

            // then
            assertThat(response).usingRecursiveComparison()
                .ignoringFields("tableGroupResponseDto")
                .isEqualTo(new OrderTableResponseDto(1L, 10, null, false));

            verify(orderTableRepository, times(1)).save(any(OrderTable.class));
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class Describe_list {

        @DisplayName("OrderTable을 목록을 반환한다.")
        @Test
        void it_returns_order_table_list() {
            // given
            OrderTable orderTable1 =
                new OrderTable(1L, new TableGroup(), 10, false, new ArrayList<>());
            OrderTable orderTable2 =
                new OrderTable(2L, new TableGroup(), 10, false, new ArrayList<>());
            given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2));

            // when
            List<OrderTableResponseDto> list = tableService.list();

            // then
            assertThat(list).hasSize(2);

            verify(orderTableRepository, times(1)).findAll();
        }
    }

    @DisplayName("changeEmpty 메서드는")
    @Nested
    class Describe_changeEmpty {

        @DisplayName("ID에 해당하는 OrderTable이 없다면")
        @Nested
        class Context_order_table_not_found {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTableChangeRequestDto orderTableChangeRequestDto =
                    new OrderTableChangeRequestDto(1L, 10, false);
                given(orderTableRepository.findById(1L)).willReturn(Optional.empty());

                // when, then
                assertThatCode(() -> tableService.changeEmpty(orderTableChangeRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable이 존재하지 않습니다.");

                verify(orderTableRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("OrderTable이 이미 특정 TableGroup에 속한 경우")
        @Nested
        class Context_order_table_has_group {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTable orderTable =
                    new OrderTable(1L, new TableGroup(), 10, false, new ArrayList<>());
                OrderTableChangeRequestDto orderTableChangeRequestDto =
                    new OrderTableChangeRequestDto(1L, 10, false);
                given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

                // when, then
                assertThatCode(() -> tableService.changeEmpty(orderTableChangeRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable이 속한 TableGroup이 존재합니다.");

                verify(orderTableRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("OrderTable에 속한 Order가 조리중 혹은 식사중이라면")
        @Nested
        class Context_order_status_cooking_or_meal {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                List<Order> orders = Arrays.asList(new Order(new OrderTable(10, false), OrderStatus.COOKING));
                OrderTable orderTable =
                    new OrderTable(1L, null, 10, false, orders);
                OrderTableChangeRequestDto orderTableChangeRequestDto =
                    new OrderTableChangeRequestDto(1L, 10, false);
                given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

                // when, then
                assertThatCode(() -> tableService.changeEmpty(orderTableChangeRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable에 속한 Order 중 일부가 조리 혹은 식사 중입니다.");

                verify(orderTableRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("정상적인 경우")
        @Nested
        class Context_valid_condition {

            @DisplayName("OrderTable의 상태를 변경하고 반환한다.")
            @Test
            void it_changes_and_returns_order_table_status() {
                // given
                List<Order> orders = Arrays.asList(
                    new Order(new OrderTable(10, false), OrderStatus.COMPLETION));
                OrderTable orderTable =
                    new OrderTable(1L, null, 10, false, orders);
                OrderTableChangeRequestDto orderTableChangeRequestDto =
                    new OrderTableChangeRequestDto(1L, 10, true);
                given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

                // when
                OrderTableResponseDto response = tableService.changeEmpty(orderTableChangeRequestDto);

                assertThat(response.isEmpty()).isTrue();

                verify(orderTableRepository, times(1)).findById(1L);
            }
        }
    }

    @DisplayName("changeNumberOfGuest 메서드는")
    @Nested
    class Describe_changeNumberOfGuest {

        @DisplayName("OrderTable을 조회할 수 없는 경우")
        @Nested
        class Context_order_table_not_found {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTableChangeRequestDto orderTableChangeRequestDto =
                    new OrderTableChangeRequestDto(1L, 10, false);
                given(orderTableRepository.findById(1L)).willReturn(Optional.empty());

                // when, then
                assertThatCode(() -> tableService.changeNumberOfGuests(orderTableChangeRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable이 존재하지 않습니다.");

                verify(orderTableRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("변경하려는 손님 숫자가 음수인 경우")
        @Nested
        class Context_number_of_guests_negative {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTableChangeRequestDto orderTableChangeRequestDto =
                    new OrderTableChangeRequestDto(1L, -10, false);
                OrderTable orderTable =
                    new OrderTable(1L, new TableGroup(), 10, false, new ArrayList<>());
                given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

                // when, then
                assertThatCode(() -> tableService.changeNumberOfGuests(orderTableChangeRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("변경하려는 손님 수는 음수일 수 없습니다.");

                verify(orderTableRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("조회된 OrderTable이 비어있는 경우")
        @Nested
        class Context_saved_order_table_is_empty {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTableChangeRequestDto orderTableChangeRequestDto =
                    new OrderTableChangeRequestDto(1L, 10, false);
                OrderTable orderTable =
                    new OrderTable(1L, new TableGroup(), 10, true, new ArrayList<>());
                given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

                // when, then
                assertThatCode(() -> tableService.changeNumberOfGuests(orderTableChangeRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable이 비어있는 상태입니다.");

                verify(orderTableRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("정상적인 경우")
        @Nested
        class Context_valid_condition {

            @DisplayName("OrderTable의 손님 수를 변경한다.")
            @Test
            void it_changes_and_returns_order_table() {
                // given
                OrderTableChangeRequestDto orderTableChangeRequestDto =
                    new OrderTableChangeRequestDto(1L, 369, false);
                OrderTable orderTable =
                    new OrderTable(1L, new TableGroup(), 10, false, new ArrayList<>());
                given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

                // when
                OrderTableResponseDto response = tableService.changeNumberOfGuests(orderTableChangeRequestDto);

                // then
                assertThat(response.getNumberOfGuests()).isEqualTo(369);

                verify(orderTableRepository, times(1)).findById(1L);
            }
        }
    }
}
