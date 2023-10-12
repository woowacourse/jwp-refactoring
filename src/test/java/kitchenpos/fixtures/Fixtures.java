package kitchenpos.fixtures;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Fixtures {

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuProductDao menuProductDao;

    @Autowired
    MenuDao menuDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderLineItemDao orderLineItemDao;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    TableGroupDao tableGroupDao;

    public Menu 메뉴_저장(Long menuGroupId, String name, Long price) {
        Menu menu = new Menu();
        menu.setMenuGroupId(menuGroupId);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        return menuDao.save(menu);
    }

    public MenuGroup 메뉴_그룹_저장(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroupDao.save(menuGroup);
    }

    public MenuProduct 메뉴_상품_저장(Long menuId, Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProductDao.save(menuProduct);
    }

    public Order 주문_저장(Long orderTableId) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        return orderDao.save(order);
    }

    public Order 주문상태가_식사완료인_주문_저장(Long orderTableId) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        return orderDao.save(order);
    }

    public OrderTable 주문_테이블_저장() {
        OrderTable orderTable = new OrderTable();
        return orderTableDao.save(orderTable);
    }

    public OrderTable 빈_테이블_저장() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return orderTableDao.save(orderTable);
    }

    public OrderLineItem 주문_항목_저장(Long orderId, Long menuId, Long quantity, Long seq) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setSeq(seq);
        return orderLineItemDao.save(orderLineItem);
    }

    public Product 상품_저장(String name, Long price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return productDao.save(product);
    }

    public TableGroup 단체_지정_저장(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroupDao.save(tableGroup);
    }

}
