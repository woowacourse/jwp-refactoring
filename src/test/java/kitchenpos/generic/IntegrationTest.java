package kitchenpos.generic;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
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
    @Autowired
    protected OrderValidator orderValidator;

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

    protected Order generateOrder(
            final OrderStatus orderStatus,
            final OrderTable orderTable,
            final List<OrderLineItem> orderLineItems
    ) {
        final Order order = new Order(
                null,
                orderTable.getId(),
                orderStatus,
                LocalDateTime.now(),
                orderLineItems,
                orderValidator
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

    protected TableGroup generateTableGroup() {
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        return tableGroupRepository.save(tableGroup);
    }
}
