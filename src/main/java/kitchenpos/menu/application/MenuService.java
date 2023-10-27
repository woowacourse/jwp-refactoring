package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.CreateMenuDto;
import kitchenpos.menu.application.dto.CreateMenuProductDto;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductQuantity;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuGroupException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuDto create(CreateMenuDto createMenuDto) {
        Long menuGroupId = createMenuDto.getMenuGroupId();
        checkMenuGroupExists(menuGroupId);
        Menu menu = new Menu(createMenuDto.getName(), createMenuDto.getPrice(), menuGroupId);
        List<MenuProduct> menuProducts = makeMenuProducts(createMenuDto.getMenuProducts());
        menu.addMenuProducts(menuProducts);
        menu = menuRepository.save(menu);
        return MenuDto.from(menu);
    }

    private void checkMenuGroupExists(Long menuGroupId) {
        if (menuGroupRepository.existsById(menuGroupId)) {
            return;
        }
        throw new MenuGroupException("존재하지 않는 메뉴 그룹입니다.");
    }

    private List<MenuProduct> makeMenuProducts(List<CreateMenuProductDto> createMenuProductDtos) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (CreateMenuProductDto createMenuProductDto : createMenuProductDtos) {
            Product product = findProduct(createMenuProductDto.getProductId());
            MenuProductQuantity quantity = new MenuProductQuantity(createMenuProductDto.getQuantity());
            MenuProduct menuProduct = new MenuProduct(product, quantity);
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("해당 상품이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<MenuDto> list() {
        List<Menu> menus = menuRepository.findAllWithMenuProducts();
        return menus.stream()
                .map(MenuDto::from)
                .collect(Collectors.toList());
    }
}
