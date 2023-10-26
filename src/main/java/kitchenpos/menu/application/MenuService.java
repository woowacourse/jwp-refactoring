package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.presentation.dto.request.CreateMenuRequest;
import kitchenpos.menu.presentation.dto.request.MenuProductRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
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

    public Menu create(final CreateMenuRequest request) {
        final Price price = Price.from(request.getPrice());
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                                                       .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."));
        final Menu menu = new Menu(request.getName(),
                                   price,
                                   menuGroup,
                                   new ArrayList<>());
        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                                                      .map(menuProductRequest -> convertToMenuProduct(menuProductRequest, menu))
                                                      .collect(Collectors.toList());
        menu.addMenuProducts(menuProducts);
        return menuRepository.save(menu);
    }

    private MenuProduct convertToMenuProduct(final MenuProductRequest request, final Menu menu) {
        final Product product = productRepository.findById(request.getProductId())
                                                 .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        return new MenuProduct(request.getQuantity(),
                               menu,
                               product);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
