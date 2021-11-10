package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Products;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService
            (MenuRepository menuRepository,
             MenuGroupRepository menuGroupRepository,
             MenuProductRepository menuProductRepository,
             ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<Long> productIds = menu.getProductIds();
        Products products = new Products(productRepository.findAllById(productIds));
        BigDecimal sum = products.sumTotalPrice(menu.getMenuProducts());
        menu.validatePrice(sum);

        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.connectMenu(savedMenu);
            menuProductRepository.save(menuProduct);
        }

        return savedMenu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
