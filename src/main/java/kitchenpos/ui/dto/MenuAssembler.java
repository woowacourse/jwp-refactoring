package kitchenpos.ui.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;

public class MenuAssembler {

    private MenuAssembler() {
    }

    public static Menu assemble(MenuRequest menuRequest, MenuGroup menuGroup,
        List<Product> products) {
        BigDecimal price = menuRequest.getPrice();
        final List<MenuProduct> menuProducts = makeMenuProducts(menuRequest.getMenuProducts(),
            products);

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            BigDecimal productPrice = menuProduct.getProductPrice();
            BigDecimal quantity = BigDecimal.valueOf(menuProduct.getQuantity());
            sum = sum.add(productPrice.multiply(quantity));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        return Menu.entityOf(
            menuRequest.getName(),
            price,
            menuGroup,
            menuProducts
        );
    }

    private static List<MenuProduct> makeMenuProducts(List<MenuProductRequest> menuProducts,
        List<Product> products) {
        return menuProducts.stream()
            .map(menuProduct -> makeMenuProduct(products, menuProduct))
            .collect(Collectors.toList());
    }

    private static MenuProduct makeMenuProduct(List<Product> products,
        MenuProductRequest menuProduct) {
        Product product = findProduct(products, menuProduct.getProductId());
        long quantity = menuProduct.getQuantity();

        return MenuProduct.entityOf(product, quantity);
    }

    private static Product findProduct(List<Product> products, Long productId) {
        return products.stream()
            .filter(product -> productId.equals(product.getId()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
