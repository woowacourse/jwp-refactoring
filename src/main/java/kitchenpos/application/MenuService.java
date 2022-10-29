package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCommand;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dao.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(MenuRepository menuRepository,
                       MenuValidator menuValidator,
                       MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(MenuCommand menuCommand) {
        String name = menuCommand.getName();
        BigDecimal price = menuCommand.getPrice();
        long menuGroupId = menuCommand.getMenuGroupId();

        validateExistsMenuGroup(menuGroupId);
        Menu menu = menuRepository.save(new Menu(name, price, menuGroupId));
        addMenuProducts(menuCommand, menu);
        return MenuResponse.from(menu);
    }

    private void validateExistsMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void addMenuProducts(MenuCommand menuCommand, Menu menu) {
        List<MenuProduct> menuProducts = toMenuProducts(menuCommand, menu);
        menuValidator.validate(menu.getPrice(), menuProducts);
        menu.addMenuProducts(menuProducts);
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
