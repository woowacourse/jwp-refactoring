package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.menu.MenuCreateRequest;
import kitchenpos.application.dto.menu.MenuCreateResponse;
import kitchenpos.application.dto.menu.MenuProductCreateRequest;
import kitchenpos.application.dto.menu.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        final List<MenuProductCreateRequest> menuProductRequests = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductCreateRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            final MenuProduct menuProduct = new MenuProduct(product, menuProductRequest.getQuantity());
            menuProducts.add(menuProduct);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu menu = new Menu(request.getName(), price, menuGroup, menuProducts);
        final Menu savedMenu = menuRepository.save(menu);

        return MenuCreateResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(toList());
    }
}
