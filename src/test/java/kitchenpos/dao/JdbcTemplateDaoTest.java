package kitchenpos.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Sql("/truncate.sql")
@SpringBootTest
public class JdbcTemplateDaoTest {
    protected static final String INSERT_MENU_GROUP = "insert into menu_group (id, name) values (:id, :name)";
    protected static final String INSERT_PRODUCT = "insert into product (id, name, price) values (:id, :name, :price)";
    protected static final String INSERT_MENU = "insert into menu (id, name, price, menu_group_id) " +
            "values (:id, :name, :price, :menu_group_id)";
    protected static final String INSERT_ORDER_TABLE = "insert into order_table (id, number_of_guests, empty) " +
            "values (:id, :number_of_guests, :empty)";
    protected static final String INSERT_ORDERS = "insert into orders (id, order_table_id, order_status, ordered_time) " +
            "values (:id, :order_table_id, :order_status, :ordered_time)";
    protected static final String INSERT_TABLE_GROUP = "insert into table_group (id, created_date) " +
            "values (:id, :created_date)";

    protected static final String DELETE_MENUS = "delete from menu where id in (:ids)";
    protected static final String DELETE_MENU_GROUPS = "delete from menu_group where id in (:ids)";
    protected static final String DELETE_MENU_PRODUCT = "delete from menu_product where seq in (:seqs)";
    protected static final String DELETE_ORDERS = "delete from orders where id in (:ids)";
    protected static final String DELETE_ORDER_LINE_ITEM = "delete from order_line_item where seq in (:seqs)";
    protected static final String DELETE_ORDER_TABLE = "delete from order_table where id in (:ids)";
    protected static final String DELETE_PRODUCT = "delete from product where id in (:ids)";
    protected static final String DELETE_TABLE_GROUP = "delete from table_group where id in (:ids)";

    protected static final int BIG_DECIMAL_FLOOR_SCALE = 2;
    protected List<Long> productIds;
    protected List<Long> tableGroupIds;
    protected List<Long> orderTableIds;
    protected List<Long> orderLineItemSeqs;
    protected List<Long> orderIds;
    protected List<Long> menuProductSeqs;
    protected List<Long> menuGroupIds;
    protected List<Long> menuIds;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected void deleteTableGroup() {
        Map<String, Object> params = Collections.singletonMap("ids", tableGroupIds);
        namedParameterJdbcTemplate.update(DELETE_TABLE_GROUP, params);
    }

    protected void deleteMenu() {
        Map<String, Object> params = Collections.singletonMap("ids", menuIds);
        namedParameterJdbcTemplate.update(DELETE_MENUS, params);
    }

    protected void deleteMenuGroup() {
        Map<String, Object> params = Collections.singletonMap("ids", menuGroupIds);
        namedParameterJdbcTemplate.update(DELETE_MENU_GROUPS, params);
    }

    protected void deleteMenuProduct() {
        Map<String, Object> params = Collections.singletonMap("seqs", menuProductSeqs);
        namedParameterJdbcTemplate.update(DELETE_MENU_PRODUCT, params);
    }

    protected void deleteOrders() {
        Map<String, Object> params = Collections.singletonMap("ids", orderIds);
        namedParameterJdbcTemplate.update(DELETE_ORDERS, params);
    }

    protected void deleteOrderLineItem() {
        Map<String, Object> params = Collections.singletonMap("seqs", orderLineItemSeqs);
        namedParameterJdbcTemplate.update(DELETE_ORDER_LINE_ITEM, params);
    }

    protected void deleteOrderTable() {
        Map<String, Object> params = Collections.singletonMap("ids", orderTableIds);
        namedParameterJdbcTemplate.update(DELETE_ORDER_TABLE, params);
    }

    protected void deleteProduct() {
        Map<String, Object> params = Collections.singletonMap("ids", productIds);
        namedParameterJdbcTemplate.update(DELETE_PRODUCT, params);
    }

    protected void saveTableGroup() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("created_date", LocalDateTime.now());
        namedParameterJdbcTemplate.update(INSERT_TABLE_GROUP, params);
    }

    protected void saveOrderTable(Long orderTableId, int numberOfGuests, boolean empty) {
        Map<String, Object> orderTableParams = new HashMap<>();
        orderTableParams.put("id", orderTableId);
        orderTableParams.put("number_of_guests", numberOfGuests);
        orderTableParams.put("empty", empty);
        namedParameterJdbcTemplate.update(INSERT_ORDER_TABLE, orderTableParams);
    }

    protected void saveOrders(Long orderId, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("id", orderId);
        orderParams.put("order_table_id", orderTableId);
        orderParams.put("order_status", orderStatus);
        orderParams.put("ordered_time", orderedTime);
        namedParameterJdbcTemplate.update(INSERT_ORDERS, orderParams);
    }

    protected void saveMenuGroup(Long menuGroupId, String name) {
        Map<String, Object> menuGroup = new HashMap<>();
        menuGroup.put("id", menuGroupId);
        menuGroup.put("name", name);
        namedParameterJdbcTemplate.update(INSERT_MENU_GROUP, menuGroup);
    }

    protected void saveMenu(Long menuId, String name, BigDecimal price, Long menuGroupId) {
        Map<String, Object> menuParams = new HashMap<>();
        menuParams.put("id", menuId);
        menuParams.put("name", name);
        menuParams.put("price", price);
        menuParams.put("menu_group_id", menuGroupId);
        namedParameterJdbcTemplate.update(INSERT_MENU, menuParams);
    }

    protected void saveProducts(Long productId, String name, BigDecimal price) {
        Map<String, Object> productParams = new HashMap<>();
        productParams.put("id", productId);
        productParams.put("name", name);
        productParams.put("price", price);
        namedParameterJdbcTemplate.update(INSERT_PRODUCT, productParams);
    }
}
