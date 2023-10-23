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
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderChangeStatusRequest;
import kitchenpos.dto.OrderCreateRequest;
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
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 3, false));
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            menuDao.save(new Menu("메뉴_이름", BigDecimal.valueOf(100L), 1L, List.of()));
            orderDao.save(new Order(1L, "", LocalDateTime.now(), List.of()));
            final var orderLineItem = orderLineItemDao.save(new OrderLineItem(1L, 1L, 5));
            final var request = new OrderCreateRequest(1L, "COOKING", LocalDateTime.now(), List.of(orderLineItem));

            // when
            final var actual = orderService.create(request);

            // then
            assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
        }
    }

    @Nested
    class create_실패_테스트 {

        @Test
        void 주문_항목이_비어있으면_에러를_반환한다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 5, false));
            final var request = new OrderCreateRequest(1L, "COOKING", LocalDateTime.now(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문 항목이 비어있습니다.");
        }

        @Test
        void 메뉴의_수와_실제_주문한_메뉴의_수가_다르면_예외를_반환한다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 3, false));
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            menuDao.save(new Menu("메뉴_이름", BigDecimal.valueOf(100L), 1L, List.of()));
            orderDao.save(new Order(1L, "", LocalDateTime.now(), List.of()));
            final var orderLineItem1 = orderLineItemDao.save(new OrderLineItem(1L, 1L, 5));
            final var orderLineItem2 = orderLineItemDao.save(new OrderLineItem(1L, 1L, 3));
            final var request = new OrderCreateRequest(1L, "COOKING", LocalDateTime.now(),
                    List.of(orderLineItem1, orderLineItem2));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 메뉴의 수와 실제 주문한 메뉴의 수가 다릅니다.");
        }

        @Test
        void 존재하지_않는_주문_테이블을_사용하면_예외를_반환한다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 3, false));
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            menuDao.save(new Menu("메뉴_이름", BigDecimal.valueOf(100L), 1L, List.of()));
            orderDao.save(new Order(1L, "", LocalDateTime.now(), List.of()));
            final var orderLineItem = new OrderLineItem(1L, 1L, 5);
            final var request = new OrderCreateRequest(2L, "COOKING", LocalDateTime.now(), List.of(orderLineItem));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 주문_테이블이_비어있으면_예외를_반환한다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 5, true));
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            menuDao.save(new Menu("메뉴_이름", BigDecimal.valueOf(100L), 1L, List.of()));
            orderDao.save(new Order(1L, "", LocalDateTime.now(), List.of()));
            final var orderLineItem = orderLineItemDao.save(new OrderLineItem(1L, 1L, 5));
            final var request = new OrderCreateRequest(1L, "COOKING", LocalDateTime.now(), List.of(orderLineItem));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문 테이블이 비어있습니다.");
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
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 5, true));
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            menuDao.save(new Menu("메뉴_이름", BigDecimal.valueOf(100L), 1L, List.of()));
            final var order = orderDao.save(new Order(1L, "", LocalDateTime.now(), List.of()));
            final var orderLineItem = orderLineItemDao.save(new OrderLineItem(1L, 1L, 5));
            order.applyOrderLineItem(List.of(orderLineItem));
            orderDao.save(order);

            final var expected = List.of(OrderResponse.toResponse(order));

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
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 5, true));
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            menuDao.save(new Menu("메뉴_이름", BigDecimal.valueOf(100L), 1L, List.of()));
            orderDao.save(new Order(1L, "COOKING", LocalDateTime.now(), List.of()));
            final var request = new OrderChangeStatusRequest(1L, "MEAL", LocalDateTime.now(), List.of());

            // when
            final var actual = orderService.changeOrderStatus(1L, request);
            final var expected = OrderResponse.toResponse(orderDao.findById(2L).get());

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
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 5, true));
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            menuDao.save(new Menu("메뉴_이름", BigDecimal.valueOf(100L), 1L, List.of()));
            orderDao.save(new Order(1L, "COOKING", LocalDateTime.now(), List.of()));
            final var request = new OrderChangeStatusRequest(1L, "COOKING", LocalDateTime.now(),
                    Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(2L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문입니다.");
        }

        @Test
        void 주문이_완료됐는데_주문_상태를_변경하면_에러를_반환한다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 5, true));
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            menuDao.save(new Menu("메뉴_이름", BigDecimal.valueOf(100L), 1L, List.of()));
            orderDao.save(new Order(1L, "COMPLETION", LocalDateTime.now(), List.of()));
            final var request = new OrderChangeStatusRequest(1L, "MEAL", LocalDateTime.now(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 완료된 주문은 상태 변경이 불가능합니다.");
        }
    }
}
