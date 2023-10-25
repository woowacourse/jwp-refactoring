package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.OrderCreateDto;
import kitchenpos.application.dto.OrderLineItemDto;
import kitchenpos.application.dto.OrderStatusUpdateDto;
import kitchenpos.application.exception.MenuAppException.NotFoundMenuException;
import kitchenpos.application.exception.OrderAppException.OrderAlreadyCompletedException;
import kitchenpos.application.exception.OrderLineItemAppException.EmptyOrderLineItemException;
import kitchenpos.domain.exception.OrderException.EmptyOrderTableException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderStatusChecker;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderStatusChecker orderStatusChecker;

    private OrderTable mockOrderTable;
    private Menu mockMenu;

    @BeforeEach
    void init() {
        final OrderTable orderTable = new OrderTable(4);
        mockOrderTable = orderTableRepository.save(orderTable);

        final MenuGroup menuGroup = new MenuGroup("테스트 메뉴 그룹");
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Product product = productRepository.save(
            new Product("테스트 상품", BigDecimal.valueOf(10000)));

        final MenuProduct menuProduct = new MenuProduct(product.getName(), product.getPrice(), 2L);
        final Menu menu = Menu.of(
            "테스트 메뉴",
            BigDecimal.valueOf(10000),
            savedMenuGroup,
            List.of(menuProduct)
        );
        mockMenu = menuRepository.save(menu);
    }

    @Test
    void 주문_생성한다() {
        // given
        final OrderLineItemDto orderLineItemDto = new OrderLineItemDto(mockMenu.getId(), 2);

        final OrderCreateDto orderCreateDto = new OrderCreateDto(
            mockOrderTable.getId(), List.of(orderLineItemDto)
        );

        // when
        final Order result = orderService.create(orderCreateDto);

        // then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(result.getOrderLineItems()).hasSize(1);
    }

    @Test
    void 주문_생성_시_주문라인아이템이_없으면_예외가_발생한다() {
        // given
        final OrderCreateDto orderCreateDto = new OrderCreateDto(
            mockOrderTable.getId(), Collections.emptyList()
        );

        // when then
        assertThatThrownBy(() -> orderService.create(orderCreateDto))
            .isInstanceOf(EmptyOrderLineItemException.class);
    }

    @Test
    void 주문_생성_시_주문라인아이템의_메뉴가_존재하지_않으면_예외가_발생한다() {
        // given
        final Long notExistMenuId = 99999L;
        final Optional<Order> emptyOrder = orderRepository.findById(notExistMenuId);
        final OrderLineItemDto orderLineItemDto = new OrderLineItemDto(notExistMenuId, 2);

        // when
        final OrderCreateDto orderCreateDto = new OrderCreateDto(
            mockOrderTable.getId(), List.of(orderLineItemDto)
        );

        // then
        assertThat(emptyOrder).isEmpty();
        assertThatThrownBy(() -> orderService.create(orderCreateDto))
            .isInstanceOf(NotFoundMenuException.class);
    }

    @Test
    void 주문_생성_시_테이블이_비어있으면_예외가_발생한다() {
        // given when
        final OrderTable emptyOrderTable = orderTableRepository.save(new OrderTable(2));
        emptyOrderTable.changeEmpty(orderStatusChecker, true);
        final OrderTable savedOrderTable = orderTableRepository.save(emptyOrderTable);

        final OrderLineItemDto orderLineItemDto = new OrderLineItemDto(mockMenu.getId(), 2);

        final OrderCreateDto orderCreateDto = new OrderCreateDto(
            savedOrderTable.getId(), List.of(orderLineItemDto)
        );

        // then
        assertThatThrownBy(() -> orderService.create(orderCreateDto))
            .isInstanceOf(EmptyOrderTableException.class);
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        final OrderLineItemDto orderLineItemDto = new OrderLineItemDto(mockMenu.getId(), 2);

        final OrderCreateDto orderCreateDto = new OrderCreateDto(
            mockOrderTable.getId(), List.of(orderLineItemDto)
        );

        final Order createdOrder = orderService.create(orderCreateDto);
        final OrderStatusUpdateDto orderStatusUpdateDto = new OrderStatusUpdateDto("MEAL");

        // when
        final Order result = orderService.changeOrderStatus(createdOrder.getId(),
            orderStatusUpdateDto);

        // then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 주문_상태가_완료된_후에는_상태를_변경할_수_없다(final OrderStatus orderStatus) {
        // given
        final OrderLineItemDto orderLineItemDto = new OrderLineItemDto(mockMenu.getId(), 2);

        final OrderCreateDto orderCreateDto = new OrderCreateDto(
            mockOrderTable.getId(), List.of(orderLineItemDto)
        );

        final Order createdOrder = orderService.create(orderCreateDto);
        createdOrder.changeOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(createdOrder);

        // when
        final OrderStatusUpdateDto orderStatusUpdateDto = new OrderStatusUpdateDto(
            orderStatus.name());

        // then
        assertThatThrownBy(
            () -> orderService.changeOrderStatus(createdOrder.getId(), orderStatusUpdateDto))
            .isInstanceOf(OrderAlreadyCompletedException.class);
    }
}
