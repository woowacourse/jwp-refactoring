package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuSnapShotCreator {

    private final ProductRepository productRepository;

    public MenuSnapShotCreator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public OrderLineItem create(Menu menu, int quantity) {
        List<MenuProductSnapShot> productSnapShots = menu.getMenuProducts().stream()
                .map(this::createMenuProductSnapShot)
                .collect(Collectors.toList());
        MenuSnapShot snapShot = new MenuSnapShot(
                menu.getMenuGroup().getName(), menu.getName(), menu.getPrice(), productSnapShots
        );
        return new OrderLineItem(snapShot, quantity);
    }

    private MenuProductSnapShot createMenuProductSnapShot(MenuProduct menuProduct) {
        Product product = productRepository.getById(menuProduct.getProductId());
        return new MenuProductSnapShot(product.getName(), product.getPrice(), menuProduct.getQuantity());
    }
}
