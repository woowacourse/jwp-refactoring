package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.MenuProductDto;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;

    public MenuService(MenuGroupRepository menuGroupRepository, ProductRepository productRepository,
                       MenuRepository menuRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu create(MenuCreateRequest request) {
        MenuProducts menuProducts = getMenuProducts(request.getMenuProducts());
        Menu menu = getMenu(request, menuProducts);

        return menuRepository.save(menu);
    }

    private Menu getMenu(MenuCreateRequest request, MenuProducts menuProducts) {
        return new Menu(
                request.getName(),
                new Price(request.getPrice()),
                getMenuGroup(request.getMenuGroupId()),
                menuProducts
        );
    }

    private MenuGroup getMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(NotFoundMenuGroupException::new);
    }

    public MenuProducts getMenuProducts(List<MenuProductDto> menuProductDtos) {
        return new MenuProducts(menuProductDtos.stream()
                .map(dto -> new MenuProduct(getProduct(dto.getProductId()), dto.getQuantity()))
                .collect(Collectors.toList()));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(NotFoundProductException::new);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
