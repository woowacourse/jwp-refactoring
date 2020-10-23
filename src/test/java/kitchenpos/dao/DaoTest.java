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

    protected JdbcTemplateMenuDao jdbcTemplateMenuDao;
    protected JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;
    protected JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;
    protected JdbcTemplateOrderDao jdbcTemplateOrderDao;
    protected JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;
    protected JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;
    protected JdbcTemplateProductDao jdbcTemplateProductDao;
    protected JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @Autowired
    protected DataSource dataSource;

    @BeforeEach
    void setUpDatabase() {
        jdbcTemplateMenuDao = new JdbcTemplateMenuDao(dataSource);
        jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        jdbcTemplateMenuProductDao = new JdbcTemplateMenuProductDao(dataSource);
        jdbcTemplateOrderDao = new JdbcTemplateOrderDao(dataSource);
        jdbcTemplateOrderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
        jdbcTemplateOrderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        jdbcTemplateProductDao = new JdbcTemplateProductDao(dataSource);
        jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);

        jdbcTemplateMenuGroupDao.save(MENU_GROUP_1);
        jdbcTemplateMenuGroupDao.save(MENU_GROUP_2);

        jdbcTemplateProductDao.save(PRODUCT_1);
        jdbcTemplateProductDao.save(PRODUCT_2);

        jdbcTemplateMenuDao.save(MENU_1);
        jdbcTemplateMenuDao.save(MENU_2);

        jdbcTemplateMenuProductDao.save(MENU_PRODUCT_1);
        jdbcTemplateMenuProductDao.save(MENU_PRODUCT_2);

        jdbcTemplateTableGroupDao.save(TABLE_GROUP);

        OrderTable nullIdOrderTable1 = createNullIdOrderTable(ORDER_TABLE_1);
        OrderTable nullIdOrderTable2 = createNullIdOrderTable(ORDER_TABLE_2);
        jdbcTemplateOrderTableDao.save(nullIdOrderTable1);
        jdbcTemplateOrderTableDao.save(nullIdOrderTable2);

        Order nullIdOrder1 = createNullIdOrder(ORDER_1);
        Order nullIdOrder2 = createNullIdOrder(ORDER_2);
        jdbcTemplateOrderDao.save(nullIdOrder1);
        jdbcTemplateOrderDao.save(nullIdOrder2);

        jdbcTemplateOrderLineItemDao.save(ORDER_LINE_ITEM_1);
        jdbcTemplateOrderLineItemDao.save(ORDER_LINE_ITEM_2);
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
        output.setOrderLineItems(order.getOrderLineItems());

        return output;
    }
}
