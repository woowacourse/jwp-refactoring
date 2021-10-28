package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.exception.NotFoundException;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductService menuProductService;

    public MenuService(MenuRepository menuRepository,
                       MenuGroupRepository menuGroupRepository,
                       MenuProductService menuProductService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductService = menuProductService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new NotFoundException("menuGroup을 찾을 수 없습니다."));

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        Menu savedMenu = menuRepository.save(menu);

        List<MenuProduct> menuProducts = menuProductService.create(menuRequest, menu);
        return MenuResponse.of(savedMenu, menuProducts);
    }

    public List<MenuResponse> list() {
        Map<Menu, List<MenuProduct>> results = new HashMap<>();

        final List<Menu> menus = menuRepository.findAll();
        for (final Menu menu : menus) {
            results.put(menu, menuProductService.findAllByMenuId(menu.getId()));
        }

        return MenuResponse.from(results);
    }
}
