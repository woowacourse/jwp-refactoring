package kitchenpos.fixtures;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kitchenpos.dao.JpaMenuGroupRepository;
import kitchenpos.dao.JpaMenuProductRepository;
import kitchenpos.dao.JpaMenuRepository;
import kitchenpos.dao.JpaOrderLineItemRepository;
import kitchenpos.dao.JpaOrderRepository;
import kitchenpos.dao.JpaOrderTableRepository;
import kitchenpos.dao.JpaProductRepository;
import kitchenpos.dao.JpaTableGroupRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
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
        Menu menu = new Menu();
        menu.setMenuGroup(menuGroup);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        return jpaMenuRepository.save(menu);
    }

    public MenuGroup 메뉴_그룹_저장(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return jpaMenuGroupRepository.save(menuGroup);
    }

    public MenuProduct 메뉴_상품_저장(Menu menu, Product product, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenu(menu);
        menuProduct.setProduct(product);
        menuProduct.setQuantity(quantity);
        return jpaMenuProductRepository.save(menuProduct);
    }

    public Order 주문_저장(OrderTable orderTable, OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderTable(orderTable);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());
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
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return jpaOrderTableRepository.save(orderTable);
    }

    public OrderTable 주문_테이블_저장(TableGroup tableGroup, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroup(tableGroup);
        orderTable.setEmpty(isEmpty);
        return jpaOrderTableRepository.save(orderTable);
    }

    public OrderTable 주문_테이블_저장(TableGroup tableGroup, boolean isEmpty, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroup(tableGroup);
        orderTable.setEmpty(isEmpty);
        orderTable.setNumberOfGuests(numberOfGuests);
        return jpaOrderTableRepository.save(orderTable);
    }

    public OrderLineItem 주문_항목_저장(Order order, Menu menu, Long quantity, Long seq) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrder(order);
        orderLineItem.setMenu(menu);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setSeq(seq);
        return jpaOrderLineItemRepository.save(orderLineItem);
    }

    public Product 상품_저장(String name, Long price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return jpaProductRepository.save(product);
    }

    public TableGroup 단체_지정_저장() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return jpaTableGroupRepository.save(tableGroup);
    }

}
