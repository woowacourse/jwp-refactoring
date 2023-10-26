package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuDto;
import kitchenpos.application.dto.ReadMenuDto;
import kitchenpos.application.exception.MenuGroupNotFoundException;
import kitchenpos.application.exception.ProductNotFoundException;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
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
        final MenuProducts menuProducts = convertMenuProducts(request);
        final Menu menu = new Menu(request.getName(), request.getPrice(),  menuGroup.getId(), menuProducts);
        final Menu persistMenu = menuRepository.save(menu);

        return new CreateMenuDto(persistMenu);
    }

    private MenuProducts convertMenuProducts(final CreateMenuRequest menuRequest) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        Price totalMenuProductPrice = Price.ZERO;

        for (final CreateMenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                                                     .orElseThrow(ProductNotFoundException::new);

            totalMenuProductPrice = totalMenuProductPrice.plus(product.price().times(menuProductRequest.getQuantity()));

            final MenuProduct menuProduct = new MenuProduct(
                    product.getId(),
                    menuProductRequest.getQuantity()
            );

            menuProducts.add(menuProduct);
        }
        final Price menuPrice = new Price(menuRequest.getPrice());

        return MenuProducts.of(totalMenuProductPrice, menuPrice, menuProducts);
    }

    public List<ReadMenuDto> list() {
        return menuRepository.findAll()
                             .stream()
                             .map(ReadMenuDto::new)
                             .collect(Collectors.toList());
    }
}
