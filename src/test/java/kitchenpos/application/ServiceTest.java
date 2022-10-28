package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.support.DatabaseCleanUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected DatabaseCleanUp databaseCleanUp;

    protected MenuGroup createMenuGroup(final String name) {
        return new MenuGroup(name);
    }

    protected Product createProduct(final String name, final Long price) {
        return new Product(name, new BigDecimal(price));
    }

    protected Menu createMenu(final String name, final Long price, final Long menuGroupId) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuGroupId).get();
        return new Menu(name, new BigDecimal(price), menuGroup);
    }

    protected OrderTable createOrderTable(final Long id, final Long tableGroupId, final int numberOfGuests,
                                          final boolean empty) {
        TableGroup tableGroup = null;
        if (tableGroupId != null) {
            tableGroup = tableGroupRepository.findById(tableGroupId).get();
        }
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
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
