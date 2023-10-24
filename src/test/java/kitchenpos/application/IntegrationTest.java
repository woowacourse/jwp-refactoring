package kitchenpos.application;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.menu.ProductService;
import kitchenpos.application.order.OrderService;
import kitchenpos.application.table.TableGroupService;
import kitchenpos.application.table.TableService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.Price;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.ProductRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class IntegrationTest {

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected ProductRepository productRepository;

    protected Price 가격(long 가격) {
        return new Price(BigDecimal.valueOf(가격));
    }

    protected Product 상품(String 이름, Price 가격) {
        return new Product(이름, 가격);
    }

    protected OrderTable 주문테이블(int 손님숫자, boolean 비어있는지) {
        return new OrderTable(손님숫자, 비어있는지);
    }

    protected MenuGroup 메뉴그룹(String 이름) {
        return new MenuGroup(이름);
    }

    protected MenuProduct 메뉴상품(Product 상품, long 수량) {
        return new MenuProduct(상품, 수량);
    }

    protected Menu 메뉴(String 메뉴이름, Price 가격, MenuGroup 메뉴그룹, MenuProduct... 메뉴상품) {
        return new Menu(메뉴이름, 가격, 메뉴그룹, 메뉴상품들(메뉴상품));
    }

    protected MenuProducts 메뉴상품들(MenuProduct... 메뉴상품) {
        return new MenuProducts(Arrays.asList(메뉴상품));
    }

    protected Order 주문(OrderTable 주문테이블, OrderStatus 주문상태, OrderLineItem... 주문항목) {
        return new Order(주문테이블, 주문상태, 주문항목들(주문항목));
    }

    protected OrderLineItems 주문항목들(OrderLineItem... 주문항목) {
        return new OrderLineItems(Arrays.asList(주문항목));
    }

    protected OrderLineItem 주문항목(Menu 메뉴, long 수량) {
        return new OrderLineItem(메뉴, 수량);
    }

    protected TableGroup 테이블그룹(OrderTable... 주문테이블) {
        return new TableGroup(Arrays.asList(주문테이블));
    }

    protected Product 상품저장(Product 상품) {
        return productRepository.save(상품);
    }

    protected OrderTable 주문테이블저장(OrderTable 주문테이블) {
        return orderTableRepository.save(주문테이블);
    }

    protected MenuGroup 메뉴그룹저장(MenuGroup 메뉴그룹) {
        return menuGroupRepository.save(메뉴그룹);
    }

    protected Menu 메뉴저장(Menu 메뉴) {
        return menuRepository.save(메뉴);
    }

    protected Order 주문저장(Order 주문) {
        return orderRepository.save(주문);
    }

    protected TableGroup 테이블그룹저장(TableGroup 테이블그룹) {
        return tableGroupRepository.save(테이블그룹);
    }

    protected Menu 맛있는_메뉴_저장() {
        MenuGroup 메뉴그룹 = 메뉴그룹저장(메뉴그룹("추천메뉴"));
        Product 상품 = 상품저장(상품("상품1", 가격(1)));
        return 메뉴저장(메뉴("메뉴", 가격(3), 메뉴그룹, 메뉴상품(상품, 3)));
    }
}
