package domain.order_lineitem;

import domain.Menu;
import domain.Product;
import domain.menu_product.MenuProduct;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import repository.MenuGroupRepository;
import repository.MenuRepository;
import repository.ProductRepository;
import support.AggregateReference;
import vo.Price;

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
