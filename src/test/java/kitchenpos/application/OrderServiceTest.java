package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static kitchenpos.domain.OrderStatus.*;
import static kitchenpos.fixture.MenuFixture.MENU;
import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP;
import static kitchenpos.fixture.MenuProductFixture.MENU_PRODUCT;
import static kitchenpos.fixture.OrderFixture.ORDER;
import static kitchenpos.fixture.OrderLineItemFixture.ORDER_LINE_ITEM;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static kitchenpos.fixture.ProductFixture.PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private OrderTableDao orderTableDao;
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private OrderLineItemDao orderLineItemDao;
    
    @Autowired
    private MenuGroupDao menuGroupDao;
    
    @Autowired
    private MenuDao menuDao;
    
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private OrderService orderService;
    
    private MenuGroup chineseMenuGroup;
    
    private Menu chineseNoodleMenu;
    
    private MenuProduct chineseNoodleMenuProduct;
    
    private Product chineseNoodle;
    
    @BeforeEach
    void setup() {
        final MenuGroup menuGroup = MENU_GROUP("중국식 메뉴 그룹");
        chineseMenuGroup = menuGroupDao.save(menuGroup);
        
        final Product product = PRODUCT("짜장면", 8000L);
        chineseNoodle = productDao.save(product);
        
        chineseNoodleMenuProduct = MENU_PRODUCT(chineseNoodle.getId());
        final Menu menu = MENU(chineseNoodle.getName(), 8000L, chineseMenuGroup.getId(), List.of(chineseNoodleMenuProduct));
        chineseNoodleMenu = menuDao.save(menu);
    }
    
    @Test
    void 주문을_생성할_때_주문_항목이_없으면_예외가_발생한다() {
        // given
        OrderTable orderTable = ORDER_TABLE(false, 3);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        
        // when & then
        Order order = ORDER(savedOrderTable.getId(), Collections.EMPTY_LIST);
        
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 없으면 주문할 수 없습니다");
    }
    
    @Test
    void 주문을_생성할_때_주문_항목에_존재하지_않는_메뉴가_있으면_예외를_발생한다() {
        // given
        OrderTable orderTable = ORDER_TABLE(false, 3);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        Menu notExistMenu = new Menu();
        OrderLineItem orderLineItem = ORDER_LINE_ITEM(chineseNoodleMenu.getId(), 3L);
        OrderLineItem orderLineItemWithNotExistMenu = ORDER_LINE_ITEM(notExistMenu.getId(), 1L);
        
        // when & then
        Order order = ORDER(savedOrderTable.getId(), List.of(orderLineItem, orderLineItemWithNotExistMenu));
        
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 메뉴는 주문할 수 없습니다");
    }
    
    @Test
    void 주문을_생성할_때_주문이_속한_주문_테이블이_비어_있으면_예외가_발생한다() {
        // given
        OrderTable emptyOrderTable = ORDER_TABLE(true, 3);
        OrderTable savedOrderTable = orderTableDao.save(emptyOrderTable);
        OrderLineItem orderLineItem = ORDER_LINE_ITEM(chineseNoodleMenu.getId(), 3L);
        
        // when & then
        Order order = ORDER(savedOrderTable.getId(), List.of(orderLineItem));
        
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 주문테이블에서는 주문할 수 없습니다");
        
    }
    
    @Test
    void 주문을_생성할_때_모든_조건을_만족하면_정상적으로_주문을_생성한다() {
        // given
        OrderTable orderTable = ORDER_TABLE(false, 3);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        OrderLineItem orderLineItem = ORDER_LINE_ITEM(chineseNoodleMenu.getId(), 3L);
        
        // when
        Order order = ORDER(savedOrderTable.getId(), List.of(orderLineItem));
        Order actual = orderService.create(order);
        
        // then : 주문에 속한 주문 항목들은 모두 주문id를 가지고 있다
        assertSoftly(softly -> {
            softly.assertThat(actual)
                  .usingRecursiveComparison()
                  .ignoringFields("id", "orderLineItems")
                  .isEqualTo(order);
            
            softly.assertThat(orderLineItemDao.findAllByOrderId(actual.getId()))
                  .usingRecursiveComparison()
                  .isEqualTo(actual.getOrderLineItems());
        });
    }
    
    @Test
    void 모든_주문을_조회한다() {
        // given
        OrderTable orderTable = orderTableDao.save(ORDER_TABLE(false, 1));
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        OrderLineItem orderLineItem = ORDER_LINE_ITEM(chineseNoodleMenu.getId(), 3L);
        
        Order order1 = ORDER(savedOrderTable.getId(), List.of(orderLineItem), COOKING);
        Order savedOrder1 = orderDao.save(order1);
        Order order2 = ORDER(savedOrderTable.getId(), List.of(orderLineItem), COOKING);
        Order savedOrder2 = orderDao.save(order2);
        
        // when
        List<Order> expected = List.of(savedOrder1, savedOrder2);
        List<Order> actual = orderService.list();
        
        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }
    
    @Test
    void 주문의_상태를_변경할_때_이미_주문이_종료된_상태이면_주문의_상태를_변경한다() {
        // given
        OrderTable orderTable = orderTableDao.save(ORDER_TABLE(false, 1));
        OrderLineItem orderLineItem = ORDER_LINE_ITEM(chineseNoodleMenu.getId(), 1L);
        Order order = orderDao.save(ORDER(orderTable.getId(), List.of(orderLineItem), COMPLETION));
        
        // when
        Order orderWithNewStatus = ORDER(orderTable.getId(), List.of(orderLineItem), MEAL);
        
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderWithNewStatus))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 종료된 주문입니다");
    }
    
    @Test
    void 주문의_상태를_변경할_때_모든_조건을_만족하면_주문의_상태를_변경한다() {
        // given
        OrderTable orderTable = orderTableDao.save(ORDER_TABLE(false, 1));
        OrderLineItem orderLineItem = ORDER_LINE_ITEM(chineseNoodleMenu.getId(), 1L);
        Order order = orderDao.save(ORDER(orderTable.getId(), List.of(orderLineItem), COOKING));
        
        // when
        Order orderWithNewStatus = ORDER(orderTable.getId(), List.of(orderLineItem), MEAL);
        Order changedOrder = orderService.changeOrderStatus(order.getId(), orderWithNewStatus);
        
        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL.name());
    }
}
