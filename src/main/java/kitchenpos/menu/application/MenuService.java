package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuRequestDto;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
    public MenuResponse create(final MenuRequestDto menuRequestDto) {

        if (!menuGroupRepository.existsById(menuRequestDto.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menuRequestDto.getMenuProducts();

        Price.convertToMenuPrice(menuRequestDto.getPrice(), findProducts(menuProducts), getQuantities(menuProducts));
        final Menu savedMenu = menuRepository.save(new Menu(menuRequestDto.getName(), menuRequestDto.getPrice(), menuRequestDto.getMenuGroupId()));

        return new MenuResponse(savedMenu, setMenuProductToMenu(menuProducts,savedMenu.getId()));
    }

    private List<BigDecimal> findProducts(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                        .map(menuProduct -> findProduct(menuProduct.getProductId()).getPrice())
                        .collect(Collectors.toList());
    }

    private List<Long> getQuantities(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                        .map(MenuProduct::getQuantity)
                        .collect(Collectors.toList());
    }

    private List<MenuProduct> setMenuProductToMenu(final List<MenuProduct> menuProducts, Long menuId) {
        return menuProducts.stream()
                .map(menuProduct -> new MenuProduct(menuId, menuProduct.getProductId(), menuProduct.getQuantity()))
                .map(menuProductRepository::save)
                .collect(Collectors.toList());
    }

    private Product findProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(it -> new MenuResponse(it, menuProductRepository.findAllByMenuId(it.getId())))
                .collect(Collectors.toList());
    }
}
