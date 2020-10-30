package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

//TODO: 현재 모든 조회 쿼리에 연관 테이블 join이 빠져있다
@JdbcTest
@Sql({"classpath:truncate.sql"})
public abstract class DaoTest extends TestFixture {

    protected MenuDao menuDao;
    protected MenuGroupDao menuGroupDao;
    protected MenuProductDao menuProductDao;
    protected OrderDao orderDao;
    protected OrderMenuDao orderMenuDao;
    protected OrderTableDao orderTableDao;
    protected ProductDao productDao;
    protected TableGroupDao tableGroupDao;

    @Autowired
    protected DataSource dataSource;

    @BeforeEach
    void setUpDatabase() {
        menuDao = new JdbcTemplateMenuDao(dataSource);
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        menuProductDao = new JdbcTemplateMenuProductDao(dataSource);
        orderDao = new JdbcTemplateOrderDao(dataSource);
        orderMenuDao = new JdbcTemplateOrderMenuDao(dataSource);
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        productDao = new JdbcTemplateProductDao(dataSource);
        tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);

        menuGroupDao.save(MENU_GROUP_1);
        menuGroupDao.save(MENU_GROUP_2);

        productDao.save(PRODUCT_1);
        productDao.save(PRODUCT_2);

        menuDao.save(MENU_1);
        menuDao.save(MENU_2);

        menuProductDao.save(MENU_PRODUCT_1);
        menuProductDao.save(MENU_PRODUCT_2);

        tableGroupDao.save(TABLE_GROUP);

        OrderTable nullIdOrderTable1 = createNullIdOrderTable(ORDER_TABLE_1);
        OrderTable nullIdOrderTable2 = createNullIdOrderTable(ORDER_TABLE_2);
        orderTableDao.save(nullIdOrderTable1);
        orderTableDao.save(nullIdOrderTable2);

        Order nullIdOrder1 = createNullIdOrder(ORDER_1);
        Order nullIdOrder2 = createNullIdOrder(ORDER_2);
        orderDao.save(nullIdOrder1);
        orderDao.save(nullIdOrder2);

        orderMenuDao.save(ORDER_MENU_1);
        orderMenuDao.save(ORDER_MENU_2);
    }

    private OrderTable createNullIdOrderTable(OrderTable orderTable) {
        OrderTable output = new OrderTable();
        output.setEmpty(orderTable.isEmpty());
        output.setNumberOfGuests(orderTable.getNumberOfGuests());
        output.setTableGroupId(orderTable.getTableGroupId());

        return output;
    }

    private Order createNullIdOrder(Order order) {
        Order output = new Order();
        output.setOrderTableId(order.getOrderTableId());
        output.setOrderStatus(order.getOrderStatus());
        output.setOrderedTime(order.getOrderedTime());
        output.setOrderMenus(order.getOrderMenus());

        return output;
    }
}
