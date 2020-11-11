package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menuproduct.MenuProductCreateRequest;
import kitchenpos.dto.menuproduct.MenuProductResponse;
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
    public MenuResponse create(MenuCreateRequest menuCreateRequest) {
        validateMenuPrice(menuCreateRequest.getPrice());

        List<MenuProductCreateRequest> menuProductCreateRequests = menuCreateRequest.getMenuProductCreateRequests();
        validateMenuPriceSum(menuCreateRequest.getPrice(), menuProductCreateRequests);

        MenuGroup menuGroup =
                menuGroupRepository.findById(menuCreateRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        Menu menu = menuCreateRequest.toMenu(menuGroup);
        Menu savedMenu = menuRepository.save(menu);

        List<MenuProductResponse> menuProductResponses = createMenuProductResponses(savedMenu,
                                                                                    menuProductCreateRequests);

        return MenuResponse.of(savedMenu, menuProductResponses);
    }

    private void validateMenuPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuPriceSum(BigDecimal price, List<MenuProductCreateRequest> menuProductCreateRequests) {
        BigDecimal sum = BigDecimal.ZERO;

        for (MenuProductCreateRequest menuProductCreateRequest : menuProductCreateRequests) {
            Product product =
                    productRepository.findById(menuProductCreateRequest.getProductId()).orElseThrow(IllegalArgumentException::new);
            BigDecimal productPrice = product.getPrice();
            BigDecimal productQuantity = BigDecimal.valueOf(menuProductCreateRequest.getQuantity());
            sum = sum.add(productPrice.multiply(productQuantity));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProductResponse> createMenuProductResponses(Menu menu,
                                                                 List<MenuProductCreateRequest> menuProductCreateRequests) {
        List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (MenuProductCreateRequest menuProductCreateRequest : menuProductCreateRequests) {
            Product product =
                    productRepository.findById(menuProductCreateRequest.getProductId()).orElseThrow(IllegalArgumentException::new);
            MenuProduct menuProduct = new MenuProduct(menu, product, menuProductCreateRequest.getQuantity());
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }

        List<MenuProductResponse> menuProductResponses = new ArrayList<>();
        for (MenuProduct menuProduct : savedMenuProducts) {
            menuProductResponses.add(MenuProductResponse.from(menuProduct));
        }

        return menuProductResponses;
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();

        List<MenuResponse> menuResponses = new ArrayList<>();
        for (Menu menu : menus) {
            List<MenuProductResponse> menuProductResponses = new ArrayList<>();
            menuProductRepository.findAllByMenuId(menu.getId())
                    .forEach(menuProduct -> menuProductResponses.add(MenuProductResponse.from(menuProduct)));
            MenuResponse menuResponse = MenuResponse.of(menu, menuProductResponses);
            menuResponses.add(menuResponse);
        }

        return menuResponses;
    }
}
