package kitchenpos.domain.menu.service;

import kitchenpos.domain.common.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.repository.MenuGroupRepository;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.menu.service.dto.MenuCreateRequest;
import kitchenpos.domain.menu.service.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final MenuGroup savedMenuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 MenuGroup 입니다. 입력값 : %s", request.getMenuGroupId())));

        final Price price = new Price(BigDecimal.valueOf(request.getPrice()));

        final Menu menu = new Menu(request.getName(), price, savedMenuGroup, new MenuProducts());

        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.toDto(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::toDto)
                .collect(toList());
    }
}
