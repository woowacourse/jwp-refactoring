package kitchenpos.application;

import static java.util.stream.Collectors.groupingBy;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuVerifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuVerifier menuVerifier;

    public MenuService(
        MenuDao menuDao,
        MenuProductDao menuProductDao,
        MenuGroupDao menuGroupDao,
        MenuVerifier menuVerifier
    ) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.menuGroupDao = menuGroupDao;
        this.menuVerifier = menuVerifier;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        if (!menuGroupDao.existsById(menuCreateRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹을 찾지 못했습니다.");
        }

        List<MenuProduct> menuProducts = menuCreateRequest.getMenuProducts()
            .stream()
            .map(MenuProductCreateRequest::toEntity)
            .collect(Collectors.toList());

        menuVerifier.verifyPrice(menuCreateRequest.getPrice(), menuProducts);

        Menu savedMenu = menuDao.save(menuCreateRequest.toEntity());

        List<MenuProduct> savedMenuProducts = menuProducts.stream()
            .peek(it -> it.changeMenuId(savedMenu.getId()))
            .map(menuProductDao::save)
            .collect(Collectors.toList());

        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        Map<Long, Menu> allMenus = menuDao.findAll()
            .stream()
            .collect(Collectors.toMap(Menu::getId, it -> it));
        Map<Menu, List<MenuProduct>> menuProductsGroup = menuProductDao
            .findAllByMenuIdIn(allMenus.keySet())
            .stream()
            .collect(groupingBy(it -> allMenus.get(it.getMenuId())));

        return allMenus.values()
            .stream()
            .map(it -> MenuResponse.of(
                it,
                menuProductsGroup.getOrDefault(it, Collections.emptyList())
            ))
            .collect(Collectors.toList());
    }
}
