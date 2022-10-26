package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderService orderService;

    private Long validOrderTableId;
    private Long validMenuId;

    @BeforeEach
    void setUp() {
        final OrderTable orderTable = orderTableDao.save(new OrderTable(1, false));
        validOrderTableId = orderTable.getId();
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("추가메뉴"));
        final Menu menu = menuDao.save(new Menu("후라후라후라", new BigDecimal(27000), menuGroup.getId()));
        validMenuId = menu.getId();
    }

    @DisplayName("주문을 저장한다.")
    @Test
    void create() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(validMenuId, 2);
        final Order order = new Order(validOrderTableId, List.of(orderLineItem));

        // when
        final Order savedOrder = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getId()).isNotNull()
        );
    }

    @DisplayName("주문 저장 시에 주문 목록이 없다면 예외를 반환한다.")
    @Test
    void create_throwException_ifOrderLineItemsEmpty() {
        // given
        final Order order = new Order(validOrderTableId, List.of());

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 목록이 없습니다.");
    }

    @DisplayName("없는 메뉴를 주문 시 예외를 반환한다.")
    @Test
    void create_throwException_ifMenuNotExist() {
        // given
        final Long noExistMenuId = 999L;
        final OrderLineItem orderLineItem = new OrderLineItem(noExistMenuId, 2);
        final Order order = new Order(validOrderTableId, List.of(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("없는 메뉴는 주문할 수 없습니다.");
    }

    @DisplayName("없는 테이블에서 주문 시 예외를 반환한다.")
    @Test
    void create_throwException_ifTableNotExist() {
        // given
        final Long noExistTableId = 999L;
        final OrderLineItem orderLineItem = new OrderLineItem(validMenuId, 2);
        final Order order = new Order(noExistTableId, List.of(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("없는 테이블에서는 주문할 수 없습니다.");
    }

    @DisplayName("주문하려는 테이블이 비어있으면 예외를 반환한다.")
    @Test
    void create_throwException_ifTableNotEmpty() {
        // given
        final OrderTable emptyOrderTable = orderTableDao.save(new OrderTable(0, true));
        final OrderLineItem orderLineItem = new OrderLineItem(validMenuId, 2);
        final Order order = new Order(emptyOrderTable.getId(), List.of(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용 중이지 않은 테이블입니다.");
    }

    @DisplayName("전체 주문을 조회한다.")
    @Test
    void findAll() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(validMenuId, 2);
        final Order order = new Order(validOrderTableId, List.of(orderLineItem));
        orderService.create(order);

        // when, then
        assertThat(orderService.findAll()).hasSize(1);
    }
}
