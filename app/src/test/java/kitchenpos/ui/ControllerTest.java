package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.application.dto.CreateMenuResponse;
import kitchenpos.menu.application.dto.SearchMenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class ControllerTest {

    @MockBean
    protected MenuGroupService menuGroupService;

    @MockBean
    protected MenuService menuService;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected TableGroupService tableGroupService;

    @MockBean
    protected TableService tableService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected CreateMenuResponse 메뉴_응답() {
        MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
        Product product = new Product(1L, "상품", new Price(BigDecimal.ONE));
        MenuProduct menuProduct = new MenuProduct(1L, product, 3);
        Menu menu = new Menu(1L, "메뉴", new Price(BigDecimal.ONE), menuGroup, new MenuProducts(List.of(menuProduct)));
        return CreateMenuResponse.from(menu);
    }

    protected SearchMenuResponse 메뉴_조회_응답() {
        MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
        Product product = new Product(1L, "상품", new Price(BigDecimal.ONE));
        MenuProduct menuProduct = new MenuProduct(1L, product, 3);
        Menu menu = new Menu(1L, "메뉴", new Price(BigDecimal.ONE), menuGroup, new MenuProducts(List.of(menuProduct)));
        return SearchMenuResponse.from(menu);
    }

    protected Order 식사중인_주문(Long id) {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1);
        return new Order(
                id,
                1L,
                OrderStatus.MEAL,
                null,
                new OrderLineItems(List.of(orderLineItem))
        );
    }

    protected TableGroup 테이블_그룹(Long id) {
        return new TableGroup(id, null);
    }

    protected OrderTable 주문_테이블(Long id) {
        return new OrderTable(id, null, 0, true);
    }
}
