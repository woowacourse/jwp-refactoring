package kitchenpos.menu.application;

import kitchenpos.global.exception.KitchenposException;
import kitchenpos.menu.MenuProductRepository;
import kitchenpos.menu.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menugroup.MenuGroupRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.ProductRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.global.exception.ExceptionInformation.MENU_GROUP_NOT_FOUND;
import static kitchenpos.global.exception.ExceptionInformation.PRODUCT_NOT_FOUND;

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
        final MenuProducts menuProducts = MenuProducts.create(findMenuProducts(menuRequest));

        final Menu menu = Menu.create(
                menuRequest.getName(),
                menuRequest.getPrice(),
                menuGroup.getId(),
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
                    return MenuProduct.create(product, menuProductRequest.getQuantity());
                })
                .collect(Collectors.toList());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
