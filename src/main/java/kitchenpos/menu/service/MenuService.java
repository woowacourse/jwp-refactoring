package kitchenpos.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.ProductPriceSnapshot;
import kitchenpos.menu.dto.request.CreateMenuRequest;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.exception.ProductNotFoundException;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(CreateMenuRequest request) {
        Menu menu = saveMenu(request);
        List<MenuProduct> menuProducts = setupMenuProducts(request, menu);
        menuRepository.save(menu);

        return MenuResponse.from(menu, menuProducts);
    }

    private Menu saveMenu(CreateMenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(MenuGroupNotFoundException::new);

        return new Menu(menuGroup.getId(), request.getName(), request.getPrice());
    }

    private List<MenuProduct> setupMenuProducts(CreateMenuRequest request, Menu menu) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(ProductNotFoundException::new);

            ProductPriceSnapshot priceSnapshot = new ProductPriceSnapshot(product.getPrice());
            menuProducts.add(new MenuProduct(product.getId(),menuProductRequest.getQuantity(), priceSnapshot));
        }
        menu.setupMenuProducts(menuProducts);

        return menuProducts;
    }

    public List<MenuResponse> findAll() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(each -> MenuResponse.from(each, each.getMenuProducts()))
                .collect(Collectors.toList());
    }
}
