package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;

    private List<OrderLineItem> orderLineItems = new ArrayList<>();
    private OrderTable savedOrderTable;

    @BeforeEach
    void setUp() {
        final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup(null, "메뉴 그룹"));
        final Product savedProduct = productDao.save(new Product(null, "상품", BigDecimal.ONE));
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(null, null, savedProduct.getId(), 2));
        final Menu savedMenu = menuDao.save(new Menu(null, "메뉴", BigDecimal.ONE, savedMenuGroup.getId(), menuProducts));
        orderLineItems.add(new OrderLineItem(null, null, savedMenu.getId(), 1));
        savedOrderTable = orderTableDao.save(new OrderTable(null, null, 5, false));
    }

    @Nested
    class 주문을_등록한다 {
        @Test
        void 주문이_정상적으로_등록된다() {
            final Order order = new Order(null, savedOrderTable.getId(), null, null, orderLineItems);
            final Order savedOrder = orderService.create(order);

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(savedOrder.getId()).isNotNull();
                softly.assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                softly.assertThat(savedOrder.getOrderedTime()).isNotNull();
            });
        }

        @Test
        void 존재하지_않는_테이블에_주문이_등록되면_예외가_발생한다() {
            final Order order = new Order(null, savedOrderTable.getId() + 1, null, null, orderLineItems);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 비어있는_상태의_테이블에_주문이_등록되면_예외가_발생한다() {
            savedOrderTable.setEmpty(true);
            orderTableDao.save(savedOrderTable);

            final Order order = new Order(null, savedOrderTable.getId(), null, null, orderLineItems);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목들이_비어있으면_예외가_발생한다() {
            final Order order = new Order(null, savedOrderTable.getId(), null, null, List.of());

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목들에_메뉴가_중복되면_예외가_발생한다() {
            final OrderLineItem orderLineItem = orderLineItems.get(0);
            orderLineItems.add(orderLineItem);
            final Order order = new Order(null, savedOrderTable.getId(), null, null, orderLineItems);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목이_존재하지_않으면_예외가_발생한다() {
            orderLineItems.get(0).setMenuId(orderLineItems.get(0).getMenuId() + 1);

            final Order order = new Order(null, savedOrderTable.getId(), null, null, orderLineItems);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 주문의_목록을_조회한다() {
        final List<Order> expected = orderService.list();
        for (int i = 0; i < 3; i++) {
            final Order order = new Order(null, savedOrderTable.getId(), null, null, orderLineItems);
            expected.add(orderService.create(order));
        }

        final List<Order> result = orderService.list();

        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(expected.size());
            softly.assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        });
    }

    @Nested
    class 주문의_상태를_변경한다 {
        @ParameterizedTest
        @ValueSource(strings = {"MEAL", "COMPLETION"})
        void 주문의_상태를_정상적으로_변경한다(final String status) {
            final Order order = new Order(null, savedOrderTable.getId(), null, null, orderLineItems);
            final Order savedOrder = orderService.create(order);
            final Order changeStatusOrder = new Order(null, null, status, null, null);

            orderService.changeOrderStatus(savedOrder.getId(), changeStatusOrder);
            final Order changeOrder = orderDao.findById(savedOrder.getId()).get();

            assertThat(changeOrder.getOrderStatus()).isEqualTo(status);
        }

        @Test
        void 주문의_상태가_계산_완료일때_예외가_발생한다() {
            final Order savedOrder = orderDao.save(new Order(null, savedOrderTable.getId(), "COMPLETION", LocalDateTime.now(), orderLineItems));
            final Order changeStatusOrder = new Order(null, null, "MEAL", null, null);

            Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), changeStatusOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문이_존재하지_않으면_예외가_발생한다() {
            final Order order = new Order(null, savedOrderTable.getId(), null, null, orderLineItems);
            final Order savedOrder = orderService.create(order);
            final Order changeStatusOrder = new Order(null, null, "MEAL", null, null);

            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId() + 1, changeStatusOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
