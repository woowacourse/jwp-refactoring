package kitchenpos.service;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.util.FakeMenuGroupDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuGroupServiceTest {

    private final MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
    private final MenuGroupService menuGroupService = new MenuGroupService(menuGroupDao);

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void create() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("우테코 단체");

        MenuGroupResponse menuGroup = menuGroupService.create(menuGroupCreateRequest);

        assertThat(menuGroup.getName()).isEqualTo("우테코 단체");
    }
    @DisplayName("이름이 null인 메뉴 그룹을 생성할 수 없다")
    @Test
    void create_nameNull() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest(null);

        assertThatThrownBy(() -> menuGroupService.create(menuGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("메뉴 그룹의 리스트를 조회한다")
    @Test
    void list() {
        preprocessWhenList(2);

        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        assertThat(menuGroups.size()).isEqualTo(2);
    }

    private void preprocessWhenList(int count) {
        for (int i = 0; i < count; i++) {
            menuGroupDao.save(new MenuGroup("test"));
        }
    }
}
