package kitchenpos.menu.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponses;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Service
@Transactional(readOnly = true)
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
    public Long create(final MenuCreateRequest request) {
        MenuGroup menuGroup = findMenuGroup(request.getMenuGroupId());
        Menu menu = request.toEntity(menuGroup);

        final Menu savedMenu = menuRepository.save(menu);

        MenuProducts menuProducts = request.getMenuProductRequest()
            .stream()
            .map(req -> new MenuProduct(null, menu, findProduct(req.getProductId()), req.getQuantity()))
            .collect(collectingAndThen(toList(), list -> new MenuProducts(list, request.getPrice())));

        List<MenuProduct> savedMenuProducts = menuProducts.stream()
            .map(menuProductRepository::save)
            .collect(toList());

        savedMenu.changeMenuProducts(savedMenuProducts);

        return savedMenu.getId();
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(IllegalArgumentException::new);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(IllegalArgumentException::new);
    }

    public MenuResponses list() {
        List<Menu> menus = menuRepository.findAllWithMenuGroup();
        return MenuResponses.from(menus);
    }
}
