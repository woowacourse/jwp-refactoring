package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        final List<Product> products = mapToProducts(menuRequest);
        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup,
                menuRequest.getMenuProducts(), products);
        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProduct> savedMenuProducts = createMenuProducts(menuRequest, savedMenu);
        return new Menu(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(), savedMenu.getMenuGroup(),
                savedMenuProducts, products);
    }

    private List<MenuProduct> createMenuProducts(MenuRequest menuRequest, Menu savedMenu) {
        return menuRequest.getMenuProducts()
                .stream()
                .map(menuProduct -> menuProductRepository.save(
                        new MenuProduct(findMenu(savedMenu), menuProduct.getProductId(), menuProduct.getQuantity())))
                .collect(Collectors.toList());
    }

    private Menu findMenu(Menu menu) {
        return menuRepository.findById(menu.getId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<Product> mapToProducts(MenuRequest menuRequest) {
        return menuRequest.getMenuProducts()
                .stream()
                .map(this::findProductByMenuProduct)
                .collect(Collectors.toList());
    }

    private Product findProductByMenuProduct(MenuProduct menuProduct) {
        return productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
