package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.menu.MenuGroupRepository;
import kitchenpos.dao.menu.MenuProductRepository;
import kitchenpos.dao.menu.MenuRepository;
import kitchenpos.dao.order.OrderLineItemRepository;
import kitchenpos.dao.order.OrderRepository;
import kitchenpos.dao.table.OrderTableRepository;
import kitchenpos.dao.product.ProductRepository;
import kitchenpos.dao.table.TableGroupRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.TableGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql("/truncate.sql")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public abstract class IntegrationTest {

    @Autowired
    protected MenuRepository menuRepository;
    @Autowired
    protected MenuGroupRepository menuGroupRepository;
    @Autowired
    protected MenuProductRepository menuProductRepository;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected OrderLineItemRepository orderLineItemRepository;
    @Autowired
    protected OrderTableRepository orderTableRepository;
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected TableGroupRepository tableGroupRepository;

    protected MenuGroup generateMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupRepository.save(menuGroup);
    }

    protected Menu generateMenu(final String name, final Long price) {
        final Menu menu = new Menu(name, BigDecimal.valueOf(price), generateMenuGroup(name + "-group"));
        return menuRepository.save(menu);
    }

    protected Product generateProduct(final String name, final Long price) {
        final Product product = new Product(name, Price.from(price));
        return productRepository.save(product);
    }

    protected Order generateOrder(final OrderStatus orderStatus, final OrderTable orderTable) {
        final Order order = new Order(
                null,
                orderTable,
                orderStatus,
                LocalDateTime.now()
        );
        return orderRepository.save(order);
    }

    protected OrderTable generateOrderTable(final int numberOfGuests) {
        return generateOrderTable(numberOfGuests, false, null);
    }

    protected OrderTable generateOrderTable(final int numberOfGuests, final boolean empty) {
        return generateOrderTable(numberOfGuests, empty, null);
    }

    protected OrderTable generateOrderTableWithOutTableGroup(final int numberOfGuests, final boolean empty) {
        return generateOrderTable(numberOfGuests, empty, null);
    }

    protected OrderTable generateOrderTable(
            final int numberOfGuests,
            final boolean empty,
            final TableGroup tableGroup
    ) {
        final OrderTable orderTable = new OrderTable(
                tableGroup,
                numberOfGuests,
                empty
        );
        return orderTableRepository.save(orderTable);
    }

    protected TableGroup generateTableGroup(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        return tableGroupRepository.save(tableGroup);
    }
}
