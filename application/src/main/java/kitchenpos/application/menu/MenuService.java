package kitchenpos.application.menu;

import java.util.ArrayList;
import java.util.stream.Collectors;
import kitchenpos.application.menu.dto.request.CreateMenuDto;
import kitchenpos.application.menu.dto.request.CreateMenuProductDto;
import kitchenpos.application.menu.dto.response.MenuDto;
import kitchenpos.common.domain.menu.MenuHistory;
import kitchenpos.common.domain.menu.MenuProduct;
import kitchenpos.common.domain.menu.ProductQuantities;
import kitchenpos.common.repository.menu.MenuGroupRepository;
import kitchenpos.common.repository.menu.MenuHistoryRepository;
import kitchenpos.common.repository.menu.MenuProductRepository;
import kitchenpos.common.repository.menu.MenuRepository;
import kitchenpos.common.domain.menu.Menu;
import kitchenpos.common.domain.menu.ProductQuantity;
import kitchenpos.common.repository.menu.ProductRepository;
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
