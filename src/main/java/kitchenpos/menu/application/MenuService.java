package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final MenuGroup findMenuGroup = menuGroupRepository.findMenuGroupById(request.getMenuGroupId());
        final Menu newMenu = Menu.withEmptyMenuProducts(
                new Name(request.getName()),
                new Price(request.getPrice()),
                findMenuGroup
        );
        newMenu.addMenuProducts(createMenuProducts(request));
        final Menu savedMenu = menuRepository.save(newMenu);

        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> createMenuProducts(final MenuCreateRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(menuProductCreateRequest -> {
                    final Product findProduct = productRepository.findProductById(menuProductCreateRequest.getProductId());
                    return MenuProduct.withoutMenu(findProduct, new Quantity(menuProductCreateRequest.getQuantity()));
                }).collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.joinMenuGroupAll());
    }
}
