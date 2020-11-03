package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.ProductQuantityRequest;
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
        List<ProductQuantityRequest> productQuantities = menuRequest.getProductQuantities();

        BigDecimal sum = BigDecimal.ZERO;
        for (final ProductQuantityRequest productQuantityRequest : productQuantities) {
            final Product product = productRepository.findById(productQuantityRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(productQuantityRequest.getQuantity())));
        }

        final BigDecimal price = menuRequest.getPrice();

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        final Menu savedMenu = menuRepository.save(menuRequest.toEntity(menuGroup));

        final List<MenuProductResponse> menuProductResponses = new ArrayList<>();

        for (final ProductQuantityRequest productQuantityRequest : productQuantities) {
            final Product product = productRepository.findById(productQuantityRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            MenuProduct menuProduct = menuProductRepository.save(new MenuProduct(savedMenu, product, productQuantityRequest.getQuantity()));
            menuProductResponses.add(MenuProductResponse.of(menuProduct));
        }

        return MenuResponse.of(savedMenu, menuProductResponses);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        final List<MenuResponse> menuResponses = new ArrayList<>();

        for (final Menu menu : menus) {
            List<MenuProduct> menuProducts = menuProductRepository.findAllByMenu(menu);
            menuResponses.add(
                    MenuResponse.of(menu, MenuProductResponse.ofList(menuProducts))
            );
        }

        return menuResponses;
    }
}
