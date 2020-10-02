package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static kitchenpos.DomainFactory.*;

@Sql("/truncate.sql")
@SpringBootTest
public class ServiceTest {
    protected static final String DELETE_ORDER_LINE_ITEM = "delete from order_line_item where seq in (:seqs)";
    protected static final String DELETE_ORDERS = "delete from orders where id in (:ids)";
    protected static final String DELETE_ORDER_TABLE = "delete from order_table where id in (:ids)";
    protected static final String DELETE_MENU = "delete from menu where id in (:ids)";
    protected static final String DELETE_MENU_GROUPS = "delete from menu_group where id in (:ids)";
    protected static final String DELETE_PRODUCT = "delete from product where id in (:ids)";
    protected static final String DELETE_MENU_PRODUCT = "delete from menu_product where seq in (:seqs)";
    protected static final String DELETE_TABLE_GROUP = "delete from table_group where id in (:ids)";


    protected static final int BIG_DECIMAL_FLOOR_SCALE = 2;
    protected List<Long> menuGroupIds;
    protected List<Long> productIds;
    protected List<Long> menuIds;
    protected List<Long> menuProductSeqs;
    protected List<Long> orderTableIds;
    protected List<Long> orderIds;
    protected List<Long> orderLineItemSeqs;
    protected List<Long> tableGroupIds;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected void deleteMenuProduct() {
        Map<String, Object> params = Collections.singletonMap("seqs", menuProductSeqs);
        namedParameterJdbcTemplate.update(DELETE_MENU_PRODUCT, params);
    }

    protected void deleteProduct() {
        Map<String, Object> params = Collections.singletonMap("ids", productIds);
        namedParameterJdbcTemplate.update(DELETE_PRODUCT, params);
    }

    protected void deleteOrderLineItem() {
        Map<String, Object> params = Collections.singletonMap("seqs", orderLineItemSeqs);
        namedParameterJdbcTemplate.update(DELETE_ORDER_LINE_ITEM, params);
    }

    protected void deleteOrder() {
        Map<String, Object> params = Collections.singletonMap("ids", orderIds);
        namedParameterJdbcTemplate.update(DELETE_ORDERS, params);
    }

    protected void deleteOrderTable() {
        Map<String, Object> params = Collections.singletonMap("ids", orderTableIds);
        namedParameterJdbcTemplate.update(DELETE_ORDER_TABLE, params);
    }

    protected void deleteMenu() {
        Map<String, Object> params = Collections.singletonMap("ids", menuIds);
        namedParameterJdbcTemplate.update(DELETE_MENU, params);
    }

    protected void deleteMenuGroup() {
        Map<String, Object> params = Collections.singletonMap("ids", menuGroupIds);
        namedParameterJdbcTemplate.update(DELETE_MENU_GROUPS, params);
    }

    protected void deleteTableGroup() {
        Map<String, Object> params = Collections.singletonMap("ids", tableGroupIds);
        namedParameterJdbcTemplate.update(DELETE_TABLE_GROUP, params);
    }

    protected MenuGroup saveMenuGroup(MenuGroupDao menuGroupDao, String name) {
        MenuGroup menuGroup = createMenuGroup(name);
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        menuGroupIds.add(savedMenuGroup.getId());
        return savedMenuGroup;
    }

    protected Product saveProduct(ProductDao productDao, String name, BigDecimal price) {
        Product product = createProduct(name, price);
        Product savedProduct = productDao.save(product);
        productIds.add(savedProduct.getId());
        return savedProduct;
    }

    protected Menu saveMenu(MenuDao menuDao, Long menuGroupId, String name, BigDecimal price) {
        Menu menu = createMenu(menuGroupId, name, price);
        Menu savedMenu = menuDao.save(menu);
        menuIds.add(savedMenu.getId());
        return savedMenu;
    }

    protected MenuProduct saveMenuProduct(MenuProductDao menuProductDao, Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = createMenuProduct(menuId, productId, quantity);
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);
        menuProductSeqs.add(savedMenuProduct.getSeq());
        return savedMenuProduct;
    }

    protected OrderTable saveOrderTable(OrderTableDao orderTableDao, int numberOfGuests, boolean empty) {
        return saveOrderTable(orderTableDao, numberOfGuests, empty, null);
    }

    protected OrderTable saveOrderTable(OrderTableDao orderTableDao, int numberOfGuests,
                                        boolean empty, Long tableGroupId) {
        OrderTable orderTable = createOrderTable(numberOfGuests, empty, tableGroupId);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        orderTableIds.add(savedOrderTable.getId());
        return savedOrderTable;
    }

    protected Order saveOrder(OrderDao orderDao, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        Order order = createOrder(orderTableId, orderStatus, orderedTime);
        Order savedOrder = orderDao.save(order);
        orderIds.add(savedOrder.getId());
        return savedOrder;
    }

    protected OrderLineItem saveOrderLineItem(OrderLineItemDao orderLineItemDao, Long orderId,
                                              Long menuId, long quantity) {
        OrderLineItem orderLineItem = createOrderLineItem(orderId, menuId, quantity);
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);
        orderLineItemSeqs.add(savedOrderLineItem.getSeq());
        return savedOrderLineItem;
    }

    protected TableGroup saveTableGroup(TableGroupDao tableGroupDao) {
        TableGroup tableGroup = createTableGroup();
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        tableGroupIds.add(savedTableGroup.getId());
        return savedTableGroup;
    }
}
