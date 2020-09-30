package kitchenpos.dao;

import static kitchenpos.constants.DaoConstants.TEST_MENU_GROUP_NAME;
import static kitchenpos.constants.DaoConstants.TEST_MENU_NAME;
import static kitchenpos.constants.DaoConstants.TEST_MENU_PRICE;
import static kitchenpos.constants.DaoConstants.TEST_ORDER_ORDERED_TIME;
import static kitchenpos.constants.DaoConstants.TEST_ORDER_TABLE_EMPTY;
import static kitchenpos.constants.DaoConstants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS;
import static kitchenpos.constants.DaoConstants.TEST_PRODUCT_NAME;
import static kitchenpos.constants.DaoConstants.TEST_PRODUCT_PRICE;
import static kitchenpos.constants.DaoConstants.TEST_TABLE_GROUP_CREATED_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class KitchenPosDaoTest {

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    protected Long getCreatedMenuId() {
        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        menu.setPrice(TEST_MENU_PRICE);
        menu.setMenuGroupId(getCreatedMenuGroupId());

        Menu savedMenu = menuDao.save(menu);

        Long savedMenuId = savedMenu.getId();
        assertThat(savedMenuId).isNotNull();
        return savedMenuId;
    }

    protected Long getCreatedMenuGroupId() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(TEST_MENU_GROUP_NAME);

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Long savedMenuGroupId = savedMenuGroup.getId();
        assertThat(savedMenuGroupId).isNotNull();
        return savedMenuGroupId;
    }

    protected Long getCreatedOrderId() {
        Order order = new Order();
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(getCreatedOrderTableId());

        Order savedOrder = orderDao.save(order);

        Long savedOrderId = savedOrder.getId();
        assertThat(savedOrderId).isNotNull();
        return savedOrderId;
    }

    protected Long getCreatedOrderTableId() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY);
        orderTable.setTableGroupId(getCreatedTableGroupId());

        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Long savedOrderTableId = savedOrderTable.getId();
        assertThat(savedOrderTableId).isNotNull();
        return savedOrderTableId;
    }

    protected Long getCreatedProductId() {
        Product product = new Product();
        product.setName(TEST_PRODUCT_NAME);
        product.setPrice(TEST_PRODUCT_PRICE);

        Product savedProduct = productDao.save(product);

        Long savedProductId = savedProduct.getId();
        assertThat(savedProductId).isNotNull();
        return savedProductId;
    }

    protected Long getCreatedTableGroupId() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(TEST_TABLE_GROUP_CREATED_DATE);

        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        Long savedTableGroupId = savedTableGroup.getId();
        assertThat(savedTableGroupId).isNotNull();
        return savedTableGroupId;
    }


}
