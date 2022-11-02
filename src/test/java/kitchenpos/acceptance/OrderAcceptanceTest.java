package kitchenpos.acceptance;

import static kitchenpos.DomainFixtures.라면_메뉴;
import static kitchenpos.DomainFixtures.맛있는_라면;
import static kitchenpos.DomainFixtures.면_메뉴_그룹;
import static kitchenpos.DomainFixtures.빈_주문_테이블_3인;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderAcceptanceTest extends AcceptanceTest {

    private Menu 메뉴1;
    private Menu 메뉴2;
    private OrderTable 테이블;

    @BeforeEach
    void setData() {
        Product 라면_제품 = testRestTemplate.postForObject("http://localhost:" + port + "/api/products",
                맛있는_라면(), Product.class);

        MenuGroup 메뉴_그룹 = testRestTemplate.postForObject("http://localhost:" + port + "/api/menu-groups",
                면_메뉴_그룹(), MenuGroup.class);

        MenuProduct menuProduct = new MenuProduct(null, 라면_제품.getId(), 1);
        List<MenuProduct> 메뉴_그룹들 = new ArrayList<>();
        메뉴_그룹들.add(menuProduct);

        MenuRequest 라면_메뉴 = 라면_메뉴();
        라면_메뉴.setMenuGroupId(메뉴_그룹.getId());
        라면_메뉴.setMenuProducts(메뉴_그룹들);

        메뉴1 = testRestTemplate.postForObject("http://localhost:" + port + "/api/menus", 라면_메뉴, Menu.class);
        메뉴2 = testRestTemplate.postForObject("http://localhost:" + port + "/api/menus", 라면_메뉴, Menu.class);

        OrderTableRequest 테이블_3인 = new OrderTableRequest( 3, false);
        테이블 = testRestTemplate.postForObject("http://localhost:" + port + "/api/tables", 테이블_3인,
                OrderTable.class);
    }

    @Test
    void 주문을_추가한다() {
        OrderRequest 주문 = new OrderRequest(테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        주문.addOrderLineItem(new OrderLineItemRequest(null, 메뉴1.getId(), "라면",
                BigDecimal.valueOf(1200), 1));
        주문.addOrderLineItem(new OrderLineItemRequest(null, 메뉴2.getId(),
                "라면", BigDecimal.valueOf(1200), 1));

        Order target = testRestTemplate.postForObject("http://localhost:" + port + "/api/orders", 주문, Order.class);

        assertThat(target.getId()).isNotZero();
        assertThat(target.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    void 주문들을_조회한다() {
        OrderRequest 주문 = new OrderRequest(테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());
        주문.addOrderLineItem(new OrderLineItemRequest(null, 메뉴1.getId(),
                "라면", BigDecimal.valueOf(1200), 1));
        주문.addOrderLineItem(new OrderLineItemRequest(null, 메뉴2.getId(),
                "라면", BigDecimal.valueOf(1200), 1));

        testRestTemplate.postForObject("http://localhost:" + port + "/api/orders", 주문, Order.class);

        List<Order> 주문들 = Arrays.asList(
                testRestTemplate.getForObject("http://localhost:" + port + "/api/orders", Order[].class));

        assertThat(주문들.size()).isEqualTo(1);
    }

    @Test
    void 주문을_수정한다() {
        Order 주문 = new Order(테이블, OrderStatus.COOKING.name(), LocalDateTime.now());
        주문.addOrderLineItem(new OrderLineItem(주문, "라면", BigDecimal.valueOf(1200), 1));
        주문.addOrderLineItem(new OrderLineItem(주문, "라면", BigDecimal.valueOf(1200), 1));

        Order order = testRestTemplate.postForObject("http://localhost:" + port + "/api/orders", 주문, Order.class);

        주문.setOrderStatus(OrderStatus.MEAL.name());
        assertDoesNotThrow(
                () -> testRestTemplate.put("http://localhost:" + port + "/api/orders" + order.getId() + "/order-status", 주문)
        );
    }
}
