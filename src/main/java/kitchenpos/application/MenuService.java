package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
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
        final Menu menu = new Menu(request.getName(), price.getAmount(), menuGroup, menuProducts);
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
        return request.getMenuProductCreateRequests()
                .stream()
                .map(it -> {
                    final Product product = productRepository.findById(it.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new MenuProduct(null, product, it.getQuantity());
                })
                .collect(Collectors.toList());
    }

    private MenuResponse generateMenuResponse(final Menu it) {
        return new MenuResponse(
                it.getId(),
                it.getName(),
                it.getPrice(),
                new MenuGroupResponse(it.getMenuGroup().getId(), it.getMenuGroup().getName()),
                generateMenuProductResponses(it)
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
                                it.getProduct().getPrice()),
                        it.getQuantity()))
                .collect(Collectors.toList());
    }
}
