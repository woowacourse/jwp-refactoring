package kitchenpos.menu.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.exception.CustomException;
import kitchenpos.exception.ExceptionType;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuValidator menuValidator;
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
        final MenuValidator menuValidator,
        final MenuRepository menuRepository,
        final MenuProductRepository menuProductRepository
    ) {
        this.menuValidator = menuValidator;
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        Menu createdMenu = menuRepository.save(menu);

        menuValidator.validate(createdMenu);

        return createdMenu;
    }

    public List<MenuDto> list() {
        final List<MenuDto> menuDtos = menuRepository.findAll()
                                                     .stream()
                                                     .map(MenuDto::from)
                                                     .collect(toList());

        for (final MenuDto menuDto : menuDtos) {
            List<MenuProductDto> menuProductDtos = menuProductRepository.findAllByMenuId(menuDto.getId())
                                                                        .stream()
                                                                        .map(menuProduct -> MenuProductDto.from(menuProduct, menuDto.getId()))
                                                                        .collect(toList());
            menuDto.setMenuProductDtos(menuProductDtos);
        }

        return menuDtos;
    }

    public Menu findById(Long menuId) {
        return menuRepository.findById(menuId)
                             .orElseThrow(() -> new CustomException(ExceptionType.MENU_NOT_FOUND));
    }
}
