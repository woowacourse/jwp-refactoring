package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("포함된 메뉴 그룹이 있어야 합니다."));
        validateMenuProductNotNull(request);

        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts);
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> findAll() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private void validateMenuProductNotNull(final MenuRequest request) {
        if (CollectionUtils.isEmpty(request.getMenuProducts())) {
            throw new IllegalArgumentException("메뉴 상품들은 null일 수 없습니다.");
        }
    }

    private MenuProduct toMenuProduct(final MenuProductRequest request) {
        final Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("없는 상품은 메뉴에 추가할 수 없습니다."));
        return new MenuProduct(product, request.getQuantity());
    }
}
