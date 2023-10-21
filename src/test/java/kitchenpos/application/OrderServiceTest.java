package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuGroupRepository;
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
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderService orderService;

    @Test
    void 주문을_저장할_수_있다() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Menu menu = menuDao.save(new Menu("메뉴", new BigDecimal(1_000), menuGroup.getId(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 5, false));
        final OrderLineItemDto orderLineItemDto = new OrderLineItemDto(menu.getId(), 5);
        final CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderTable.getId(), List.of(orderLineItemDto));

        // when
        final Order actual = orderService.create(createOrderRequest);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo("COOKING"),
                () -> assertThat(actual.getOrderLineItems()).hasSize(1)
        );
    }

    @Nested
    class 주문_생성_실패 {

        @Test
        void 주문_항목이_비어있다면_예외가_발생한다() {
            // given
            final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 1, false));
            final CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderTable.getId(), List.of());

            assertThatThrownBy(() -> orderService.create(createOrderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목의_개수와_메뉴_개수가_일치하지_않으면_에외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
            final Menu menu = menuDao.save(new Menu("메뉴", new BigDecimal(1_000), menuGroup.getId(), null));
            final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 1, false));

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
            final Menu menu = menuDao.save(new Menu("메뉴", new BigDecimal(1_000), menuGroup.getId(), null));

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
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Menu menu = menuDao.save(new Menu("메뉴", new BigDecimal(1_000), menuGroup.getId(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 5, false));
        final OrderLineItem orderLineItem = new OrderLineItem(null, null, menu.getId(), 10);

        final Order expected = orderDao.save(new Order(orderTable.getId(), String.valueOf(OrderStatus.COOKING), LocalDateTime.now(), List.of(orderLineItem)));

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
        final Menu menu = menuDao.save(new Menu("메뉴", new BigDecimal(1_000), menuGroup.getId(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 5, false));
        final OrderLineItem orderLineItem = new OrderLineItem(null, null, menu.getId(), 10);

        final Order expected = orderDao.save(new Order(orderTable.getId(), String.valueOf(OrderStatus.COMPLETION), LocalDateTime.now(), List.of(orderLineItem)));

        final UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest(OrderStatus.COMPLETION.name());

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(expected.getId(), updateOrderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문입니다.");
    }

    @Test
    void 주문_목록을_가져올_수_있다() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Menu menu = menuDao.save(new Menu("메뉴", new BigDecimal(1_000), menuGroup.getId(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 5, false));
        final OrderLineItem orderLineItem = new OrderLineItem(null, null, menu.getId(), 10);

        orderDao.save(new Order(orderTable.getId(), String.valueOf(OrderStatus.COMPLETION), LocalDateTime.now(), List.of(orderLineItem)));

        // when
        final List<Order> expected = orderService.list();

        // then
        assertThat(expected).hasSize(1);
    }

}
