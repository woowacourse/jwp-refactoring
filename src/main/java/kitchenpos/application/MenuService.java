package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.ProductRepository;
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
    public Menu create(final MenuRequest menuRequest) {
        if (menuRequest.getMenuGroupId() == null) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }

        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        final List<MenuProduct> menuProducts = menuRequest.getMenuProductsRequest().stream()
                .map(request -> request.toMenuProduct(productRepository.findById(request.getProductId())
                        .orElseThrow(IllegalArgumentException::new)))
                .collect(Collectors.toUnmodifiableList());
        final Menu menu = menuRequest.toMenu(menuGroup, menuProducts);

        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
