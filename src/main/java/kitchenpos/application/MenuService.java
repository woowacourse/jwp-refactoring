package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import kitchenpos.ui.dto.MenuProductResponse;
import kitchenpos.ui.dto.MenuResponse;
import kitchenpos.ui.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final Price price = new Price(request.getPrice());
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        final List<MenuProduct> menuProducts = createMenuProducts(request);
        final Menu menu = new Menu(request.getName(), price, menuGroup, menuProducts);
        menuRepository.save(menu);

        return generateMenuResponse(menu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(this::generateMenuResponse)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> createMenuProducts(final MenuCreateRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(it -> {
                    final Product product = productRepository.findById(it.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new MenuProduct(product, it.getQuantity());
                })
                .collect(Collectors.toList());
    }

    private MenuResponse generateMenuResponse(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice().getAmount(),
                new MenuGroupResponse(menu.getMenuGroup().getId(), menu.getMenuGroup().getName()),
                generateMenuProductResponses(menu)
        );
    }

    private List<MenuProductResponse> generateMenuProductResponses(final Menu savedMenu) {
        return savedMenu.getMenuProducts()
                .stream()
                .map(it -> new MenuProductResponse(
                        it.getSeq(),
                        new ProductResponse(
                                it.getProduct().getId(),
                                it.getProduct().getName(),
                                it.getProduct().getPrice().getAmount()),
                        it.getQuantity()))
                .collect(Collectors.toList());
    }
}
