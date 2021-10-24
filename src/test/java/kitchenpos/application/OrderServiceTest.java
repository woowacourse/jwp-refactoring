package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.request.OrderChangeRequestDto;
import kitchenpos.application.dto.request.OrderCreateRequestDto;
import kitchenpos.application.dto.request.OrderLineItemRequestDto;
import kitchenpos.application.dto.response.OrderResponseDto;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("OrderService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("Order가 속한 OrderTable을 조회할 수 없는 경우")
        @Nested
        class Context_order_table_not_found {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderCreateRequestDto orderCreateRequestDto =
                    new OrderCreateRequestDto(1L, "COOKING", Collections.emptyList());
                given(orderTableRepository.findById(1L)).willReturn(Optional.empty());

                // when. then
                assertThatCode(() -> orderService.create(orderCreateRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 OrderTable ID입니다.");

                verify(orderTableRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("Order가 속한 OrderTable이 비어있는 경우")
        @Nested
        class Context_order_table_empty {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderCreateRequestDto orderCreateRequestDto =
                    new OrderCreateRequestDto(1L, "COOKING", Collections.emptyList());
                OrderTable orderTable = new OrderTable(10, true);
                given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

                // when. then
                assertThatCode(() -> orderService.create(orderCreateRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable이 비어있는 상태입니다.");

                verify(orderTableRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("Order에 속한 OrderLineItem이 속한 Menu들이 DB에 저장되지 않은 경우")
        @Nested
        class Context_menu_not_persisted {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                List<OrderLineItemRequestDto> orderLineItemRequestDtos = Arrays.asList(
                    new OrderLineItemRequestDto(1L, 10L),
                    new OrderLineItemRequestDto(2L, 10L)
                );
                Menu menu1 = new Menu("m1", BigDecimal.valueOf(10), new MenuGroup(1L, "mg1"));
                OrderCreateRequestDto orderCreateRequestDto =
                    new OrderCreateRequestDto(1L, "COOKING", orderLineItemRequestDtos);
                OrderTable orderTable = new OrderTable(10, false);
                given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
                given(menuRepository.findById(1L)).willReturn(Optional.of(menu1));
                given(menuRepository.findById(2L)).willReturn(Optional.empty());

                // when. then
                assertThatCode(() -> orderService.create(orderCreateRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 항목이 속한 Menu가 DB에 존재하지 않습니다.");

                verify(orderTableRepository, times(1)).findById(1L);
                verify(menuRepository, times(2)).findById(anyLong());
            }
        }

        @DisplayName("Order에 속한 OrderLineItem 컬렉션이 비어있는 경우")
        @Nested
        class Context_orderLineItem_empty {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderCreateRequestDto orderCreateRequestDto =
                    new OrderCreateRequestDto(1L, "COOKING", Collections.emptyList());
                OrderTable orderTable = new OrderTable(10, false);
                given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

                // when. then
                assertThatCode(() -> orderService.create(orderCreateRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문에 포함된 주문 항목이 없습니다.");

                verify(orderTableRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("그 외 정상적인 경우")
        @Nested
        class Context_valid_condition {

            @DisplayName("Order를 정상 생성 및 반환한다.")
            @Test
            void it_saves_and_returns_order() {
                // given
                List<OrderLineItemRequestDto> orderLineItemRequestDtos = Arrays.asList(
                    new OrderLineItemRequestDto(1L, 10L),
                    new OrderLineItemRequestDto(2L, 10L)
                );
                Menu menu1 = new Menu("m1", BigDecimal.valueOf(10), new MenuGroup(1L, "mg1"));
                Menu menu2 = new Menu("m2", BigDecimal.valueOf(10), new MenuGroup(1L, "mg1"));
                OrderCreateRequestDto orderCreateRequestDto =
                    new OrderCreateRequestDto(1L, "COOKING", orderLineItemRequestDtos);
                OrderTable orderTable = new OrderTable(10, false);
                Order order = new Order(1L, new OrderTable(10, false), OrderStatus.COOKING, new ArrayList<>());
                given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
                given(menuRepository.findById(1L)).willReturn(Optional.of(menu1));
                given(menuRepository.findById(2L)).willReturn(Optional.of(menu2));
                given(orderRepository.save(any(Order.class))).willReturn(order);

                // when
                OrderResponseDto response = orderService.create(orderCreateRequestDto);

                // then
                assertThat(response.getId()).isOne();

                verify(orderTableRepository, times(1)).findById(1L);
                verify(menuRepository, times(2)).findById(anyLong());
                verify(orderRepository, times(1)).save(any(Order.class));
            }
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class Describe_list {

        @DisplayName("Order 목록을 조회한다.")
        @Test
        void it_returns_order_list() {
            // given
            Order order1 = new Order(1L, new OrderTable(10, false), OrderStatus.COOKING, new ArrayList<>());
            Order order2 = new Order(2L, new OrderTable(10, false), OrderStatus.COOKING, new ArrayList<>());
            List<Order> orders = Arrays.asList(order1, order2);
            given(orderRepository.findAll()).willReturn(orders);

            // when
            List<OrderResponseDto> response = orderService.list();

            // then
            assertThat(response).hasSize(2);

            verify(orderRepository, times(1)).findAll();
        }
    }

    @DisplayName("changeOrderStatus 메서드는")
    @Nested
    class Describe_changeOrderStatus {

        @DisplayName("Order를 조회할 수 없으면")
        @Nested
        class Context_order_not_found {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderChangeRequestDto orderChangeRequestDto =
                    new OrderChangeRequestDto(1L, "COOKING");
                given(orderRepository.findById(1L)).willReturn(Optional.empty());

                // when, then
                assertThatCode(() -> orderService.changeOrderStatus(orderChangeRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 Order입니다.");

                verify(orderRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("조회된 Order의 상태가 COMPLETION인 경우")
        @Nested
        class Context_order_status_completion {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderChangeRequestDto orderChangeRequestDto =
                    new OrderChangeRequestDto(1L, "COOKING");
                Order order = new Order(1L, new OrderTable(10, false), OrderStatus.COMPLETION, new ArrayList<>());
                given(orderRepository.findById(1L)).willReturn(Optional.of(order));

                // when, then
                assertThatCode(() -> orderService.changeOrderStatus(orderChangeRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Order 상태를 변경할 수 없는 상황입니다.");

                verify(orderRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("그 외 정상적인 경우")
        @Nested
        class Context_valid_condition {

            @DisplayName("Order 상태가 변경된다.")
            @Test
            void it_changes_order_status() {
                // given
                OrderChangeRequestDto orderChangeRequestDto =
                    new OrderChangeRequestDto(1L, "MEAL");
                Order order = new Order(1L, new OrderTable(10, false), OrderStatus.COOKING, new ArrayList<>());
                given(orderRepository.findById(1L)).willReturn(Optional.of(order));

                // when
                OrderResponseDto response = orderService.changeOrderStatus(orderChangeRequestDto);

                // then
                assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());

                verify(orderRepository, times(1)).findById(1L);
            }
        }
    }
}
