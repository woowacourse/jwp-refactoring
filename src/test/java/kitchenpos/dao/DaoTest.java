package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.Table;
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
    protected TableDao tableDao;
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
        tableDao = new JdbcTemplateTableDao(dataSource);
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

        Table nullIdTable1 = createNullIdTable(TABLE_1);
        Table nullIdTable2 = createNullIdTable(TABLE_2);
        tableDao.save(nullIdTable1);
        tableDao.save(nullIdTable2);

        Order nullIdOrder1 = createNullIdOrder(ORDER_1);
        Order nullIdOrder2 = createNullIdOrder(ORDER_2);
        orderDao.save(nullIdOrder1);
        orderDao.save(nullIdOrder2);

        orderMenuDao.save(ORDER_MENU_1);
        orderMenuDao.save(ORDER_MENU_2);
    }

    private Table createNullIdTable(Table table) {
        return new Table(null, table.getTableGroupId(), table.getNumberOfGuests(), table.isEmpty());
    }

    private Order createNullIdOrder(Order order) {
        return new Order(null, order.getTableId(), order.getOrderStatus(), order.getOrderedTime());
    }
}
