package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuDto;
import kitchenpos.application.dto.ReadMenuDto;
import kitchenpos.application.exception.MenuGroupNotFoundException;
import kitchenpos.application.exception.ProductNotFoundException;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.menugroup.repository.MenuGroupRepository;
import kitchenpos.domain.product.repository.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.request.CreateMenuProductRequest;
import kitchenpos.ui.dto.request.CreateMenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public CreateMenuDto create(final CreateMenuRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                                                       .orElseThrow(MenuGroupNotFoundException::new);
        final List<MenuProduct> menuProducts = findMenuProducts(request);
        final Menu menu = Menu.of(request.getName(), request.getPrice(), menuProducts, menuGroup.getId());
        final Menu persistMenu = menuRepository.save(menu);

        return new CreateMenuDto(persistMenu);
    }

    private List<MenuProduct> findMenuProducts(final CreateMenuRequest menuRequest) {
        final List<MenuProduct> menuProducts = new ArrayList<>();

        for (final CreateMenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                                                     .orElseThrow(ProductNotFoundException::new);

            final MenuProduct menuProduct = new MenuProduct(
                    product.getId(),
                    product.price(),
                    product.name(),
                    menuProductRequest.getQuantity()
            );
            menuProducts.add(menuProduct);
        }

        return menuProducts;
    }

    public List<ReadMenuDto> list() {
        return menuRepository.findAll()
                             .stream()
                             .map(ReadMenuDto::new)
                             .collect(Collectors.toList());
    }
}
