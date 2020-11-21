package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.utils.TestObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"/truncate.sql", "/init-data.sql"})
class MenuServiceTest {

    private static final String NEW_MENU_NAME = "후라이드양념간장메뉴";
    private static final BigDecimal NEW_MENU_PRICE = new BigDecimal(48_000L);
    private static final MenuGroupCreateRequest MENU_GROUP_CREATE_REQUEST
        = TestObjectFactory.createMenuGroupCreateRequest("세마리메뉴");

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("새로운 메뉴를 생성한다.")
    @Test
    void create() {
        MenuGroupResponse menuGroupResponse = menuGroupService.create(MENU_GROUP_CREATE_REQUEST);
        MenuGroup menuGroup = menuGroupRepository.getOne(menuGroupResponse.getId());
        List<MenuProduct> newMenuProduct =
            Arrays.asList(menuProductRepository.findAllByMenuId(1L).get(0),
                menuProductRepository.findAllByMenuId(2L).get(0),
                menuProductRepository.findAllByMenuId(5L).get(0));

        MenuCreateRequest menuCreateRequest = TestObjectFactory
            .createMenuCreateRequest(NEW_MENU_NAME, NEW_MENU_PRICE, menuGroup, newMenuProduct);

        MenuResponse menuResponse = menuService.create(menuCreateRequest);

        assertAll(() -> {
            assertThat(menuResponse).isInstanceOf(MenuResponse.class);
            assertThat(menuResponse).isNotNull();
            assertThat(menuResponse.getId()).isNotNull();
            assertThat(menuResponse.getName()).isNotNull();
            assertThat(menuResponse.getName()).isEqualTo(menuCreateRequest.getName());
            assertThat(menuResponse.getPrice()).isNotNull();
            assertThat(menuResponse.getPrice().toBigInteger())
                .isEqualTo(menuResponse.getPrice().toBigInteger());
            assertThat(menuResponse.getMenuGroupId()).isNotNull();
            assertThat(menuResponse.getMenuGroupId())
                .isEqualTo(menuCreateRequest.getMenuGroupId());
            assertThat(menuResponse.getMenuProducts()).isNotEmpty();
            assertThat(menuResponse.getMenuProducts().size())
                .isEqualTo(menuCreateRequest.getMenuProductRequests().size());
        });
    }

    @DisplayName("새로운 메뉴를 생성한다. - 메뉴 가격이 null일 경우")
    @Test
    void create_IfMenuPriceNull_ThrowException() {
        MenuGroupResponse menuGroupResponse = menuGroupService.create(MENU_GROUP_CREATE_REQUEST);
        MenuGroup menuGroup = menuGroupRepository.getOne(menuGroupResponse.getId());
        List<MenuProduct> new_menu_product =
            Arrays.asList(menuProductRepository.findAllByMenuId(1L).get(0),
                menuProductRepository.findAllByMenuId(2L).get(0),
                menuProductRepository.findAllByMenuId(5L).get(0));

        MenuCreateRequest menuCreateRequest = TestObjectFactory
            .createMenuCreateRequest(NEW_MENU_NAME, null, menuGroup, new_menu_product);

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성한다. - 메뉴 가격이 0 이하일 경우")
    @Test
    void create_IfMenuPriceIsNotPositive_ThrowException() {
        MenuGroupResponse menuGroupResponse = menuGroupService.create(MENU_GROUP_CREATE_REQUEST);
        MenuGroup menuGroup = menuGroupRepository.getOne(menuGroupResponse.getId());
        List<MenuProduct> new_menu_product =
            Arrays.asList(menuProductRepository.findAllByMenuId(1L).get(0),
                menuProductRepository.findAllByMenuId(2L).get(0),
                menuProductRepository.findAllByMenuId(5L).get(0));

        MenuCreateRequest menuCreateRequest = TestObjectFactory
            .createMenuCreateRequest(NEW_MENU_NAME, new BigDecimal(-1L), menuGroup,
                new_menu_product);

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성한다. - groupId가 메뉴 그룹에 존재하지 않는 경우")
    @Test
    void create_IfGroupIdNotExist_ThrowException() {
        MenuGroup invalidMenuGroup = new MenuGroup(0L,null);

        List<MenuProduct> new_menu_product =
            Arrays.asList(menuProductRepository.findAllByMenuId(1L).get(0),
                menuProductRepository.findAllByMenuId(2L).get(0),
                menuProductRepository.findAllByMenuId(5L).get(0));

        MenuCreateRequest menuCreateRequest = TestObjectFactory
            .createMenuCreateRequest(NEW_MENU_NAME, NEW_MENU_PRICE, invalidMenuGroup,
                new_menu_product);

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성한다. - 메뉴 가격이 모든 메뉴 가격 합을 초과할 경우")
    @Test
    void create_IfInvalidMenuPrice_ThrowException() {
        MenuGroupResponse menuGroupResponse = menuGroupService.create(MENU_GROUP_CREATE_REQUEST);
        MenuGroup menuGroup = menuGroupRepository.getOne(menuGroupResponse.getId());
        List<MenuProduct> new_menu_product =
            Arrays.asList(menuProductRepository.findAllByMenuId(1L).get(0),
                menuProductRepository.findAllByMenuId(2L).get(0),
                menuProductRepository.findAllByMenuId(5L).get(0));

        MenuCreateRequest menuCreateRequest = TestObjectFactory
            .createMenuCreateRequest(NEW_MENU_NAME, new BigDecimal(50_000L), menuGroup,
                new_menu_product);

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴 리스트를 조회한다.")
    @Test
    void list() {
        List<MenuResponse> menuResponses = menuService.list();

        assertAll(() -> {
            assertThat(menuResponses).isNotEmpty();
            assertThat(menuResponses).hasSize(6);
        });
    }
}
