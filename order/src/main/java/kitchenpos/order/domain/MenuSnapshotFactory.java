package kitchenpos.order.domain;

import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableMap;

import java.util.List;
import java.util.Map;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuSnapshotFactory {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuSnapshotFactory(
        final MenuRepository menuRepository,
        final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    public MenuSnapshot createSnapshot(final Long menuId) {
        final Menu menu = menuRepository.getById(menuId);

        final List<MenuProductSnapshot> menuProductSnapshots
            = createMenuProductSnapshot(menu.getMenuProducts());

        return new MenuSnapshot(menu.getName(), menu.getPrice(), menuProductSnapshots);
    }

    private List<MenuProductSnapshot> createMenuProductSnapshot(
        final List<MenuProduct> menuProducts
    ) {
        final Map<Long, Long> productIdQuantityMap = menuProducts.stream()
            .collect(toUnmodifiableMap(MenuProduct::getProductId, MenuProduct::getQuantity));

        final List<Product> products = productRepository.findAllById(productIdQuantityMap.keySet());

        return products.stream()
            .map(product -> createMenuProductSnapshot(product, productIdQuantityMap))
            .collect(toUnmodifiableList());
    }

    public MenuProductSnapshot createMenuProductSnapshot(
        final Product product,
        final Map<Long, Long> productIdQuantityMap
    ) {
        return new MenuProductSnapshot(
            product.getName(),
            product.getPrice(),
            productIdQuantityMap.get(product.getId())
        );
    }
}
