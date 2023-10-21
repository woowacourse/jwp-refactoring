package kitchenpos.application;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.response.ProductResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.product.Name;
import kitchenpos.domain.product.Price;
import org.springframework.util.ReflectionUtils;

public class kitchenposFixture {
    public static Menu 저장할메뉴만들기(final String name, final String price, final Long menuGroupId, final MenuProduct... menuProducts) {
        final Menu menu = new Menu();
        menu.setId(9987L);
        menu.setName(name);
        menu.setPrice(new BigDecimal(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(List.of(menuProducts));
        return menu;
    }

    public static MenuProduct 메뉴상품만들기(final Product savedProduct, final long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(999L);
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static Product 상품만들기(final String name, final String price, final ProductService productService) {
        final ProductResponse productResponse = productService.create(new Name(name), new Price(new BigDecimal(price)));
        return new Product(productResponse.getId(), productResponse.getName(), productResponse.getPrice());
    }

    public static MenuGroup 메뉴그룹만들기(final MenuGroupService menuGroupService) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("코딱지메뉴그룹");
        return menuGroupService.create(menuGroup);
    }

    public static OrderTable 주문테이블만들기(final TableService tableService, final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable(6, isEmpty);
        final Long orderTableId = tableService.create(6, isEmpty);
        final Field field = ReflectionUtils.findField(OrderTable.class, "id", Long.class);
        assert field != null;
        field.setAccessible(true);
        ReflectionUtils.setField(field, orderTable, orderTableId);

        return orderTable;
    }

    public static OrderLineItem 주문할메뉴만들기(final Menu savedMenu, final int quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(quantity);
        orderLineItem.setMenuId(savedMenu.getId());
        return orderLineItem;
    }
}
