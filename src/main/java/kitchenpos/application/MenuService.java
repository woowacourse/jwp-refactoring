package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.menu.MenuProductRequest;
import kitchenpos.application.dto.request.menu.MenuRequest;
import kitchenpos.application.dto.response.menu.MenuResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProduct.MenuProductBuilder;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            MenuProductRepository menuProductRepository,
            ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(NotFoundMenuGroupException::new);

        Menu menu = menuRequest.toEntity(menuGroup, getMenuProducts(menuRequest.getMenuProductRequests()));

        final Menu savedMenu = menuRepository.save(menu);
        List<MenuProduct> menuProducts = saveMenuProducts(savedMenu, savedMenu.getMenuProducts());
        savedMenu.changeMenuProducts(menuProducts);

        return MenuResponse.create(savedMenu);
    }

    private List<MenuProduct> getMenuProducts(List<MenuProductRequest> menuProductRequests) {

        List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(NotFoundProductException::new);

            menuProducts.add(menuProductRequest.toEntity(product));
        }

        return menuProducts;
    }

    private List<MenuProduct> saveMenuProducts(Menu menu, List<MenuProduct> menuProducts) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();

        for (final MenuProduct menuProduct : menuProducts) {
            MenuProduct saveMenuProduct = new MenuProductBuilder()
                                                .setMenu(menu)
                                                .setProduct(menuProduct.getProduct())
                                                .setQuantity(menuProduct.getQuantity())
                                                .build();

            savedMenuProducts.add(menuProductRepository.save(saveMenuProduct));
        }

        return savedMenuProducts;
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.changeMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(MenuResponse::create)
                .collect(Collectors.toList());
    }
}
