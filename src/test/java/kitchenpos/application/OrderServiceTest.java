package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.menu;
import static kitchenpos.fixture.MenuGroupFixture.menuGroup;
import static kitchenpos.fixture.MenuProductFixture.menuProduct;
import static kitchenpos.fixture.OrderFixture.order;
import static kitchenpos.fixture.OrderLineItemFixture.orderLineItem;
import static kitchenpos.fixture.OrderTableFixture.orderTable;
import static kitchenpos.fixture.ProductFixture.product;
import static kitchenpos.fixture.TableGroupFixture.tableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("주문을 등록한다")
    void create() {
        // given
        final OrderTable 세명_테이블 = orderTableDao.save(orderTable(3, false));
        final OrderTable 네명_테이블 = orderTableDao.save(orderTable(4, false));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupDao.save(tableGroup());
        final OrderTable 그룹화된_세명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 세명_테이블));
        final OrderTable 그룹화된_네명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 네명_테이블));

        final Product 후라이드 = productDao.save(product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupDao.save(menuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = menuProduct(후라이드.getId(), 2l);

        final Menu 메뉴 = menuDao.save(menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개)));

        final OrderLineItem 주문항목 = orderLineItem(메뉴.getId(), 1l);

        final Order order = order(그룹화된_세명_테이블.getId(), List.of(주문항목));

        // when
        final Order actual = orderService.create(order);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(OrderStatus.valueOf(actual.getOrderStatus())).isEqualTo(OrderStatus.COOKING);
            softAssertions.assertThat(actual.getOrderedTime()).isNotNull();
            softAssertions.assertThat(actual.getOrderLineItems()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("주문을 등록할 때 주문항목이 없으면 예외가 발생한다")
    void create_emptyOrderLineItems() {
        // given
        final OrderTable 세명_테이블 = orderTableDao.save(orderTable(3, false));
        final OrderTable 네명_테이블 = orderTableDao.save(orderTable(4, false));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupDao.save(tableGroup());
        final OrderTable 그룹화된_세명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 세명_테이블));
        final OrderTable 그룹화된_네명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 네명_테이블));

        final Product 후라이드 = productDao.save(product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupDao.save(menuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = menuProduct(후라이드.getId(), 2l);

        menuDao.save(menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개)));

        final Order order = order(그룹화된_세명_테이블.getId(), Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록할 때 주문항목의 개수와 메뉴 개수가 다르면 예외가 발생한다.")
    void create_invalidNumberOfOrderLineItems() {
        // given
        final OrderTable 세명_테이블 = orderTableDao.save(orderTable(3, false));
        final OrderTable 네명_테이블 = orderTableDao.save(orderTable(4, false));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupDao.save(tableGroup());
        final OrderTable 그룹화된_세명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 세명_테이블));
        final OrderTable 그룹화된_네명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 네명_테이블));

        final Product 후라이드 = productDao.save(product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupDao.save(menuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = menuProduct(후라이드.getId(), 2l);

        final Menu 메뉴 = menuDao.save(menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개)));

        final OrderLineItem 주문항목1 = orderLineItem(메뉴.getId(), 1l);
        final OrderLineItem 주문항목2 = orderLineItem(메뉴.getId(), 2l);

        final Order order = order(그룹화된_세명_테이블.getId(), List.of(주문항목1, 주문항목2));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록할 때 주문테이블을 찾을 수 없으면 예외가 발생한다")
    void create_invalidOrderTable() {
        // given
        final OrderTable 세명_테이블 = orderTableDao.save(orderTable(3, false));
        final OrderTable 네명_테이블 = orderTableDao.save(orderTable(4, false));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupDao.save(tableGroup());
        final OrderTable 그룹화된_세명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 세명_테이블));
        final OrderTable 그룹화된_네명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 네명_테이블));

        final Product 후라이드 = productDao.save(product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupDao.save(menuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = menuProduct(후라이드.getId(), 2l);

        final Menu 메뉴 = menuDao.save(menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개)));

        final OrderLineItem 주문항목 = orderLineItem(메뉴.getId(), 1l);

        final long invalidOrderTableId = -999L;
        final Order order = order(invalidOrderTableId, List.of(주문항목));


        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 테이블이 비어있으면 예외가 발생한다")
    void create_emptyTable() {
        // given
        final OrderTable 세명_테이블 = orderTableDao.save(orderTable(3, true));
        final OrderTable 네명_테이블 = orderTableDao.save(orderTable(4, true));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupDao.save(tableGroup());
        final OrderTable 그룹화된_세명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 세명_테이블));
        final OrderTable 그룹화된_네명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 네명_테이블));

        final Product 후라이드 = productDao.save(product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupDao.save(menuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = menuProduct(후라이드.getId(), 2l);

        final Menu 메뉴 = menuDao.save(menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개)));

        final OrderLineItem 주문항목 = orderLineItem(메뉴.getId(), 1l);

        final Order order = order(그룹화된_세명_테이블.getId(), List.of(주문항목));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다")
    void list() {
        // given
        final OrderTable 세명_테이블 = orderTableDao.save(orderTable(3, true));
        final OrderTable 네명_테이블 = orderTableDao.save(orderTable(4, true));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupDao.save(tableGroup());
        final OrderTable 그룹화된_세명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 세명_테이블));
        final OrderTable 그룹화된_네명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 네명_테이블));

        final Product 후라이드 = productDao.save(product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupDao.save(menuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = menuProduct(후라이드.getId(), 2l);

        final Menu 메뉴 = menuDao.save(menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개)));

        final OrderLineItem 주문항목 = orderLineItem(메뉴.getId(), 1l);

        orderDao.save(order(그룹화된_세명_테이블.getId(), OrderStatus.COOKING, List.of(주문항목)));
        orderDao.save(order(그룹화된_네명_테이블.getId(), OrderStatus.COOKING, List.of(주문항목)));

        // when
        final List<Order> actual = orderService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).getOrderTableId()).isEqualTo(그룹화된_세명_테이블.getId());
            softAssertions.assertThat(actual.get(1).getOrderTableId()).isEqualTo(그룹화된_네명_테이블.getId());
        });
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {

        // given
        final OrderTable 세명_테이블 = orderTableDao.save(orderTable(3, true));
        final OrderTable 네명_테이블 = orderTableDao.save(orderTable(4, true));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupDao.save(tableGroup());
        final OrderTable 그룹화된_세명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 세명_테이블));
        final OrderTable 그룹화된_네명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 네명_테이블));

        final Product 후라이드 = productDao.save(product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupDao.save(menuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = menuProduct(후라이드.getId(), 2l);

        final Menu 메뉴 = menuDao.save(menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개)));

        final OrderLineItem 주문항목 = orderLineItem(메뉴.getId(), 1l);

        final Order 주문 = orderDao.save(order(그룹화된_세명_테이블.getId(), OrderStatus.COOKING, List.of(주문항목)));

        final OrderStatus expect = OrderStatus.MEAL;
        final Order 주문상태_바꾼_주문 = order(그룹화된_세명_테이블.getId(), expect);

        // when
        final Order actual = orderService.changeOrderStatus(주문.getId(), 주문상태_바꾼_주문);

        // then
        assertThat(OrderStatus.valueOf(actual.getOrderStatus())).isEqualTo(expect);
    }

    @Test
    @DisplayName("주문 상태를 변경할 때 주문 상태가 이미 COMPLETION이면 예외가 발생한다")
    void changeOrderStatus_orderStatusCompletion() {

        // given
        final OrderTable 세명_테이블 = orderTableDao.save(orderTable(3, true));
        final OrderTable 네명_테이블 = orderTableDao.save(orderTable(4, true));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupDao.save(tableGroup());
        final OrderTable 그룹화된_세명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 세명_테이블));
        final OrderTable 그룹화된_네명_테이블 = orderTableDao.save(orderTable(그룹화된_세명_네명_테이블.getId(), 네명_테이블));

        final Product 후라이드 = productDao.save(product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupDao.save(menuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = menuProduct(후라이드.getId(), 2l);

        final Menu 메뉴 = menuDao.save(menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개)));

        final OrderLineItem 주문항목 = orderLineItem(메뉴.getId(), 1l);

        final Order 주문 = orderDao.save(order(그룹화된_세명_테이블.getId(), OrderStatus.COMPLETION, List.of(주문항목)));

        final OrderStatus newOrderStatus = OrderStatus.MEAL;
        final Order 주문상태_바꾼_주문 = order(그룹화된_세명_테이블.getId(), newOrderStatus);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문상태_바꾼_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
