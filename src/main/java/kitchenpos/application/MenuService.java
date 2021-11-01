package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.request.MenuRequest.MenuProductRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("존재하지 않는 메뉴 그룹입니다. (id: %d)", menuRequest.getMenuGroupId())
            ));
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);

        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            Long productId = menuProductRequest.getProductId();
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                    String.format("존재하지 않는 제품이 있습니다.(id: %d)", productId)
                ));
            menu.addProduct(product, menuProductRequest.getQuantity());
        }

        Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
