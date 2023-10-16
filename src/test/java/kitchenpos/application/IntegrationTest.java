package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
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
        return generateOrderTable(numberOfGuests, false, generateTableGroup());
    }

    protected OrderTable generateOrderTable(final int numberOfGuests, final boolean empty) {
        return generateOrderTable(numberOfGuests, empty, generateTableGroup());
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
