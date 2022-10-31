package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
    public MenuResponse create(final MenuRequest request) {
        final BigDecimal price = request.getPrice();

        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(MenuProductRequest::toEntity)
                .collect(Collectors.toList());

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        Menu menu = request.toEntity();
        final Menu savedMenu = menuRepository.save(menu);

        return new MenuResponse(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }
}
