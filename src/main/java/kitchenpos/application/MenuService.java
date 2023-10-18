package kitchenpos.application;

import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            MenuProductRepository menuProductRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(Menu menu) {
        if (menu.getName().length() > 255) {
            throw new IllegalArgumentException();
        }

        BigDecimal price = menu.getPrice();

        if (Objects.isNull(price) ||
                (price.compareTo(BigDecimal.ZERO) < 0) ||
                (price.compareTo(BigDecimal.valueOf(Math.pow(10, 20))) >= 0)
        ) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        Menu savedMenu = menuRepository.save(menu);

        Long menuId = savedMenu.getId();
        List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        List<Menu> menus = menuRepository.findAll();

        for (Menu menu : menus) {
            menu.setMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
