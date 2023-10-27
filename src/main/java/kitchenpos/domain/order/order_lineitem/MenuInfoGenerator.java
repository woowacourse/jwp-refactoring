package kitchenpos.domain.order.order_lineitem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.menu_product.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.vo.Price;
import kitchenpos.repositroy.MenuGroupRepository;
import kitchenpos.repositroy.MenuRepository;
import kitchenpos.repositroy.ProductRepository;
import kitchenpos.support.AggregateReference;
import org.springframework.stereotype.Component;

@Component
public class MenuInfoGenerator {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuInfoGenerator(
            final MenuRepository menuRepository,
            final ProductRepository productRepository,
            final MenuGroupRepository menuGroupRepository
    ) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuInfo generateMenuInfo(final AggregateReference<Menu> menuId) {
        final Menu menu = menuRepository.getById(menuId.getId());
        final String menuName = menu.getMenuName().getName();
        final String menuGroupName = menuGroupRepository.getById(menu.getMenuGroup().getId()).getMenuGroupName()
                .getName();
        final BigDecimal price = menu.getMenuPrice().getPrice();
        final List<MenuProductInfo> menuProductInfos = menu.getMenuProducts().menuProducts().stream()
                .map(this::generateMenuProductInfo)
                .collect(Collectors.toUnmodifiableList());

        return new MenuInfo(menuName, menuGroupName, price, menuProductInfos);
    }

    private MenuProductInfo generateMenuProductInfo(final MenuProduct menuProduct) {
        final Product product = productRepository.getById(menuProduct.getProductId().getId());
        final Price productPrice = product.getProductPrice();
        return new MenuProductInfo(
                product.getProductName().getName(),
                productPrice.getPrice(),
                menuProduct.getQuantity()
        );
    }
}
