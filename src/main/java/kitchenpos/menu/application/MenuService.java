package kitchenpos.menu.application;

import kitchenpos.exception.NonExistentException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
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
                .orElseThrow(() -> new NonExistentException("menuGroup을 찾을 수 없습니다."));

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        Menu savedMenu = menuRepository.save(menu);
        List<MenuProduct> menuProducts = menuProductService.create(menuRequest, menu);
        savedMenu.addMenuProducts(menuProducts);
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }

    @Transactional
    public MenuResponse update(MenuRequest menuRequest) {

        return null;
    }
}
