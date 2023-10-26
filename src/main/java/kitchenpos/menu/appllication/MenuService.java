package kitchenpos.menu.appllication;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menugroup.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
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
    public Long create(final MenuCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.menuGroupId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "[ERROR] MenuGroup이 존재하지 않습니다. id : " + request.menuGroupId()
                ));
        final Menu menu = new Menu(
                request.name(),
                request.price(),
                menuGroup
        );
        final Menu savedMenu = menuRepository.save(menu);
        final List<MenuProduct> menuProducts = createMenuProducts(request.menuProducts(), savedMenu);

        final BigDecimal totalPrice = calculateTotalPrice(menuProducts);
        if (savedMenu.price().compareTo(totalPrice) != 0) {
            throw new IllegalArgumentException("[ERROR] 가격이 잘못되었습니다.");
        }

        return savedMenu.id();
    }

    private List<MenuProduct> createMenuProducts(
            final List<MenuProductCreateRequest> menuProductCreateRequests,
            final Menu menu
    ) {
        final List<Long> productIds = parseProcess(menuProductCreateRequests, MenuProductCreateRequest::productId);
        final List<Product> products = productRepository.findAllById(productIds);

        if (productIds.size() != products.size()) {
            throw new IllegalArgumentException("[ERROR] 없는 상품이 존재합니다.");
        }

        final List<Long> quantities = parseProcess(menuProductCreateRequests, MenuProductCreateRequest::quantity);

        return IntStream.range(0, products.size())
                .mapToObj(index -> new MenuProduct(products.get(index), quantities.get(index)))
                .map(menuProduct -> {
                    menuProduct.setMenu(menu);
                    return menuProductRepository.save(menuProduct);
                })
                .collect(Collectors.toUnmodifiableList());
    }

    private BigDecimal calculateTotalPrice(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private <R, T> List<T> parseProcess(
            List<R> requests,
            Function<R, T> processor
    ) {
        return requests.stream()
                .map(processor)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(menu -> {
                    final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.id());
                    return MenuResponse.of(menu, menuProducts);
                })
                .collect(Collectors.toUnmodifiableList());
    }
}
