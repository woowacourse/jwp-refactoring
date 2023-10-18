package kitchenpos.application;

import kitchenpos.application.dto.CreateMenuDto;
import kitchenpos.application.dto.CreateMenuProductDto;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.menu.*;
import kitchenpos.domain.product.Product;
import kitchenpos.exception.MenuGroupException;
import kitchenpos.exception.ProductException;
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
        MenuGroup menuGroup = findMenuGroup(createMenuDto.getMenuGroupId());
        Menu menu = new Menu(
                new MenuName(createMenuDto.getName()),
                new MenuPrice(createMenuDto.getPrice()),
                menuGroup);
        List<MenuProduct> menuProducts = makeMenuProducts(createMenuDto.getMenuProducts());
        menu.addMenuProducts(menuProducts);
        menu = menuRepository.save(menu);
        return MenuDto.from(menu);
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new MenuGroupException("존재하지 않는 메뉴 그룹입니다."));
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
