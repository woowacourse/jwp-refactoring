package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.request.MenuCreateRequest;
import kitchenpos.dto.menu.request.MenuProductCreateRequest;
import kitchenpos.dto.menu.response.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
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
        final List<MenuProduct> menuProducts = createMenuProducts(request.menuProducts());

        final BigDecimal totalPrice = calculateTotalPrice(menuProducts);
        if (menu.price().compareTo(totalPrice) != 0) {
            throw new IllegalArgumentException("[ERROR] 가격이 잘못되었습니다.");
        }

        final Menu savedMenu = menuRepository.save(menu);
        menuProducts.forEach(menuProduct -> {
            menuProduct.setMenu(savedMenu);
            menuProductRepository.save(menuProduct);
        });

        return savedMenu.id();
    }

    private List<MenuProduct> createMenuProducts(final List<MenuProductCreateRequest> menuProductCreateRequests) {
        final List<Long> productIds = parseProcess(menuProductCreateRequests, MenuProductCreateRequest::productId);
        final List<Product> products = productRepository.findAllById(productIds);

        if (productIds.size() != products.size()) {
            throw new IllegalArgumentException("[ERROR] 없는 상품이 존재합니다.");
        }

        final List<Long> quantities = parseProcess(menuProductCreateRequests, MenuProductCreateRequest::quantity);

        return IntStream.range(0, products.size())
                .mapToObj(index -> new MenuProduct(products.get(index), quantities.get(index)))
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
