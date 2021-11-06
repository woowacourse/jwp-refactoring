package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductService menuProductService;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuProductService menuProductService) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductService = menuProductService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다."));
        Menu menuEntity = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        Menu menu = menuRepository.save(menuEntity);
        List<MenuProduct> menuProducts = menuProductService.create(menu, menuRequest);
        return MenuResponse.of(menu, menuProducts);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll() {
        Map<Menu, List<MenuProduct>> menuMap = new HashMap<>();

        List<Menu> menus = menuRepository.findAll();
        for (Menu menu : menus) {
            List<MenuProduct> menuProducts = menuProductService.findAllByMenuId(menu.getId());
            menuMap.put(menu, menuProducts);
        }
        return MenuResponse.from(menuMap);
    }
}
