package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.application.dto.OrderCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

public final class OrderSnapShotValidator {
    public static void validate(final Menu menu, final OrderCreateRequest.MenuSnapShot menuSnapShot) {
        if (!menu.getName().equals(menuSnapShot.getName())) {
            throw new IllegalArgumentException();
        }
        if (menu.getPrice().compareTo(menuSnapShot.getPrice()) != 0) {
            throw new IllegalArgumentException();
        }
        validateProductSnapShots(menu.getMenuProducts(), menuSnapShot.getProductSnapShots());
    }

    private static void validateProductSnapShots(final List<MenuProduct> menuProducts, final List<OrderCreateRequest.MenuSnapShot.ProductSnapShot> productSnapShots) {
        if (menuProducts.size() != productSnapShots.size()) {
            throw new IllegalArgumentException();
        }
        final List<MenuProduct> sortedMenuProducts = menuProducts.stream()
                .sorted((o1, o2) -> Math.toIntExact(o1.getSeq() - o2.getSeq()))
                .collect(Collectors.toList());
        final List<OrderCreateRequest.MenuSnapShot.ProductSnapShot> sortedProductSnapShots = productSnapShots.stream()
                .sorted((o1, o2) -> Math.toIntExact(o1.getProductId() - o2.getProductId()))
                .collect(Collectors.toList());
        for (int i = 0; i < menuProducts.size(); i++) {
            final MenuProduct menuProduct = sortedMenuProducts.get(i);
            final OrderCreateRequest.MenuSnapShot.ProductSnapShot productSnapShot = sortedProductSnapShots.get(i);
            validateProductSnapShot(menuProduct, productSnapShot);
        }
    }

    private static void validateProductSnapShot(final MenuProduct menuProduct, final OrderCreateRequest.MenuSnapShot.ProductSnapShot productSnapShot) {
        if (!menuProduct.getSeq().equals(productSnapShot.getMenuProductId())) {
            throw new IllegalArgumentException();
        }
        if (!(menuProduct.getQuantity() == productSnapShot.getQuantity())) {
            throw new IllegalArgumentException();
        }
        if (!menuProduct.getProduct().getId().equals(productSnapShot.getProductId())) {
            throw new IllegalArgumentException();
        }
        if (!menuProduct.getProduct().getName().equals(productSnapShot.getName())) {
            throw new IllegalArgumentException();
        }
        if (!menuProduct.getProduct().getPrice().equals(productSnapShot.getPrice())) {
            throw new IllegalArgumentException();
        }
    }
}
