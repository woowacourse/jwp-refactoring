package kitchenpos.menu.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.vo.Money;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.CreateMenuRequest;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.MenuPriceIsBiggerThanActualPriceException;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
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
        Menu menu = saveMenu(request);
        menuRepository.save(menu);
        setupMenuProducts(request, menu);
        List<MenuProduct> menuProducts = menuProductRepository.findAllByMenu(menu);

        return MenuResponse.from(menu, menuProducts);
    }

    private Menu saveMenu(CreateMenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(MenuGroupNotFoundException::new);
        return new Menu(menuGroup.getId(), request.getName(), request.getPrice());
    }

    private void setupMenuProducts(CreateMenuRequest request, Menu menu) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            MenuProduct menuProduct = saveMenuProduct(menu, menuProductRequest);
            menuProducts.add(menuProduct);
        }
        validateMenuPriceIsNotBiggerThanActualPrice(menu, menuProducts);
    }

    private MenuProduct saveMenuProduct(Menu menu, MenuProductRequest menuProductRequest) {
        Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        MenuProduct menuProduct = new MenuProduct(product.getId(), menu, menuProductRequest.getQuantity());

        return menuProductRepository.save(menuProduct);
    }

    private void validateMenuPriceIsNotBiggerThanActualPrice(Menu menu, List<MenuProduct> menuProducts) {
        Money actualPrice = calculateActualPrice(menuProducts);

        if (menu.hasPriceGreaterThan(actualPrice)) {
            throw new MenuPriceIsBiggerThanActualPriceException();
        }
    }

    private Money calculateActualPrice(List<MenuProduct> menuProducts) {
        Money actualPrice = Money.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            BigDecimal menuProductPrice = calculateMenuProductPrice(menuProduct);
            actualPrice = actualPrice.add(new Money(menuProductPrice));
        }
        return actualPrice;
    }

    private BigDecimal calculateMenuProductPrice(MenuProduct menuProduct) {
        Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        return product.getPrice()
                .multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
    }

    public List<MenuResponse> findAll() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(each -> MenuResponse.from(each, menuProductRepository.findAllByMenu(each)))
                .collect(Collectors.toList());
    }
}
