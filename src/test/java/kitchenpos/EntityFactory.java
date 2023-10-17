package kitchenpos;

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
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.singletonList;

@Component
public class EntityFactory {

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;
    @Autowired
    private MenuProductRepository menuProductRepository;
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
        final OrderTable request = new OrderTable();
        request.setNumberOfGuests(5);
        request.setEmpty(true);

        return orderTableRepository.save(request);
    }

    public OrderTable saveOrderTableWithNotEmpty() {
        final OrderTable request = new OrderTable();
        request.setNumberOfGuests(5);

        return orderTableRepository.save(request);
    }

    public OrderTable saveOrderTableWithTableGroup(final TableGroup tableGroup) {
        final OrderTable request = new OrderTable();
        request.setNumberOfGuests(5);
        request.setTableGroupId(tableGroup.getId());

        return orderTableRepository.save(request);
    }

    public TableGroup saveTableGroup(final OrderTable orderTable1, final OrderTable orderTable2) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        return tableGroupRepository.save(tableGroup);
    }

    public TableGroup saveTableGroup() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        return tableGroupRepository.save(tableGroup);
    }

    public Order saveOrder(final OrderTable orderTable) {
        final Menu menu = saveMenu();
        final OrderLineItem orderLineItem = createOrderLineItem(menu, 2);

        final Order request = new Order();
        request.setOrderTableId(orderTable.getId());
        request.setOrderStatus(OrderStatus.COOKING);
        final Order order = orderRepository.save(request);

        orderLineItem.setOrderId(order.getId());
        final OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItem);
        order.setOrderLineItems(List.of(savedOrderLineItem));

        return orderRepository.save(order);
    }

    public Order saveOrder() {
        final OrderTable orderTable = saveOrderTableWithNotEmpty();
        final Menu menu = saveMenu();
        final OrderLineItem orderLineItem = createOrderLineItem(menu, 2);

        final Order request = new Order();
        request.setOrderTableId(orderTable.getId());
        request.setOrderStatus(OrderStatus.COOKING);
        final Order order = orderRepository.save(request);

        orderLineItem.setOrderId(order.getId());
        final OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItem);
        order.setOrderLineItems(List.of(savedOrderLineItem));

        return orderRepository.save(request);
    }

    public OrderLineItem createOrderLineItem(final Menu menu, final int quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    public Menu saveMenu() {
        final Product product = saveProduct("연어", 4000);
        final MenuProduct menuProduct = createMenuProduct(4, product);
        final MenuGroup menuGroup = saveMenuGroup("일식");

        final Menu request = new Menu();
        request.setMenuGroupId(menuGroup.getId());
        request.setPrice(BigDecimal.valueOf(16000));
        request.setName("떡볶이 세트");
        final Menu menu = menuRepository.save(request);

        menuProduct.setMenuId(menu.getId());
        final MenuProduct saved = menuProductRepository.save(menuProduct);

        menu.setMenuProducts(singletonList(saved));

        return menuRepository.save(request);
    }

    public Product saveProduct(final String name, final int price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return productRepository.save(product);
    }

    public MenuProduct createMenuProduct(final int quantity, final Product product) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    public MenuGroup saveMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroupRepository.save(menuGroup);
    }
}
