package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.jpa.MenuGroupRepository;
import kitchenpos.dao.jpa.MenuProductRepository;
import kitchenpos.dao.jpa.MenuRepository;
import kitchenpos.dao.jpa.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public MenuResponse create(CreateMenuRequest request) {
        validateMenuGroupExists(request.getMenuGroupId());

        final BigDecimal price = request.getPrice();
        validatePriceIsExistsAndNonNegative(price);

        List<MenuProductRequest> menuProductRequests = request.getMenuProducts();
        validateMenuPriceIsNotBiggerThanActualPrice(menuProductRequests, price);

        Menu menu = createMenuWith(request);

        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            MenuProduct menuProduct = createMenuProductWith(menuProductRequest, menu);
            menuProducts.add(menuProduct);
        }

        menu.setMenuProducts(menuProducts);
        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private MenuProduct createMenuProductWith(MenuProductRequest menuProductRequest, Menu menu) {
        MenuProduct menuProduct = new MenuProduct();

        Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow();

        menuProduct.setProduct(product);
        menuProduct.setQuantity(menuProductRequest.getQuantity());
        menuProduct.setMenu(menu);
        return menuProduct;
    }

    private Menu createMenuWith(CreateMenuRequest request) {
        Menu menu = new Menu();
        menu.setName(request.getName());
        menu.setPrice(request.getPrice());
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow();
        menu.setMenuGroup(menuGroup);
        return menu;
    }

    private void validateMenuPriceIsNotBiggerThanActualPrice(List<MenuProductRequest> menuProductRequests, BigDecimal price) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePriceIsExistsAndNonNegative(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuGroupExists(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> findAll() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
