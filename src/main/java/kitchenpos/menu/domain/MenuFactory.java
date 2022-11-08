package kitchenpos.menu.domain;

import java.util.List;
import java.util.stream.Collectors;

public class MenuFactory {

    private MenuFactory() {
    }

    public static Menu create(final Menu menuEntity, final List<MenuProduct> menuProductEntity) {
        return new Menu(
                menuEntity.getId(),
                menuEntity.getName(),
                menuEntity.getPrice(),
                menuEntity.getMenuGroupId(),
                createMenuProducts(menuEntity.getId(), menuProductEntity));
    }

    private static List<MenuProduct> createMenuProducts(final Long menuId, final List<MenuProduct> menuProductEntity) {
        return menuProductEntity.stream()
                .map(it -> new MenuProduct(
                        it.getSeq(),
                        menuId,
                        it.getProductId(),
                        it.getPrice(),
                        it.getQuantity()))
                .collect(Collectors.toList());
    }
}
