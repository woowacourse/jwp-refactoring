package kitchenpos.menu.application;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductAppender;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.MenuValidator;
import kitchenpos.vo.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductAppender menuProductAppender;
    private final MenuValidator menuValidator;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuProductAppender menuProductAppender,
        final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuProductAppender = menuProductAppender;
        this.menuValidator = menuValidator;
    }

    @Transactional(readOnly = true)
    public List<MenuDto> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuDto::from)
            .collect(toUnmodifiableList());
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        final Price price = new Price(menuDto.getPrice());
        final Menu menu = new Menu(menuDto.getName(), price, menuDto.getMenuGroupId());

        menuProductAppender.append(menu, menuDto.getProductQuantityMap());
        menu.validateCreate(menuValidator);

        final Menu savedMenu = menuRepository.save(menu);
        return MenuDto.from(savedMenu);
    }
}
