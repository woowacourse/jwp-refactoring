package kitchenpos;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.singletonList;

@Component
public class EntityFactory {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;

    public OrderTable saveOrderTable() {
        final OrderTable request = new OrderTable(5, true);
        return orderTableRepository.save(request);
    }

    public OrderTable saveOrderTableWithNotEmpty() {
        final OrderTable request = new OrderTable(5, false);
        return orderTableRepository.save(request);
    }

    public TableGroup saveTableGroup(final OrderTable orderTable1, final OrderTable orderTable2) {
        final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));
        return tableGroupRepository.save(tableGroup);
    }

    public Order saveOrder(final OrderTable orderTable) {
        final Menu menu = saveMenu();
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 2);

        final Order request = new Order(orderTable.getId(), List.of(orderLineItem));
        return orderRepository.save(request);
    }

    public Order saveOrder() {
        final OrderTable orderTable = saveOrderTableWithNotEmpty();
        final Menu menu = saveMenu();
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 2);

        final Order request = new Order(orderTable.getId(), List.of(orderLineItem));
        return orderRepository.save(request);
    }

    public Menu saveMenu() {
        final Product product = saveProduct("연어", 4000);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 4);
        final MenuGroup menuGroup = saveMenuGroup("일식");

        final Menu request = new Menu("떡볶이 세트", BigDecimal.valueOf(16000), menuGroup.getId(),
                singletonList(menuProduct));

        return menuRepository.save(request);
    }

    public Product saveProduct(final String name, final int price) {
        final Product product = new Product(name, BigDecimal.valueOf(price));
        return productRepository.save(product);
    }

    public MenuGroup saveMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupRepository.save(menuGroup);
    }
}
