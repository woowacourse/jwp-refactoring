package kitchenpos.application;

import static kitchenpos.DomainFixture.뿌링클_생성;
import static kitchenpos.DomainFixture.뿌링클_치즈볼_메뉴_생성;
import static kitchenpos.DomainFixture.세트_메뉴;
import static kitchenpos.DomainFixture.주문_생성;
import static kitchenpos.DomainFixture.치즈볼_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    private Menu menu;
    private OrderTable table;

    @BeforeEach
    void setUp() {
        final var productA = 상품_저장(뿌링클_생성());
        final var productB = 상품_저장(치즈볼_생성());
        final var menuGroup = 메뉴_그룹_저장(세트_메뉴);
        menu = 메뉴_세팅_및_저장(뿌링클_치즈볼_메뉴_생성(), menuGroup.getId(), productA.getId(), productB.getId());
        table = 테이블_생성_및_저장();
    }

    @DisplayName("주문 생성 테스트")
    @Nested
    class CreateTest {

        @Test
        void 주문을_생성하고_결과를_반환한다() {
            // given
            final var order = 주문_생성(table.getId(), menu.getId());

            // when
            final var createdOrder = orderService.create(order);

            // then
            assertAll(
                    () -> assertThat(createdOrder.getId()).isNotNull(),
                    () -> assertThat(createdOrder.getOrderTableId()).isEqualTo(table.getId()),
                    () -> assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                    () -> assertThat(createdOrder.getOrderedTime()).isBefore(LocalDateTime.now()),
                    () -> assertThat(createdOrder.getOrderLineItems()).hasSize(1)
            );
        }

        @Test
        void 주문_항목이_비어있으면_예외를_던진다() {
            // given
            final var order = 주문_생성(table.getId());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 없는_메뉴인_경우_예외를_던진다() {
            // given
            final var order = 주문_생성(table.getId(), 100L);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 없는_테이블인_경우_예외를_던진다() {
            // given
            final var order = 주문_생성(100L, menu.getId());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블인_경우_예외를_던진다() {
            // given
            final var emptyTable = 빈_테이블_생성_및_저장();
            final var order = 주문_생성(emptyTable.getId(), menu.getId());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 주문_목록을_조회한다() {
        // given
        주문_생성_및_저장(table.getId(), menu.getId());
        주문_생성_및_저장(table.getId(), menu.getId());

        // when
        final var foundOrders = orderService.list();

        // then
        assertThat(foundOrders).hasSizeGreaterThanOrEqualTo(2);
    }

    @DisplayName("주문 상태 변경 테스트")
    @Nested
    class ChangeStatusTest {

        @Test
        void 주문_상태를_식사로_변경한다() {
            // given
            final var orderId = 주문_생성_및_저장(table.getId(), menu.getId()).getId();
            final var order = new Order();
            order.setOrderStatus(OrderStatus.MEAL.name());

            // when
            final var changedOrder = orderService.changeOrderStatus(orderId, order);

            // then
            assertAll(
                    () -> assertThat(changedOrder.getId()).isEqualTo(orderId),
                    () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus())
            );
        }

        @Test
        void 주문_상태를_계산완료로_변경한다() {
            // given
            final var orderId = 주문_생성_및_저장(table.getId(), menu.getId()).getId();
            final var order = new Order();
            order.setOrderStatus(OrderStatus.COMPLETION.name());

            // when
            final var changedOrder = orderService.changeOrderStatus(orderId, order);

            // then
            assertAll(
                    () -> assertThat(changedOrder.getId()).isEqualTo(orderId),
                    () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus())
            );
        }

        @Test
        void 없는_주문일_경우_예외를_던진다() {
            // given
            final var order = new Order();
            order.setOrderStatus(OrderStatus.MEAL.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(100L, order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태가_이미_계산완료인_경우_예외를_던진다() {
            // given
            final var order = 주문_생성(table.getId(), menu.getId());
            order.setOrderStatus(OrderStatus.COMPLETION.name());
            final var orderId = 주문_저장(order).getId();

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
