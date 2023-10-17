package kitchenpos.application;

import static kitchenpos.test.fixture.MenuFixture.메뉴;
import static kitchenpos.test.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.test.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.test.fixture.OrderFixture.주문;
import static kitchenpos.test.fixture.OrderLineItemFixture.주문_메뉴_목록;
import static kitchenpos.test.fixture.ProductFixture.상품;
import static kitchenpos.test.fixture.TableFixture.테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Nested
    class 주문_추가_시 {

        private Menu menu;

        @BeforeEach
        void setUp() {
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuProduct menuProduct = 메뉴_상품(null, product.getId(), 1);
            MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹("일식"));
            menu = menuDao.save(메뉴("텐동", BigDecimal.valueOf(11000), menuGroup.getId(), List.of(menuProduct)));
            menuProduct.setMenuId(menu.getId());
            menuProductDao.save(menuProduct);
        }

        @Test
        void 정상적인_주문이라면_주문을_추가한다() {
            //given
            OrderTable orderTable = orderTableDao.save(테이블(null, 10, false));
            OrderLineItem orderLineItem = 주문_메뉴_목록(orderTable.getId(), menu.getId(), 1);
            LocalDateTime orderedTime = LocalDateTime.now();
            Order order = 주문(orderTable.getId(), null, orderedTime, List.of(orderLineItem));

            //when
            Order savedOrder = orderService.create(order);

            //then
            assertSoftly(softly -> {
                softly.assertThat(savedOrder.getId()).isNotNull();
                softly.assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTable.getId());
                softly.assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                softly.assertThat(savedOrder.getOrderedTime()).isAfter(orderedTime);
                softly.assertThat(savedOrder.getOrderLineItems()).usingRecursiveComparison()
                        .ignoringFields("seq")
                        .isEqualTo(List.of(orderLineItem));
            });
        }

        @Test
        void 주문_메뉴_목록이_비어있으면_예외를_던진다() {
            //given
            OrderTable orderTable = orderTableDao.save(테이블(null, 10, false));
            Order order = 주문(orderTable.getId(), null, LocalDateTime.now(), Collections.emptyList());

            //when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_메뉴_목록에_메뉴가_존재하지_않으면_예외를_던진다() {
            //given
            OrderTable orderTable = orderTableDao.save(테이블(null, 10, false));
            OrderLineItem orderLineItem = 주문_메뉴_목록(orderTable.getId(), null, 1);
            LocalDateTime orderedTime = LocalDateTime.now();
            Order order = 주문(orderTable.getId(), null, orderedTime, List.of(orderLineItem));

            //when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이라면_예외를_던진다() {
            //given
            OrderTable orderTable = orderTableDao.save(테이블(null, 10, false));
            OrderLineItem orderLineItem = 주문_메뉴_목록(orderTable.getId(), menu.getId(), 1);
            LocalDateTime orderedTime = LocalDateTime.now();
            Order order = 주문(-1L, null, orderedTime, List.of(orderLineItem));

            //when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_비어있으면_예외를_던진다() {
            //given
            OrderTable orderTable = orderTableDao.save(테이블(null, 10, true));
            OrderLineItem orderLineItem = 주문_메뉴_목록(orderTable.getId(), menu.getId(), 1);
            LocalDateTime orderedTime = LocalDateTime.now();
            Order order = 주문(orderTable.getId(), null, orderedTime, List.of(orderLineItem));

            //when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_목록_조회_시 {

        @Test
        void 모든_주문_목록을_조회한다() {
            //given
            Product product = 상품("텐동", BigDecimal.valueOf(11000));
            MenuProduct menuProduct = 메뉴_상품(null, product.getId(), 1);
            MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹("일식"));
            Menu menu = menuDao.save(메뉴("텐동", BigDecimal.valueOf(11000), menuGroup.getId(), List.of(menuProduct)));

            OrderTable orderTable = orderTableDao.save(테이블(null, 10, false));
            OrderLineItem orderLineItem = 주문_메뉴_목록(orderTable.getId(), menu.getId(), 1);
            LocalDateTime cookingTime = LocalDateTime.now();
            Order orderA = 주문(orderTable.getId(), null, cookingTime, List.of(orderLineItem));
            Order orderB = 주문(orderTable.getId(), null, cookingTime, List.of(orderLineItem));
            Order savedOrderA = orderService.create(orderA);
            Order savedOrderB = orderService.create(orderB);

            //when
            List<Order> orders = orderService.list();

            //then
            assertThat(orders).usingRecursiveComparison().isEqualTo(List.of(savedOrderA, savedOrderB));
        }

        @Test
        void 주문이_존재하지_않으면_목록이_비어있다() {
            //given, when
            List<Order> orders = orderService.list();

            //then
            assertThat(orders).isEmpty();
        }
    }

    @Nested
    class 주문_상태_수정_시 {

        private Order order;

        @BeforeEach
        void setUp() {
            Product product = 상품("텐동", BigDecimal.valueOf(11000));
            MenuProduct menuProduct = 메뉴_상품(null, product.getId(), 1);
            MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹("일식"));
            Menu menu = menuDao.save(메뉴("텐동", BigDecimal.valueOf(11000), menuGroup.getId(), List.of(menuProduct)));

            OrderTable orderTable = orderTableDao.save(테이블(null, 10, false));
            OrderLineItem orderLineItem = 주문_메뉴_목록(orderTable.getId(), menu.getId(), 1);
            LocalDateTime cookingTime = LocalDateTime.now();
            order = orderService.create(주문(orderTable.getId(), null, cookingTime, List.of(orderLineItem)));
        }

        @Test
        void 정상적인_주문이라면_주문_상태를_수정한다() {
            //given
            Order updateOrder = 주문(null, OrderStatus.MEAL.name(), null, Collections.emptyList());

            //when
            Order updatedOrder = orderService.changeOrderStatus(order.getId(), updateOrder);

            //then
            assertSoftly(softly -> {
                softly.assertThat(updatedOrder).usingRecursiveComparison()
                        .ignoringFields("orderStatus")
                        .isEqualTo(order);
                softly.assertThat(updatedOrder.getOrderStatus()).isEqualTo(updateOrder.getOrderStatus());
            });
        }

        @Test
        void 존재하지_않는_주문이라면_예외를_던진다() {
            //given
            Order updateOrder = 주문(null, OrderStatus.MEAL.name(), null, Collections.emptyList());

            //when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, updateOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 완료된_주문이라면_예외를_던진다() {
            //given
            Order completeUpdate = 주문(null, OrderStatus.COMPLETION.name(), null, Collections.emptyList());
            orderService.changeOrderStatus(order.getId(), completeUpdate);
            Order updateOrder = 주문(null, OrderStatus.MEAL.name(), null, Collections.emptyList());

            //when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), updateOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
