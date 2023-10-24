package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Money;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.KitchenPosException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineCreateRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @Nested
    class create {

        @Test
        void 주문_테이블_식별자에_대한_주문_테이블이_없으면_예외() {
            // given
            var orderLineCreateRequests = List.of(
                new OrderLineCreateRequest(1L, 1),
                new OrderLineCreateRequest(2L, 2)
            );
            var request = new OrderCreateRequest(4885L, orderLineCreateRequests);
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("해당 주문 테이블이 없습니다. orderTableId=4885");
        }

        @Test
        void 주문_목록_항목이_비어있으면_예외() {
            // given
            List<OrderLineCreateRequest> orderLineCreateRequests = Collections.emptyList();
            var request = new OrderCreateRequest(4885L, orderLineCreateRequests);
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(new OrderTable(4885L, false, 0)));

            // when
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("주문 목록 항목은 비어있을 수 없습니다.");
        }

        @Test
        void 메뉴_식별자에_대한_메뉴가_없으면_예외() {
            // given
            var orderLineCreateRequests = List.of(
                new OrderLineCreateRequest(1L, 1),
                new OrderLineCreateRequest(2L, 2),
                new OrderLineCreateRequest(4885L, 3),
                new OrderLineCreateRequest(4886L, 4)
            );
            var request = new OrderCreateRequest(4885L, orderLineCreateRequests);
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(new OrderTable(4885L, false, 0)));
            given(menuRepository.findByIdIn(anyList()))
                .willReturn(List.of(
                    new Menu(1L, "소주", Money.from(5000), new MenuGroup(1L, "주류")),
                    new Menu(2L, "맥주", Money.from(6000), new MenuGroup(1L, "주류"))
                ));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("존재하지 않는 메뉴가 있습니다. notExistMenuIds=[4885, 4886]");
        }

        @Test
        void 성공() {
            // given
            var orderLineCreateRequests = List.of(
                new OrderLineCreateRequest(1L, 1),
                new OrderLineCreateRequest(2L, 2)
            );
            var request = new OrderCreateRequest(4885L, orderLineCreateRequests);
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(new OrderTable(4885L, false, 0)));
            given(menuRepository.findByIdIn(anyList()))
                .willReturn(List.of(
                    new Menu(1L, "소주", Money.from(5000), new MenuGroup(1L, "주류")),
                    new Menu(2L, "맥주", Money.from(6000), new MenuGroup(1L, "주류"))
                ));
            given(orderRepository.save(any(Order.class)))
                .willAnswer(invoke -> invoke.getArgument(0));

            // when
            var actual = orderService.create(request);

            // then
            assertThat(actual.getOrderTableId()).isEqualTo(4885L);
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        }
    }

    @Nested
    class changeOrderStatus {

        @Test
        void 주문_식별자에_대한_주문이_없으면_예외() {
            // given
            given(orderRepository.findById(anyLong()))
                .willReturn(Optional.empty());
            // when
            assertThatThrownBy(() -> orderService.changeOrderStatus(4885L, OrderStatus.COMPLETION))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("해당 주문이 없습니다. orderId=4885");
        }

        @Test
        void 성공() {
            // given
            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
            OrderTable orderTable = new OrderTable(1L, false, 0);
            Order order = new Order(4885L, OrderStatus.COOKING, orderedTime, orderTable);
            given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(order));

            // when
            orderService.changeOrderStatus(4885L, OrderStatus.COMPLETION);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
        }
    }
}
