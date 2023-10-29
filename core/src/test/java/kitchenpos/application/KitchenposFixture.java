package kitchenpos.application;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.core.menugroup.dto.MenuGroupResponse;
import kitchenpos.core.product.application.dto.ProductResponse;
import kitchenpos.core.ordertable.application.TableService;
import kitchenpos.core.ordertable.application.dto.TableResponse;
import kitchenpos.core.menu.domain.Menu;
import kitchenpos.core.menugroup.domain.MenuGroup;
import kitchenpos.core.menu.domain.MenuProduct;
import kitchenpos.core.menu.domain.MenuProducts;
import kitchenpos.core.order.domain.OrderLineItem;
import kitchenpos.core.ordertable.domain.OrderTable;
import kitchenpos.core.product.application.ProductService;
import kitchenpos.core.product.domain.Product;
import kitchenpos.core.ordertable.domain.NumberOfGuests;
import kitchenpos.core.product.domain.Name;
import kitchenpos.core.product.domain.Price;
import kitchenpos.core.menugroup.application.MenuGroupService;
import org.springframework.util.ReflectionUtils;

public class KitchenposFixture {
    public static Menu 저장할메뉴만들기(final String name, final String price, final Long menuGroupId,
                                final MenuProduct... menuProducts) {
        return new Menu(9987L, name, new Price(new BigDecimal(price)), menuGroupId, new MenuProducts(List.of(menuProducts)));
    }

    public static MenuProduct 메뉴상품만들기(final Product savedProduct, final long quantity) {
        return new MenuProduct(999L, savedProduct.getPrice(), savedProduct.getId(), quantity);
    }

    public static Product 상품만들기(final String name, final String price, final ProductService productService) {
        final ProductResponse productResponse = productService.create(new Name(name), new Price(new BigDecimal(price)));
        return new Product(productResponse.getId(), productResponse.getName(), productResponse.getPrice());
    }

    public static MenuGroup 메뉴그룹만들기(final MenuGroupService menuGroupService) {
        final MenuGroupResponse response = menuGroupService.create(new Name("코딱지메뉴그룹"));
        return new MenuGroup(response.getId(), response.getName());
    }

    public static OrderTable 주문테이블만들기(final TableService tableService, final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable(new NumberOfGuests(6), isEmpty);
        final TableResponse orderTableId = tableService.create(new NumberOfGuests(6), isEmpty);
        final Field field = ReflectionUtils.findField(OrderTable.class, "id", Long.class);
        assert field != null;
        field.setAccessible(true);
        ReflectionUtils.setField(field, orderTable, orderTableId.getId());

        return orderTable;
    }

    public static OrderLineItem 주문할메뉴만들기(final Long menuId, final int quantity) {
        return new OrderLineItem(menuId, new Price(new BigDecimal("4000")), "메뉴", quantity);
    }
}
