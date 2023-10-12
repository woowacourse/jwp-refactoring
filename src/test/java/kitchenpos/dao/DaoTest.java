package kitchenpos.dao;

import kitchenpos.DatabaseCleaner;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.fixtures.domain.MenuFixture.createMenu;
import static kitchenpos.fixtures.domain.MenuGroupFixture.createMenuGroupRequest;
import static kitchenpos.fixtures.domain.MenuProductFixture.createMenuProduct;
import static kitchenpos.fixtures.domain.OrderFixture.createOrder;
import static kitchenpos.fixtures.domain.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.fixtures.domain.OrderTableFixture.createOrderTable;
import static kitchenpos.fixtures.domain.ProductFixture.createProduct;
import static kitchenpos.fixtures.domain.TableGroupFixture.createTableGroup;

@SpringBootTest
@Transactional
public class DaoTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void cleanTables() throws SQLException {
        databaseCleaner.clear();
    }

    protected Menu saveMenu(final String name,
                            final int price,
                            final MenuGroup menuGroup,
                            final List<MenuProduct> menuProducts) {
        final Menu menu = createMenu(name, new BigDecimal(price), menuGroup.getId(), menuProducts);
        return menuDao.save(menu);
    }

    protected MenuGroup saveMenuGroup(final String name) {
        final MenuGroup request = createMenuGroupRequest(name);
        return menuGroupDao.save(request);
    }

    protected MenuProduct saveMenuProduct(final long productId,
                                          final long quantity) {
        final MenuProduct request = createMenuProduct(productId, quantity);
        return menuProductDao.save(request);
    }

    protected MenuProduct saveMenuProductWithMenuId(final long menuId,
                                                    final long productId,
                                                    final long quantity) {
        final MenuProduct request = createMenuProduct(menuId, productId, quantity);
        return menuProductDao.save(request);
    }

    protected Order saveOrder(final OrderStatus orderStatus,
                              final OrderTable orderTable,
                              final List<OrderLineItem> orderLineItems) {
        final Order request = createOrder(orderTable.getId(), orderStatus, LocalDateTime.now(), orderLineItems);
        return orderDao.save(request);
    }

    protected OrderLineItem saveOrderLineItem(final long orderId,
                                              final long menuId,
                                              final long quantity) {
        OrderLineItem orderLineItem = createOrderLineItem(orderId,menuId, quantity);
        return orderLineItemDao.save(orderLineItem);
    }
//
//    protected OrderTable saveOrderTable(final int numberOfGuests,
//                                        final boolean empty) {
//        final OrderTable request = createOrderTable(numberOfGuests, empty);
//        return orderTableDao.save(request);
//    }

    protected OrderTable saveOrderTable(final long tableGroupId,
                                        final int numberOfGuests,
                                        final boolean empty) {
        final OrderTable request = createOrderTable(tableGroupId, numberOfGuests, empty);
        return orderTableDao.save(request);
    }

    protected Product saveProduct(final String name,
                                  final int price) {
        final Product request = createProduct(name, new BigDecimal(price));
        return productDao.save(request);
    }

    protected TableGroup saveTableGroup(final OrderTable... orderTables) {
        final TableGroup request = createTableGroup(LocalDateTime.now(), List.of(orderTables));
        return tableGroupDao.save(request);
    }
}
