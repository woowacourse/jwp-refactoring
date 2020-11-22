package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;

class MenuGroupServiceTest extends ServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹을 추가한다.")
    @Test
    void create() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("test_group");

        MenuGroupResponse actual = menuGroupService.create(menuGroupCreateRequest);

        assertAll(
            () -> assertThat(actual).extracting(MenuGroupResponse::getId).isNotNull(),
            () -> assertThat(actual).extracting(MenuGroupResponse::getName).isEqualTo(menuGroupCreateRequest.getName())
        );
    }

    @DisplayName("메뉴 그룹 전체 목록을 조회한다.")
    @Test
    void list() {
        MenuGroup menuGroup = new MenuGroup("Test");

        menuGroupRepository.save(menuGroup);

        List<MenuGroupResponse> actual = menuGroupService.list();

        assertThat(actual).hasSize(1);
    }
}