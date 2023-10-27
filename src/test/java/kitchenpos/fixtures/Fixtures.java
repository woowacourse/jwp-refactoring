package kitchenpos.fixtures;

import java.math.BigDecimal;
import kitchenpos.menu.JpaMenuGroupRepository;
import kitchenpos.menu.JpaMenuProductRepository;
import kitchenpos.menu.JpaMenuRepository;
import kitchenpos.order.JpaOrderLineItemRepository;
import kitchenpos.order.JpaOrderRepository;
import kitchenpos.table.JpaOrderTableRepository;
import kitchenpos.prodcut.JpaProductRepository;
import kitchenpos.table.JpaTableGroupRepository;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import kitchenpos.table.OrderTable;
import kitchenpos.prodcut.Price;
import kitchenpos.prodcut.Product;
import kitchenpos.table.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Fixtures {

    @Autowired
    JpaMenuGroupRepository jpaMenuGroupRepository;

    @Autowired
    JpaMenuProductRepository jpaMenuProductRepository;

    @Autowired
    JpaMenuRepository jpaMenuRepository;

    @Autowired
    JpaOrderRepository jpaOrderRepository;

    @Autowired
    JpaOrderLineItemRepository jpaOrderLineItemRepository;

    @Autowired
    JpaOrderTableRepository jpaOrderTableRepository;

    @Autowired
    JpaProductRepository jpaProductRepository;

    @Autowired
    JpaTableGroupRepository jpaTableGroupRepository;

    public Menu 메뉴_저장(MenuGroup menuGroup, String name, Long price) {
        Menu menu = new Menu(name, new Price(BigDecimal.valueOf(price)), menuGroup);
        return jpaMenuRepository.save(menu);
    }

    public MenuGroup 메뉴_그룹_저장(String name) {
        MenuGroup menuGroup = new MenuGroup(name);
        return jpaMenuGroupRepository.save(menuGroup);
    }

    public MenuProduct 메뉴_상품_저장(Menu menu, Product product, Long quantity) {
        MenuProduct menuProduct = new MenuProduct(menu, product.getId(), quantity);
        return jpaMenuProductRepository.save(menuProduct);
    }

    public Order 주문_저장(OrderTable orderTable, OrderStatus orderStatus) {
        Order order = new Order(orderTable, orderStatus);
        return jpaOrderRepository.save(order);
    }

    public Order 주문_저장(OrderTable orderTable) {
        return 주문_저장(orderTable, OrderStatus.COOKING);
    }

    public OrderTable 주문_테이블_저장() {
        OrderTable orderTable = new OrderTable();
        return jpaOrderTableRepository.save(orderTable);
    }

    public OrderTable 빈_테이블_저장() {
        OrderTable orderTable = new OrderTable(true);
        return jpaOrderTableRepository.save(orderTable);
    }

    public OrderTable 주문_테이블_저장(TableGroup tableGroup, boolean isEmpty) {
        OrderTable orderTable = new OrderTable(tableGroup, 0, isEmpty);
        return jpaOrderTableRepository.save(orderTable);
    }

    public OrderTable 주문_테이블_저장(TableGroup tableGroup, boolean isEmpty, int numberOfGuests) {
        OrderTable orderTable = new OrderTable(tableGroup, numberOfGuests, isEmpty);
        return jpaOrderTableRepository.save(orderTable);
    }

    public OrderLineItem 주문_항목_저장(Order order, Menu menu, Long quantity, Long seq) {
        OrderLineItem orderLineItem = new OrderLineItem(order, menu.getId(), quantity);
        return jpaOrderLineItemRepository.save(orderLineItem);
    }

    public Product 상품_저장(String name, Long price) {
        Product product = new Product(name, new Price(BigDecimal.valueOf(price)));
        return jpaProductRepository.save(product);
    }

    public TableGroup 단체_지정_저장() {
        TableGroup tableGroup = new TableGroup();
        return jpaTableGroupRepository.save(tableGroup);
    }

}
