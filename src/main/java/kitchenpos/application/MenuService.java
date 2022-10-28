package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCommand;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuCreatedValidator menuValidator;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(MenuRepository menuRepository,
                       MenuCreatedValidator menuValidator,
                       MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(MenuCommand menuCommand) {
        validateExistsMenuGroup(menuCommand.getMenuGroupId());
        Menu menu = menuRepository.save(
                new Menu(menuCommand.getName(), menuCommand.getPrice(), menuCommand.getMenuGroupId()));
        List<MenuProduct> menuProducts = toMenuProducts(menuCommand, menu);
        menuValidator.validate(menu.getPrice(), menuProducts);
        menu.addMenuProducts(menuProducts);
        return MenuResponse.from(menu);
    }

    private void validateExistsMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private List<MenuProduct> toMenuProducts(MenuCommand menuCommand, Menu menu) {
        return menuCommand.getMenuProducts().stream()
                .map(it -> it.toEntity(menu.getId()))
                .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
