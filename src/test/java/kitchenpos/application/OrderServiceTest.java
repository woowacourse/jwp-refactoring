package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.order.CreateOrderRequest;
import kitchenpos.ui.dto.order.OrderLineItemDto;
import kitchenpos.ui.dto.order.UpdateOrderRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Test
    void 주문을_저장할_수_있다() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Menu menu = menuRepository.save(new Menu("메뉴", new BigDecimal(1_000), menuGroup.getId()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));
        final OrderLineItemDto orderLineItemDto = new OrderLineItemDto(menu.getId(), 5);
        final CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderTable.getId(), List.of(orderLineItemDto));

        // when
        final Order actual = orderService.create(createOrderRequest);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo("COOKING")
        );
    }

    @Nested
    class 주문_생성_실패 {

        @Test
        void 주문_항목이_비어있다면_예외가_발생한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 1, true));
            final CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderTable.getId(), List.of());

            assertThatThrownBy(() -> orderService.create(createOrderRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 생성시 주문 목은 비어있을 수 없습니다.");
        }

        @Test
        void 주문_항목의_개수와_메뉴_개수가_일치하지_않으면_에외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
            final Menu menu = menuRepository.save(new Menu("메뉴", new BigDecimal(1_000), menuGroup.getId()));
            final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 1, false));

            final OrderLineItemDto firstOrderLineItemDto = new OrderLineItemDto(menu.getId(), 2);
            final OrderLineItemDto secondOrderLineItemDto = new OrderLineItemDto(menu.getId(), 2);

            final CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderTable.getId(), List.of(firstOrderLineItemDto, secondOrderLineItemDto));

            // expected
            assertThatThrownBy(() -> orderService.create(createOrderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있을_경우_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
            final Menu menu = menuRepository.save(new Menu("메뉴", new BigDecimal(1_000), menuGroup.getId()));

            final OrderLineItemDto firstOrderLineItemDto = new OrderLineItemDto(menu.getId(), 2);
            final OrderLineItemDto secondOrderLineItemDto = new OrderLineItemDto(menu.getId(), 2);

            final CreateOrderRequest createOrderRequest = new CreateOrderRequest(null, List.of(firstOrderLineItemDto, secondOrderLineItemDto));

            // expected
            assertThatThrownBy(() -> orderService.create(createOrderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));
        final Order expected = orderRepository.save(Order.createBy(orderTable));
        final UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest(OrderStatus.COMPLETION.name());

        // when
        final Order actual = orderService.changeOrderStatus(expected.getId(), updateOrderRequest);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo("COMPLETION");
    }

    @Test
    void 주문_상태가_완료라면_상태_변경시_예외가_발생한다() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));

        final Order expected = orderRepository.save(Order.createBy(orderTable));

        final UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest(OrderStatus.COMPLETION.name());

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(expected.getId(), updateOrderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문입니다.");
    }

    @Test
    void 주문_목록을_가져올_수_있다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));
        orderRepository.save(Order.createBy(orderTable));

        // when
        final List<Order> expected = orderService.list();

        // then
        assertThat(expected).hasSize(1);
    }

}
