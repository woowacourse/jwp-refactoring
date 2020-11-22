package kitchenpos.application;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuPrice;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductPrice;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menuproduct.MenuProductCreateRequest;
import kitchenpos.dto.menuproduct.MenuProductResponse;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public MenuResponse createMenu(MenuCreateRequest menuCreateRequest) {
        MenuPrice menuPrice = MenuPrice.from(menuCreateRequest.getPrice());

        List<MenuProductCreateRequest> menuProductCreateRequests = menuCreateRequest.getMenuProductCreateRequests();
        validateMenuPrice(menuPrice, menuProductCreateRequests);

        Long menuGroupId = menuCreateRequest.getMenuGroupId();
        MenuGroup menuGroup =
                menuGroupRepository.findById(menuGroupId).orElseThrow(() -> new MenuGroupNotFoundException(menuGroupId));
        Menu savedMenu = menuRepository.save(new Menu(menuCreateRequest.getName(), menuPrice, menuGroup));

        List<MenuProductResponse> menuProductResponses = createMenuProductResponses(savedMenu, menuProductCreateRequests);

        return MenuResponse.of(savedMenu, menuProductResponses);
    }

    private void validateMenuPrice(MenuPrice menuPrice, List<MenuProductCreateRequest> menuProductCreateRequests) {
        BigDecimal menuProductPriceSum = BigDecimal.ZERO;

        for (MenuProductCreateRequest menuProductCreateRequest : menuProductCreateRequests) {
            Long productId = menuProductCreateRequest.getProductId();
            Product product =
                    productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
            ProductPrice productPrice = product.getProductPrice();
            BigDecimal productQuantity = BigDecimal.valueOf(menuProductCreateRequest.getQuantity());
            menuProductPriceSum = menuProductPriceSum.add(productPrice.multiply(productQuantity));
        }

        if (menuPrice.isBiggerThan(menuProductPriceSum)) {
            throw new InvalidMenuPriceException("메뉴 가격은 구성된 상품 가격의 총합을 초과할 수 없습니다!");
        }
    }

    private List<MenuProductResponse> createMenuProductResponses(Menu menu, List<MenuProductCreateRequest> menuProductCreateRequests) {
        List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (MenuProductCreateRequest menuProductCreateRequest : menuProductCreateRequests) {
            Long productId = menuProductCreateRequest.getProductId();
            Product product =
                    productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
            MenuProduct menuProduct = new MenuProduct(menu, product, menuProductCreateRequest.getQuantity());
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }

        return savedMenuProducts.stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());
    }

    public List<MenuResponse> listAllMenus() {
        List<Menu> menus = menuRepository.findAll();

        List<MenuResponse> menuResponses = new ArrayList<>();
        for (Menu menu : menus) {
            List<MenuProductResponse> menuProductResponses =
                    menuProductRepository.findAllByMenuId(menu.getId()).stream()
                            .map(MenuProductResponse::from)
                            .collect(Collectors.toList());
            MenuResponse menuResponse = MenuResponse.of(menu, menuProductResponses);
            menuResponses.add(menuResponse);
        }

        return menuResponses;
    }
}
