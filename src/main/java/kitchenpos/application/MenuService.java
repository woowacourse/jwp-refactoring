package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.price.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.productquantity.ProductQuantities;
import kitchenpos.domain.productquantity.ProductQuantity;
import kitchenpos.domain.Quantity;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuProductResponse;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.exception.InvalidRequestParamException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuProductRepository menuProductRepository,
        final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final ProductQuantities productQuantities = getProductQuantities(menuRequest.getMenuProducts());
        final Price menuPrice = new Price(menuRequest.getPrice());
        productQuantities.validateTotalPriceIsGreaterOrEqualThan(menuPrice);

        final MenuGroup foundMenuGroup = findMenuGroupById(menuRequest.getMenuGroupId());
        final Menu menu = convertRequestToEntity(menuRequest, foundMenuGroup);
        menuRepository.save(menu);

        final List<MenuProduct> menuProducts = createMenuProducts(menu, productQuantities);
        menuProductRepository.saveAll(menuProducts);

        return createMenuResponse(menu, menuProducts);
    }

    private ProductQuantities getProductQuantities(List<MenuProductRequest> menuProductRequests) {
        final ProductQuantities productQuantities = new ProductQuantities();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product foundProduct = findProductById(menuProductRequest.getProductId());
            final Quantity quantity = new Quantity(menuProductRequest.getQuantity());
            productQuantities.add(new ProductQuantity(foundProduct, quantity));
        }
        return productQuantities;
    }

    private MenuGroup findMenuGroupById(Long id) {
        return menuGroupRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 id의 MenuGroup이 존재하지 않습니다."));
    }

    private Menu convertRequestToEntity(MenuRequest menuRequest, MenuGroup menuGroup) {
        try {
            return new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestParamException(e.getMessage());
        }
    }

    private List<MenuProduct> createMenuProducts(Menu menu, ProductQuantities productQuantities) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (ProductQuantity productQuantity : productQuantities.getProductQuantities()) {
            menuProducts.add(new MenuProduct(menu, productQuantity.getProduct(), productQuantity.getQuantity()));
        }
        return menuProducts;
    }

    private MenuResponse createMenuResponse(Menu menu, List<MenuProduct> newMenuProducts) {
        final List<MenuProductResponse> menuProductResponses = createMenuProductResponses(newMenuProducts);
        return new MenuResponse(menu, menuProductResponses);
    }

    private List<MenuProductResponse> createMenuProductResponses(List<MenuProduct> newMenuProducts) {
        return newMenuProducts.stream()
            .map(MenuProductResponse::new)
            .collect(Collectors.toList())
            ;
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 id의 MenuProduct가 존재하지 않습니다."));
    }

    public List<MenuResponse> findAll() {
        final List<Menu> foundAllMenus = menuRepository.findAll();
        final List<MenuResponse> menuResponses = new ArrayList<>();
        for (Menu foundMenu : foundAllMenus) {
            final List<MenuProduct> foundMenuProducts = menuProductRepository.findAllByMenu(foundMenu);
            final List<MenuProductResponse> menuProductResponses = createMenuProductResponses(foundMenuProducts);
            menuResponses.add(new MenuResponse(foundMenu, menuProductResponses));
        }
        return menuResponses;
    }
}
