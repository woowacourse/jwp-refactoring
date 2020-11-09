package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuProductResponse;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menu.ProductQuantityRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@Service
public class MenuService {

    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            final ProductRepository productRepository,
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository
    ) {
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = makeMenuProductsWithoutMenu(menuRequest.getProductQuantities());

        validatePrice(menuProducts, menuRequest.getPrice());

        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        final Menu savedMenu = menuRepository.save(menuRequest.toEntity(menuGroup));

        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.updateMenu(savedMenu);
            menuProductRepository.save(menuProduct);
        }

        return MenuResponse.of(savedMenu, MenuProductResponse.ofList(menuProducts));
    }

    private List<MenuProduct> makeMenuProductsWithoutMenu(final List<ProductQuantityRequest> productQuantities) {
        return productQuantities.stream()
                .map(productQuantityRequest -> {
                    final Product product = productRepository.findById(productQuantityRequest.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new MenuProduct(null, product, productQuantityRequest.getQuantity());
                }).collect(Collectors.toList());
    }

    private void validatePrice(final List<MenuProduct> menuProducts, final BigDecimal price) {
        BigDecimal sum = BigDecimal.ZERO;

        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(menu -> {
                    List<MenuProduct> menuProducts = menuProductRepository.findAllByMenu(menu);
                    return MenuResponse.of(menu, MenuProductResponse.ofList(menuProducts));
                }).collect(Collectors.toList());
    }
}
