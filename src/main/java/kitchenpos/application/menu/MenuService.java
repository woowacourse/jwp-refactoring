package kitchenpos.application.menu;

import kitchenpos.application.menu.request.MenuCreateRequest;
import kitchenpos.application.menu.request.MenuProductRequest;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
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
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(MenuCreateRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<MenuProductRequest> menuProducts = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProducts) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        Menu menu = mapToMenu(request, menuProducts);

        final Menu savedMenu = menuRepository.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProducts) {
            MenuProduct menuProduct = mapToMenuProduct(menuProductRequest, menuId);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.saveMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    private MenuProduct mapToMenuProduct(MenuProductRequest menuProductRequest, Long menuId) {
        return new MenuProduct(
                menuId,
                menuProductRequest.getProductId(),
                menuProductRequest.getQuantity());
    }

    private Menu mapToMenu(MenuCreateRequest request, List<MenuProductRequest> menuProducts) {
        final Menu menu = Menu.of(request.getName(), request.getPrice().longValue(), request.getMenuGroupId());
        for (final MenuProductRequest menuProduct : menuProducts) {
            menu.addProduct(menuProduct.getProductId(), menuProduct.getQuantity());
        }
        return menu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.saveMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
