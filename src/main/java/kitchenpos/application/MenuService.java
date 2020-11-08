package kitchenpos.application;

import kitchenpos.application.exceptions.NotExistedMenuGroupException;
import kitchenpos.application.exceptions.NotExistedProductException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.menu.MenuProductRequest;
import kitchenpos.ui.dto.menu.MenuRequest;
import kitchenpos.ui.dto.menu.MenuResponse;
import kitchenpos.ui.dto.menu.MenuResponses;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
    public MenuResponse create(final MenuRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(NotExistedMenuGroupException::new);

        final List<MenuProductRequest> menuProductsRequest = request.getMenuProducts();
        final BigDecimal sum = calculateProductsPrice(menuProductsRequest);
        final Menu savedMenu = menuRepository.save(request.toEntity(sum, menuGroup));

        return MenuResponse.of(savedMenu, findMenuProducts(menuProductsRequest, savedMenu));
    }

    private List<MenuProduct> findMenuProducts(final List<MenuProductRequest> menuProductsRequest, final Menu savedMenu) {
        return menuProductsRequest.stream()
                .map(m -> {
                    Product product = productRepository.findById(m.getProductId()).orElseThrow(NotExistedProductException::new);
                    return menuProductRepository.save(new MenuProduct(m.getSeq(), savedMenu, product, m.getQuantity()));
                })
                .collect(Collectors.toList());
    }

    private BigDecimal calculateProductsPrice(List<MenuProductRequest> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(NotExistedProductException::new);
            sum = sum.add(product.getTotalPrice(menuProduct.getQuantity()));
        }
        return sum;
    }

    public MenuResponses list() {
        final List<Menu> menus = menuRepository.findAll();
        final List<MenuResponse> menuResponses = menus.stream()
                .map(m -> MenuResponse.of(m, menuProductRepository.findAllBy(m.getId())))
                .collect(Collectors.toList());

        return MenuResponses.from(menuResponses);
    }
}
