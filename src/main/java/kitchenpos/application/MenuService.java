package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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
    public Menu create(final Menu menu) {
        validateMenuGroup(menu);
        validateMenuProducts(menu.getMenuProducts());
        return menuRepository.save(menu);
    }

    private void validateMenuGroup(final Menu menu) {
        final Long menuGroupId = menu.getMenuGroupId();
        if (Objects.nonNull(menuGroupId) && menuGroupRepository.existsById(menuGroupId)) {
            return;
        }
        throw new IllegalArgumentException("등록되지 않은 메뉴 그룹으로 메뉴를 생성할 수 없습니다.");
    }

    private void validateMenuProducts(List<MenuProduct> menuProducts) {
        final List<Long> productIds = extractProductIds(menuProducts);
        if (productIds.size() != productRepository.findAllByIdIn(productIds).size()) {
            throw new IllegalArgumentException("유효하지 않은 상품 정보로 메뉴를 생성할 수 없습니다.");
        }
    }

    private List<Long> extractProductIds(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
