package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderChangeStatusRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Nested
    class create_성공_테스트 {

        @Test
        void 주문_생성을_할_수_있다() {
            // given
            final var tableGroup = tableGroupRepository.save(TableGroup.create());
            final var orderTable = orderTableRepository.save(new OrderTable(tableGroup, 3, false));
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            final var menu = menuRepository.save(new Menu("메뉴_이름", BigDecimal.valueOf(0), menuGroup, List.of()));
            final var orderLineitem = new OrderLineItem(menu, 5L);
            final var order = orderRepository.save(new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), List.of(orderLineitem)));
            orderLineItemRepository.save(new OrderLineItem(order, menu, 5));

            final var orderLineItemRequest = new OrderLineItemRequest(1L, 5L);
            final var request = new OrderCreateRequest(1L, List.of(orderLineItemRequest));

            // when
            final var actual = orderService.create(request);

            // then
            assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
        }
    }

    @Nested
    class create_실패_테스트 {

        @Test
        void 존재하지_않는_주문_테이블을_사용하면_예외를_반환한다() {
            // given
            final var tableGroup = tableGroupRepository.save(TableGroup.create());
            final var orderTable = orderTableRepository.save(new OrderTable(tableGroup, 3, false));
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            final var menu = menuRepository.save(new Menu("메뉴_이름", BigDecimal.valueOf(0), menuGroup, List.of()));
            final var orderLineItem = new OrderLineItem(menu, 5);
            orderRepository.save(new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), List.of(orderLineItem)));

            final var orderLineItemRequest = new OrderLineItemRequest(1L, 5L);
            final var orderCreateRequest = new OrderCreateRequest(2L, List.of(orderLineItemRequest));

            // when & then
            assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 존재하지_않는_메뉴를_사용하면_예외를_반환한다() {
            // given
            final var tableGroup = tableGroupRepository.save(TableGroup.create());
            orderTableRepository.save(new OrderTable(tableGroup, 3, false));

            final var orderLineItemRequest = new OrderLineItemRequest(999L, 5L);
            final var request = new OrderCreateRequest(1L, List.of(orderLineItemRequest));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 메뉴입니다.");
        }

        @Test
        void 메뉴의_수와_실제_주문한_메뉴의_수가_다르면_예외를_반환한다() {
            // given
            final var tableGroup = tableGroupRepository.save(TableGroup.create());
            final var orderTable = orderTableRepository.save(new OrderTable(tableGroup, 3, false));
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            final var menu = menuRepository.save(new Menu("메뉴_이름", BigDecimal.valueOf(0), menuGroup, List.of()));
            final var orderLineItem1 = new OrderLineItem(menu, 5);
            final var orderLineItem2 = new OrderLineItem(menu, 3);
            orderRepository.save(new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), List.of(orderLineItem1, orderLineItem2)));

            final var orderLineItemRequest1 = new OrderLineItemRequest(1L, 5L);
            final var orderLineItemRequest2 = new OrderLineItemRequest(1L, 3L);
            final var request = new OrderCreateRequest(1L, List.of(orderLineItemRequest1, orderLineItemRequest2));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 메뉴의 수와 실제 주문한 메뉴의 수가 다릅니다.");
        }
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 주문_목록이_존재하지_않으면_빈_값을_반환한다() {
            // given & when
            final var actual = orderService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 주문이_하나_이상_존재하면_주문_목록을_반환한다() {
            // given
            final var tableGroup = tableGroupRepository.save(TableGroup.create());
            final var orderTable = orderTableRepository.save(new OrderTable(tableGroup, 5, false));
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            final var menu = menuRepository.save(new Menu("메뉴_이름", BigDecimal.valueOf(0), menuGroup, List.of()));
            final var orderLineItem = new OrderLineItem(menu, 5);
            final var order = orderRepository.save(new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), List.of(orderLineItem)));

            final var expected = List.of(OrderResponse.from(order));

            // when
            final var actual = orderService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class list_실패_테스트 {
    }

    @Nested
    class changeOrderStatus_성공_테스트 {

        @Test
        void 주문_상태_변경을_할_수_있다() {
            // given
            final var tableGroup = tableGroupRepository.save(TableGroup.create());
            final var orderTable = orderTableRepository.save(new OrderTable(tableGroup, 5, false));
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            final var menu = menuRepository.save(new Menu("메뉴_이름", BigDecimal.valueOf(0L), menuGroup, List.of()));
            final var orderLineItem = new OrderLineItem(menu, 5L);
            orderRepository.save(new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), List.of(orderLineItem)));
            orderLineItemRepository.save(orderLineItem);

            final var orderLineItemRequest = new OrderLineItemRequest(1L, 5L);
            final var request = new OrderChangeStatusRequest("COOKING");

            // when
            final var actual = orderService.changeOrderStatus(1L, request);
            final var expected = OrderResponse.from(orderRepository.findById(1L).get());

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class changeOrderStatus_실패_테스트 {

        @Test
        void 존재하지_않는_주문의_상태를_변경하면_에러를_반환한다() {
            // given
            final var request = new OrderChangeStatusRequest("COOKING");

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문입니다.");
        }
    }
}
