package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu.repository.MenuGroupRepository;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    protected MenuGroupService menuGroupService;
    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹을 저장한다")
    void create() {
        // given
        MenuGroupCreateRequest createRequest = new MenuGroupCreateRequest("test");

        // when
        MenuGroup createdMenuGroup = menuGroupService.create(createRequest);

        // then
        assertAll(
            () -> assertThat(createdMenuGroup).isNotNull(),
            () -> assertThat(createdMenuGroup.getName()).isEqualTo("test")
        );
    }

    @Test
    @DisplayName("메뉴 그룹을 조회한다")
    void list() {
        // given

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertAll(
            () -> assertThat(menuGroups).hasSameSizeAs(menuGroupRepository.findAll())
        );
    }
}
