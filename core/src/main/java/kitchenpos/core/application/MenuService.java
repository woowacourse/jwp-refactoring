package kitchenpos.core.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.core.domain.menu.Menu;
import kitchenpos.core.domain.menu.MenuProduct;
import kitchenpos.core.domain.menu.repository.MenuGroupRepository;
import kitchenpos.core.domain.menu.repository.MenuRepository;
import kitchenpos.core.domain.product.Product;
import kitchenpos.core.domain.product.repository.ProductRepository;
import kitchenpos.core.dto.request.MenuCreateRequest;
import kitchenpos.core.dto.request.MenuProductCreateRequest;
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

    public Menu create(final MenuCreateRequest request) {
        final Long menuGroupId = request.getMenuGroupId();
        validateMenuGroup(menuGroupId);
        final Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                menuGroupId,
                createMenuProducts(request.getMenuProducts())
        );
        return menuRepository.save(menu);
    }

    private void validateMenuGroup(final Long menuGroupId) {
        if (Objects.isNull(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹 아이디가 존재하지 않습니다.");
        }
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("등록되지 않은 메뉴 그룹으로 메뉴를 생성할 수 없습니다.");
        }
    }

    private List<MenuProduct> createMenuProducts(final List<MenuProductCreateRequest> menuProducts) {
        return menuProducts.stream()
                .map(request -> new MenuProduct(findProduct(request.getProductId()), request.getQuantity()))
                .collect(Collectors.toList());
    }

    private Product findProduct(final Long productId) {
        if (Objects.isNull(productId)) {
            throw new IllegalArgumentException("메뉴로 등록할 상품 아이디가 존재하지 않습니다.");
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품으로 메뉴를 생성할 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
