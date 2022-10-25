package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
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
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class OrderServiceTest {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final ProductDao productDao;
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final OrderService orderService;

    OrderServiceTest(OrderDao orderDao, OrderTableDao orderTableDao, ProductDao productDao,
                     MenuDao menuDao, MenuGroupDao menuGroupDao, OrderService orderService) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.productDao = productDao;
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.orderService = orderService;
    }

    @Test
    void 주문을_생성한다() {
        OrderTable savedTable = orderTableDao.save(new OrderTable(null, 0, false));
        Order order = new Order(savedTable.getId(), List.of(new OrderLineItem(1L, 메뉴를_저장한다().getId(), 1)));

        Order actual = orderService.create(order);
        assertThat(actual).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 주문을_생성할때_주문정보가_없는_경우_예외를_발생시킨다() {
        OrderTable savedTable = orderTableDao.save(new OrderTable(null, 0, false));
        Order order = new Order(savedTable.getId(), null);

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할때_주문정보와_저장된_메뉴와_다를_경우_예외를_발생시킨다() {
        OrderTable savedTable = orderTableDao.save(new OrderTable(null, 0, false));
        Order order = new Order(savedTable.getId(), List.of(new OrderLineItem(1L, -1L, 1)));

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할때_테이블이_존재하지_않는_경우_예외를_발생시킨다() {
        Order order = new Order(-1L, List.of(new OrderLineItem(1L, 메뉴를_저장한다().getId(), 1)));

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_빈_경우_예외를_발생시킨다() {
        OrderTable savedTable = orderTableDao.save(new OrderTable(null, 0, true));
        Order order = new Order(savedTable.getId(), List.of(new OrderLineItem(1L, 메뉴를_저장한다().getId(), 1)));

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_조회한다() {
        orderDao.save(new Order());

        List<Order> orders = orderService.list();

        assertThat(orders).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void 주문의_상태를_조리에서_식사로_바꾼다() {
        OrderTable savedTable = orderTableDao.save(new OrderTable(null, 0, false));
        Order order = new Order(savedTable.getId(), List.of(new OrderLineItem(1L, 메뉴를_저장한다().getId(), 1)));
        Order savedOrder = orderService.create(order);

        savedOrder.setOrderStatus("MEAL");
        Order result = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 주문의_상태를_바꿀때_주문이_존재하지_않는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문의_상태를_바꿀때_주문의_상태가_계산완료인_경우_예외를_발생시킨다() {
        OrderTable savedTable = orderTableDao.save(new OrderTable(null, 0, false));
        Order order = new Order(savedTable.getId(), List.of(new OrderLineItem(1L, 메뉴를_저장한다().getId(), 1)));
        Order savedOrder = orderService.create(order);

        savedOrder.setOrderStatus("COMPLETION");
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    Menu 메뉴를_저장한다() {
        Product jwt_후라이드 = productDao.save(new Product("JWT 후라이드", new BigDecimal(100_000)));
        Product jwt_양념 = productDao.save(new Product("JWT 양념", new BigDecimal(100_000)));
        MenuProduct 후라이드 = new MenuProduct(1L, jwt_후라이드.getId(), 1);
        MenuProduct 양념 = new MenuProduct(2L, jwt_양념.getId(), 1);
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("추천메뉴"));
        Menu menu = new Menu("반반치킨", new BigDecimal(200_000), menuGroup.getId(), List.of(후라이드, 양념));

        return menuDao.save(menu);
    }
}
