package kitchenpos.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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

    public MenuResponse create(final MenuRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
            .orElseThrow(NoSuchElementException::new);

        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
            .map(menuProduct -> new MenuProduct(
                    productRepository.findById(menuProduct.getProductId()).orElseThrow(NoSuchElementException::new),
                    menuProduct.getQuantity()
                )
            )
            .collect(Collectors.toList());

        final Menu savedMenu = menuRepository.save(
            new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts)
        );
        menuProductRepository.saveAll(menuProducts);

        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }
}
