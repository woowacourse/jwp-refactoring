package kitchenpos.application;

import kitchenpos.order.controller.dto.OrderChangeStatusRequest;
import kitchenpos.order.controller.dto.OrderCreateRequest;
import kitchenpos.order.controller.dto.OrderLineItemRequest;
import kitchenpos.order.exception.InvalidOrderChangeException;
import kitchenpos.order.exception.InvalidOrderException;
import kitchenpos.menu.exception.NotExistMenuException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static kitchenpos.order.domain.OrderStatus.*;
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
    private MenuGroupRepository menuGroupRepository;
    
    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderTableRepository orderTableRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderService orderService;
    
    private MenuGroup chineseMenuGroup;
    
    private Menu chineseNoodleMenu;
    
    private MenuProduct chineseNoodleMenuProduct;
    
    private Product chineseNoodle;
    
    @BeforeEach
    void setup() {
        final MenuGroup menuGroup = MENU_GROUP("중국식 메뉴 그룹");
        chineseMenuGroup = menuGroupRepository.save(menuGroup);
        
        final Product product = PRODUCT("짜장면", 8000L);
        chineseNoodle = productRepository.save(product);
        
        chineseNoodleMenuProduct = MENU_PRODUCT(chineseNoodle);
        chineseNoodleMenu = MENU(chineseNoodle.getName(), 8000L, chineseMenuGroup, List.of(chineseNoodleMenuProduct));
        menuRepository.save(chineseNoodleMenu);
    }
    
    @Test
    void 주문을_생성할_때_주문_항목이_없으면_예외가_발생한다() {
        // given
        OrderTable orderTable = ORDER_TABLE(false, 3);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        
        // when & then
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(),
                COOKING.name(),
                Collections.EMPTY_LIST
        );
        
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessageContaining("주문 항목이 없으면 주문할 수 없습니다");
    }
    
    @Test
    void 주문을_생성할_때_주문_항목에_존재하지_않는_메뉴가_있으면_예외를_발생한다() {
        // given
        OrderTable orderTable = ORDER_TABLE(false, 3);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        
        Long notExistMenuId = 200L;
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(chineseNoodleMenu.getId(), 3L);
        OrderLineItemRequest orderLineItemRequestWithNotExistMenuId = new OrderLineItemRequest(notExistMenuId, 1L);
        
        // when & then
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(),
                COOKING.name(),
                List.of(orderLineItemRequest, orderLineItemRequestWithNotExistMenuId)
        );
        
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(NotExistMenuException.class)
                .hasMessageContaining("존재하지 않는 메뉴입니다");
    }
    
    @Test
    void 주문을_생성할_때_주문이_속한_주문_테이블이_비어_있으면_예외가_발생한다() {
        // given
        OrderTable emptyOrderTable = ORDER_TABLE(true, 3);
        OrderTable savedEmptyOrderTable = orderTableRepository.save(emptyOrderTable);
        
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(chineseNoodleMenu.getId(), 3L);
        
        // when & then
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                savedEmptyOrderTable.getId(),
                COOKING.name(),
                List.of(orderLineItemRequest)
        );
        
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessageContaining("빈 테이블에서는 주문할 수 없습니다");
        
    }
    
    @Test
    void 주문을_생성할_때_모든_조건을_만족하면_정상적으로_주문을_생성한다() {
        // given
        OrderTable orderTable = ORDER_TABLE(false, 3);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        
        // when
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(chineseNoodleMenu.getId(), 3L);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                savedOrderTable.getId(),
                COOKING.name(),
                List.of(orderLineItemRequest));
        Order actual = orderService.create(orderCreateRequest);
        
        Order expected = Order.of(savedOrderTable,
                COOKING,
                List.of(new OrderLineItem(null, chineseNoodleMenu, 3L)));
        
        // then : 주문에 속한 주문 항목들은 모두 주문id를 가지고 있다
        assertSoftly(softly -> {
            softly.assertThat(actual)
                  .usingRecursiveComparison()
                  .ignoringFields("id", "orderLineItems", "orderedTime")
                  .isEqualTo(expected);
            
            for (OrderLineItem orderLineItem : orderLineItemRepository.findAll()) {
                softly.assertThat((actual))
                      .usingRecursiveComparison()
                      .isEqualTo(orderLineItem.getOrder());
            }
        });
    }
    
    @Test
    void 모든_주문을_조회한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(ORDER_TABLE(false, 1));
        OrderLineItem orderLineItem = ORDER_LINE_ITEM(chineseNoodleMenu, 3L);
        
        Order order1 = ORDER(orderTable, List.of(orderLineItem), COOKING);
        Order savedOrder1 = orderRepository.save(order1);
        Order order2 = ORDER(orderTable, List.of(orderLineItem), COOKING);
        Order savedOrder2 = orderRepository.save(order2);
        
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
    void 주문의_상태를_변경할_때_이미_주문이_종료된_상태이면_예외가_발생한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(ORDER_TABLE(false, 1));
        OrderLineItem orderLineItem = ORDER_LINE_ITEM(chineseNoodleMenu, 1L);
        Order order = orderRepository.save(ORDER(orderTable, List.of(orderLineItem), COMPLETION));
        
        // when & then
        OrderChangeStatusRequest orderChangeStatusRequest = new OrderChangeStatusRequest(COMPLETION.name());
        
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderChangeStatusRequest))
                .isInstanceOf(InvalidOrderChangeException.class)
                .hasMessageContaining("이미 종료된 주문입니다");
    }
    
    @Test
    void 주문의_상태를_변경할_때_모든_조건을_만족하면_주문의_상태를_변경한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(ORDER_TABLE(false, 1));
        OrderLineItem orderLineItem = ORDER_LINE_ITEM(chineseNoodleMenu, 1L);
        Order order = orderRepository.save(ORDER(orderTable, List.of(orderLineItem), COOKING));
        
        // when
        OrderChangeStatusRequest orderChangeStatusRequest = new OrderChangeStatusRequest(MEAL.name());
        
        Order changedOrder = orderService.changeOrderStatus(order.getId(), orderChangeStatusRequest);
        
        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL);
    }
}
