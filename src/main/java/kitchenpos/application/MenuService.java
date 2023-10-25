package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.repository.MenuRepository;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupService menuGroupService,
                       final ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    public Menu create(final MenuCreateRequest request) {
        final Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                menuGroupService.findById(request.getMenuGroupId())
        );
        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProductCreateRequest> menuProductCreateRequestList = request.getMenuProducts();
        for (final MenuProductCreateRequest menuProductCreateRequest : menuProductCreateRequestList) {
            final MenuProduct menuProduct =
                    new MenuProduct(
                            savedMenu,
                            productService.findById(menuProductCreateRequest.getProductId()),
                            menuProductCreateRequest.getQuantity()
                    );
            savedMenu.addMenuProduct(menuProduct);
        }
        return savedMenu;
    }

    @Transactional(readOnly = true)
    public Menu findById(final Long id) {
        return menuRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
    }

    @Transactional(readOnly = true)
    public int countByIdIn(final List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

    @Transactional(readOnly = true)
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }
}
