package kitchenpos.application;

import java.util.ArrayList;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.CreateMenuDto;
import kitchenpos.application.dto.request.CreateMenuProductDto;
import kitchenpos.application.dto.response.MenuDto;
import kitchenpos.domain.menu.MenuHistory;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.ProductQuantities;
import kitchenpos.domain.menu.repository.MenuGroupRepository;
import kitchenpos.domain.menu.repository.MenuHistoryRepository;
import kitchenpos.domain.menu.repository.MenuProductRepository;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.ProductQuantity;
import kitchenpos.domain.menu.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuHistoryRepository menuHistoryRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository,
                       MenuHistoryRepository menuHistoryRepository,
                       MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuHistoryRepository = menuHistoryRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuDto> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(menu -> MenuDto.of(menu, menuProductRepository.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }

    public MenuDto create(final CreateMenuDto createMenuDto) {
        if (!menuGroupRepository.existsById(createMenuDto.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
        final ProductQuantities productQuantities = getMenuProductQuantities(createMenuDto.getMenuProducts());
        final Menu savedMenu = menuRepository.save(createMenuDto.toEntity(productQuantities));
        menuHistoryRepository.save(MenuHistory.of(savedMenu));
        final List<MenuProduct> savedMenuProducts = saveMenuProducts(productQuantities, savedMenu);
        return MenuDto.of(savedMenu, savedMenuProducts);
    }

    private ProductQuantities getMenuProductQuantities(List<CreateMenuProductDto> menuProductDtos) {
        return new ProductQuantities(menuProductDtos.stream()
                .map(it -> new ProductQuantity(productRepository.get(it.getProductId()), it.getQuantity()))
                .collect(Collectors.toList()));
    }

    private List<MenuProduct> saveMenuProducts(ProductQuantities productQuantities, Menu menu) {
        ArrayList<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProduct menuProduct : productQuantities.toMenuProducts(menu.getId())) {
            menuProducts.add(menuProductRepository.save(menuProduct));
        }
        return menuProducts;
    }
}
