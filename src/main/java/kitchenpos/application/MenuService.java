package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.dto.menu.MenuCreateRequest;
import kitchenpos.application.dto.menu.MenuCreateResponse;
import kitchenpos.application.dto.menu.MenuProductCreateRequest;
import kitchenpos.application.dto.menu.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Price;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuCreateResponse create(final MenuCreateRequest request) {
        final Price price = new Price(request.getPrice());
        final MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());

        final MenuProducts menuProducts = makeMenuProducts(request.getMenuProducts());
        validateMenuProductsPrice(price, menuProducts);

        final Menu menu = new Menu(null, request.getName(), price, menuGroup, menuProducts);
        final Menu savedMenu = menuRepository.save(menu);

        return MenuCreateResponse.of(savedMenu);
    }

    private MenuGroup findMenuGroupById(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다. id = " + menuGroupId));
    }

    private MenuProducts makeMenuProducts(final List<MenuProductCreateRequest> request) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductCreateRequest menuProductRequest : request) {
            final Product product = findProductById(menuProductRequest.getProductId());
            final MenuProduct menuProduct = new MenuProduct(product, menuProductRequest.getQuantity());
            menuProducts.add(menuProduct);
        }
        return new MenuProducts(menuProducts);
    }

    private Product findProductById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. id = " + productId));
    }

    private void validateMenuProductsPrice(final Price price, final MenuProducts menuProducts) {
        final BigDecimal sum = menuProducts.calculateMenuPrice();
        if (price.isGreaterThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격이 메뉴 상품들의 가격의 합보다 클 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(toList());
    }
}
