package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(MenuCreateRequest request) {
        Menu menu = Menu.of(
                request.getName(),
                request.getPrice(),
                findMenuGroup(request.getMenuGroupId())
        );

        List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList());

        menu.addAllMenuProducts(menuProducts);

        return menuRepository.save(menu);
    }

    private MenuProduct createMenuProduct(MenuProductCreateRequest menuProductCreateRequest) {
        return MenuProduct.of(
                findProduct(menuProductCreateRequest.getProductId()),
                menuProductCreateRequest.getQuantity()
        );
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        validateMenuGroupId(menuGroupId);

        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateMenuGroupId(Long menuGroupId) {
        if (Objects.isNull(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹의 ID 는 존재하지 않을 수 없습니다.");
        }
    }

    private Product findProduct(Long productId) {
        validateProductId(productId);

        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateProductId(Long productId) {
        if (Objects.isNull(productId)) {
            throw new IllegalArgumentException("상품의 ID 는 존재하지 않을 수 없습니다.");
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

}
