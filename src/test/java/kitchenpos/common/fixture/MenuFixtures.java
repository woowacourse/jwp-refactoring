package kitchenpos.common.fixture;

import static java.util.stream.Collectors.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.menu.dto.MenuProductSaveRequest;
import kitchenpos.menu.dto.MenuSaveRequest;

public class MenuFixtures {

    public static final Menu generateMenu(final String name, final BigDecimal price, final Long menuGroupId) {
        return generateMenu(null, name, price, menuGroupId, List.of());
    }

    public static final Menu generateMenu(final String name,
                                          final BigDecimal price,
                                          final Long menuGroupId,
                                          final List<MenuProduct> menuProducts) {
        return generateMenu(null, name, price, menuGroupId, menuProducts);
    }

    public static final Menu generateMenu(final Long id, final Menu menu) {
        return generateMenu(id, menu.getName(), menu.getPrice().getValue(), menu.getMenuGroupId(), menu.getMenuProducts());
    }

    public static final Menu generateMenu(final Long id,
                                          final String name,
                                          final BigDecimal price,
                                          final Long menuGroupId,
                                          final List<MenuProduct> menuProducts) {
        try {
            Constructor<Menu> constructor = Menu.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Menu menu = constructor.newInstance();
            Class<? extends Menu> clazz = menu.getClass();

            Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(menu, id);

            Field nameField = clazz.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(menu, name);

            Field priceField = clazz.getDeclaredField("price");
            priceField.setAccessible(true);
            priceField.set(menu, new Price(price));

            Field menuGroupIdField = clazz.getDeclaredField("menuGroupId");
            menuGroupIdField.setAccessible(true);
            menuGroupIdField.set(menu, menuGroupId);

            Field menuProductsField = clazz.getDeclaredField("menuProducts");
            menuProductsField.setAccessible(true);
            menuProductsField.set(menu, menuProducts);

            return menu;
        } catch (final Exception e) {
            throw new RuntimeException();
        }
    }

    public static final MenuSaveRequest generateMenuSaveRequest(final String name,
                                                                final BigDecimal price,
                                                                final Long menuGroupId,
                                                                final List<MenuProduct> menuProducts) {
        List<MenuProductSaveRequest> menuProductSaveRequests = menuProducts.stream()
                .map(it -> new MenuProductSaveRequest(it.getProductId(), it.getQuantity()))
                .collect(toList());
        return new MenuSaveRequest(name, price, menuGroupId, menuProductSaveRequests);
    }
}
