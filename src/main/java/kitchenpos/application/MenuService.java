package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.exception.MenuPriceException;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
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
    private final MenuProductRepository menuProductRepository;

    public MenuService(MenuGroupRepository menuGroupRepository, ProductRepository productRepository,
                       MenuRepository menuRepository, MenuProductRepository menuProductRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Menu create(MenuCreateRequest request) {
        Menu savedMenu = saveMenu(request);

        MenuProducts menuProducts = getMenuProducts(request.getMenuProducts(), savedMenu);
        validatePrice(menuProducts, new Price(request.getPrice()));
        saveMenuProducts(menuProducts);

        return savedMenu;
    }

    private Menu saveMenu(MenuCreateRequest request) {
        return menuRepository.save(new Menu(
                request.getName(),
                request.getPrice(),
                getMenuGroup(request.getMenuGroupId())
        ));
    }

    private MenuGroup getMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(NotFoundMenuGroupException::new);
    }

    public MenuProducts getMenuProducts(List<MenuProductDto> menuProductDtos, Menu menu) {
        return new MenuProducts(menuProductDtos.stream()
                .map(dto -> new MenuProduct(menu, getProduct(dto.getProductId()), dto.getQuantity()))
                .collect(Collectors.toList()));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(NotFoundProductException::new);
    }

    private void validatePrice(MenuProducts menuProducts, Price price) {
        if (price.isExpensiveThan(menuProducts.getTotalProductsPrice())) {
            throw new MenuPriceException();
        }
    }

    private void saveMenuProducts(MenuProducts menuProducts) {
        menuProductRepository.saveAll(menuProducts.getMenuProducts());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
