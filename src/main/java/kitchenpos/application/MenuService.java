package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.exception.KitchenposException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.request.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.exception.ExceptionInformation.MENU_GROUP_NOT_FOUND;
import static kitchenpos.exception.ExceptionInformation.PRODUCT_NOT_FOUND;

@Service
public class MenuService {
    private final MenuRepository menuRepository;

    private final MenuProductRepository menuProductRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuProductRepository menuProductRepository, final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = findMenuGroup(menuRequest);
        final MenuProducts menuProducts = MenuProducts.from(findMenuProducts(menuRequest));

        final Menu menu = Menu.from(
                menuRequest.getName(),
                menuRequest.getPrice(),
                menuGroup,
                menuProducts
        );
        final Menu savedMenu = menuRepository.save(menu);
        menuProducts.updateMenu(savedMenu);
        menuProductRepository.saveAll(menuProducts.getMenuProducts());
        return savedMenu;
    }

    private MenuGroup findMenuGroup(final MenuRequest menuRequest) {
        return menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new KitchenposException(MENU_GROUP_NOT_FOUND));
    }

    private List<MenuProduct> findMenuProducts(final MenuRequest menuRequest) {
        return menuRequest.getMenuProducts().stream()
                .map(menuProductRequest -> {
                    Product product = productRepository.findById(menuProductRequest.getProductId())
                            .orElseThrow(() -> new KitchenposException(PRODUCT_NOT_FOUND));
                    return MenuProduct.from(product, menuProductRequest.getQuantity());
                })
                .collect(Collectors.toList());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
