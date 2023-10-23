package kitchenpos.support;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewTestSupporter {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    public Product createProduct() {
        final Product product = new Product("name", 10_000);
        return productRepository.save(product);
    }

    public MenuGroup createMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup("name");
        return menuGroupRepository.save(menuGroup);
    }

    public Menu createMenu() {
        final Menu menu = new Menu("name",
                                   Price.from(50_000),
                                   createMenuGroup(),
                                   null);
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(5,
                                                                       menu,
                                                                       createProduct()));
        menu.addMenuProducts(menuProducts);
        return menuRepository.save(menu);
    }
}
