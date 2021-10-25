package kitchenpos.application;

import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ServiceTest {
    protected Product product;
    protected Menu menu;
    protected MenuProduct menuProduct;
    protected List<MenuProduct> menuProducts;
    protected MenuGroup menuGroup;

    protected Order order;
    protected OrderLineItem orderLineItem;
    protected List<OrderLineItem> orderLineItems;

    protected OrderTable orderTable;
    protected OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setName("분식");
        menuGroup.setId(1L);

        product = new Product();
        product.setPrice(BigDecimal.valueOf(1000L));
        product.setName("어묵");
        product.setId(1L);

        menu = new Menu();
        menu.setName("떡볶이");
        menu.setId(1L);

        menuProduct = new MenuProduct();
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setSeq(1L);
        menuProduct.setQuantity(100);

        menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        menu.setMenuProducts(menuProducts);
        menu.setPrice(BigDecimal.valueOf(2000.0));
        menu.setMenuGroupId(menuGroup.getId());

        orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(10);

        orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);

        order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(1L);
        order.setOrderLineItems(orderLineItems);

        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(3);

        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(false);
        orderTable2.setTableGroupId(1L);
        orderTable2.setNumberOfGuests(6);
    }
}
