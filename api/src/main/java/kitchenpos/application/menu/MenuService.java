package kitchenpos.application.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.repository.MenuGroupRepository;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menu.MenuUpdateRequest;
import kitchenpos.exception.NonExistentException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        menuProductService.addMenuToMenuProduct(menuRequest, savedMenu);
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }

    @Transactional
    public MenuResponse update(Long menuId, MenuUpdateRequest menuUpdateRequest) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NonExistentException("menu를 찾을 수 없습니다."));
        menu.update(menuUpdateRequest.getName(), menuUpdateRequest.getPrice());
        menuRepository.save(menu);
        return MenuResponse.from(menu);
    }
}
