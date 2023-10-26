package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.util.BigDecimalUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuGroupValidator menuGroupValidator;
    private final ProductTotalPriceValidator productTotalPriceValidator;

    public MenuService(final MenuRepository menuRepository,
                       final MenuProductRepository menuProductRepository,
                       final MenuGroupValidator menuGroupValidator,
                       final ProductTotalPriceValidator productTotalPriceValidator) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupValidator = menuGroupValidator;
        this.productTotalPriceValidator = productTotalPriceValidator;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        final Menu menu = createMenuByRequest(menuCreateRequest);
        final Menu savedMenu = menuRepository.save(menu);
        final List<MenuProduct> savedMenuProducts = saveAllMenuProducts(menu.getMenuProducts(), savedMenu.getId());
        savedMenu.addMenuProducts(savedMenuProducts);
        return MenuResponse.from(savedMenu);
    }

    private Menu createMenuByRequest(final MenuCreateRequest request) {
        final String name = request.getName();
        final BigDecimal price = request.getPrice();
        final Long menuGroupId = request.getMenuGroupId();
        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(productRequest -> new MenuProduct(productRequest.getProductId(), productRequest.getQuantity()))
                .collect(Collectors.toList());

        validateMenuGroupExist(request.getMenuGroupId());
        validateMenuPriceNotBiggerThanMenuProductsTotalPrice(price, menuProducts);

        return Menu.builder(name, price, menuGroupId)
                .menuProducts(menuProducts)
                .build();
    }

    private void validateMenuGroupExist(final Long menuGroupId) {
        menuGroupValidator.validateMenuGroupExist(menuGroupId);
    }

    private void validateMenuPriceNotBiggerThanMenuProductsTotalPrice(final BigDecimal price,
                                                                      final List<MenuProduct> menuProducts) {
        final BigDecimal menuProductsTotalPrice = getMenuProductsTotalPrice(menuProducts);
        BigDecimalUtil.valueForCompare(price)
                .throwIfBiggerThan(menuProductsTotalPrice, IllegalArgumentException::new);
    }

    private BigDecimal getMenuProductsTotalPrice(final List<MenuProduct> menuProducts) {
        final List<BigDecimal> menuProductTotalPrices = menuProducts.stream()
                .map(this::getProductTotalPrice)
                .collect(Collectors.toList());

        return BigDecimalUtil.sum(menuProductTotalPrices);
    }

    private BigDecimal getProductTotalPrice(final MenuProduct menuProduct) {
        return productTotalPriceValidator.getTotalPriceThrowIfNotExist(
                menuProduct.getProductId(),
                BigDecimal.valueOf(menuProduct.getQuantity())
        );
    }

    private List<MenuProduct> saveAllMenuProducts(final List<MenuProduct> menuProducts, final Long menuId) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.belongToMenu(menuId);
            final MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);
            savedMenuProducts.add(savedMenuProduct);
        }
        return savedMenuProducts;
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.getId());
            menu.addMenuProducts(menuProducts);
        }

        return MenuResponse.listOf(menus);
    }
}
