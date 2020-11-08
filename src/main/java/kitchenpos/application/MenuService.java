package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductCreateRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
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

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        final BigDecimal price = menuCreateRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        MenuGroup menuGroup = menuGroupRepository.findById(menuCreateRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        final List<MenuProductCreateRequest> menuProductCreateRequests =
                menuCreateRequest.getMenuProductCreateRequests();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductCreateRequest menuProductCreateRequest : menuProductCreateRequests) {
            final Product product = productRepository.findById(menuProductCreateRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductCreateRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(menuCreateRequest.toMenu(menuGroup));

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductCreateRequest menuProductCreateRequest : menuProductCreateRequests) {
            final Product product = productRepository.findById(menuProductCreateRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            MenuProduct menuProduct = new MenuProduct(product, menuProductCreateRequest.getQuantity());
            menuProduct.setMenu(savedMenu);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        List<MenuProductResponse> menuProductResponses = new ArrayList<>();
        for (final MenuProduct menuProduct : savedMenuProducts) {
            menuProductResponses.add(MenuProductResponse.from(menuProduct));
        }

        return MenuResponse.from(savedMenu, menuProductResponses);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        List<MenuResponse> menuResponses = new ArrayList<>();
        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));

            List<MenuProductResponse> menuProductResponses = new ArrayList<>();
            for (final MenuProduct menuProduct : menu.getMenuProducts()) {
                menuProductResponses.add(MenuProductResponse.from(menuProduct));
            }
            MenuResponse menuResponse = MenuResponse.from(menu, menuProductResponses);
            menuResponses.add(menuResponse);
        }

        return menuResponses;
    }
}
