package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
        MenuRepository menuRepository,
        MenuProductRepository menuProductRepository,
        MenuGroupRepository menuGroupRepository,
        ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = findMenuGroupById(request.getMenuGroup());
        Menu menu = menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup));
        MenuProducts menuProducts = generateMenuProducts(menu, request.getMenuProducts());
        menuProducts.validateMenuPrice(new MenuPrice(request.getPrice()));

        return MenuResponse.from(menu, menuProducts.toList());
    }

    private MenuProducts generateMenuProducts(Menu menu, List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductRequest request : menuProductRequests) {
            Product product = findProductById(request.getProductId());
            MenuProduct menuProduct = new MenuProduct(menu, product, request.getQuantity());
            menuProducts.add(menuProductRepository.save(menuProduct));
        }

        return new MenuProducts(menuProducts);
    }

    public List<MenuResponse> list() {
        List<MenuResponse> menuResponses = new ArrayList<>();

        for (Menu menu : menuRepository.findAll()) {
            List<MenuProduct> menuProducts = menuProductRepository.findAllByMenu(menu);
            menuResponses.add(MenuResponse.from(menu, menuProducts));
        }

        return menuResponses;
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new MenuGroupNotFoundException(
                String.format("%d ID를 가진 MenuGroup이 존재하지 않습니다.", menuGroupId)
            ));
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(
                String.format("%d ID를 가진 Product가 존재하지 않습니다.", productId))
            );
    }
}
