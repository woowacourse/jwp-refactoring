package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuCreateRequest.MenuProductRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuName;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.vo.Price;
import kitchenpos.repositroy.MenuGroupRepository;
import kitchenpos.repositroy.MenuRepository;
import kitchenpos.repositroy.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
        final MenuName menuName = new MenuName(request.getName());
        final Price menuPrice = new Price(request.getPrice());
        final MenuGroup menuGroup = menuGroupRepository.getById(request.getMenuGroupId());
        final MenuProducts menuProducts = new MenuProducts(request.getMenuProducts()
                .stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toUnmodifiableList())
        );

        return MenuResponse.from(menuRepository.save(new Menu(menuName, menuPrice, menuProducts, menuGroup)));
    }

    private MenuProduct createMenuProduct(final MenuProductRequest request) {
        final Product product = productRepository.getById(request.getProductId());
        return new MenuProduct(product, request.getQuantity());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAllByFetch()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
