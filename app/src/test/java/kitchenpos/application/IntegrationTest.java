package kitchenpos.application;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.TableEmptyChangedEventListener;
import kitchenpos.order.application.TableUngroupedEventListener;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.OrderedEventListener;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
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

    @Autowired
    protected TableEmptyChangedEventListener tableEmptyChangedEventListener;

    @Autowired
    protected OrderedEventListener orderedEventListener;

    @Autowired
    protected TableUngroupedEventListener tableUngroupedEventListener;

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
        return new Order(주문테이블.id(), 주문상태, 주문항목들(주문항목));
    }

    protected OrderLineItems 주문항목들(OrderLineItem... 주문항목) {
        return new OrderLineItems(Arrays.asList(주문항목));
    }

    protected OrderLineItem 주문항목(Menu 메뉴, long 수량) {
        return new OrderLineItem(메뉴.id(), 수량);
    }

    protected TableGroup 테이블그룹(OrderTable... 주문테이블) {
        TableGroup tableGroup = new TableGroup();
        Arrays.stream(주문테이블)
                .forEach(it -> it.group(tableGroup));
        return tableGroup;
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
