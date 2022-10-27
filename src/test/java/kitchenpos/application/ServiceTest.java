package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.DatabaseCleanUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

    @Autowired
    protected DatabaseCleanUp databaseCleanUp;

    protected MenuGroup createMenuGroup(final String name) {
        return new MenuGroup(name);
    }

    protected Product createProduct(final String name, final Long price) {
        return new Product(name, new BigDecimal(price));
    }

    protected Menu createMenu(final String name, final Long price, final Long menuGroupId) {
        return new Menu(name, new BigDecimal(price), menuGroupId);
    }

    protected OrderTable createOrderTable(final Long id, final Long tableGroupId, final int numberOfGuests,
                                          final boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    protected OrderTable createOrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        return createOrderTable(null, tableGroupId, numberOfGuests, empty);
    }

    protected OrderTable createOrderTable(final int numberOfGuests, final boolean empty) {
        return createOrderTable(null, null, numberOfGuests, empty);
    }

    protected TableGroup createTableGroup(final LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }

}
