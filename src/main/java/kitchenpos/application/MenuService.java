package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductCreateRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
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
        final Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                findMenuGroup(request.getMenuGroupId()),
                createMenuProducts(request.getMenuProducts())
        );
        return menuRepository.save(menu);
    }

    private MenuGroup findMenuGroup(final Long menuGroupId) {
        if (Objects.isNull(menuGroupId)) {
            throw new IllegalArgumentException("등록되지 않은 메뉴 그룹으로 메뉴를 생성할 수 없습니다.");
        }
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴 그룹으로 메뉴를 생성할 수 없습니다."));
    }

    private List<MenuProduct> createMenuProducts(final List<MenuProductCreateRequest> menuProducts) {
        return menuProducts.stream()
                .map(request -> new MenuProduct(findProduct(request.getProductId()), request.getQuantity()))
                .collect(Collectors.toList());
    }

    private Product findProduct(final Long productId) {
        if (Objects.isNull(productId)) {
            throw new IllegalArgumentException("등록되지 않은 상품으로 메뉴를 생성할 수 없습니다.");
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품으로 메뉴를 생성할 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
