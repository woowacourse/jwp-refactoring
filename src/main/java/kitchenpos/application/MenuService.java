package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuCommand;
import kitchenpos.application.dto.CreateMenuCommand.CreateMenuProductCommand;
import kitchenpos.application.dto.domain.MenuDto;
import kitchenpos.domain.Money;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.MenuValidator;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toMap;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuDto create(final CreateMenuCommand command) {
        Menu menu = command.toDomain();
        menu.register(menuValidator);
        return MenuDto.from(menuRepository.save(menu));
    }

    public List<MenuDto> list() {
        return menuRepository.findAll().stream()
                .map(MenuDto::from)
                .collect(Collectors.toList());
    }

}
