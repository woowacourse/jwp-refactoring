package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.CreateMenuRequest;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
        MenuRepository menuRepository,
        MenuGroupRepository menuGroupRepository,
        ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final CreateMenuRequest request) {

        final MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());

        final Map<Product, Long> menuProducts = request.getMenuProducts().stream()
            .collect(Collectors.toMap(
                menuProduct -> findProductById(menuProduct.getProductId()),
                menuProduct -> menuProduct.getQuantity())
            );

        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts);
        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    private MenuGroup findMenuGroupById(Long id) {
        return menuGroupRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다."));
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품입니다."));
    }
}
