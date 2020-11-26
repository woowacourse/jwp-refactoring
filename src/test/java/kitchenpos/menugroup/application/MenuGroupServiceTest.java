package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.application.ServiceTest;
import kitchenpos.menugroup.application.dto.MenuGroupCreateRequestDto;
import kitchenpos.menugroup.application.dto.MenuGroupResponseDto;
import kitchenpos.menugroup.model.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;

class MenuGroupServiceTest extends ServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        String name = "치킨";
        MenuGroupCreateRequestDto menuGroupCreateRequest = new MenuGroupCreateRequestDto(name);

        MenuGroupResponseDto menuGroupResponse = menuGroupService.create(menuGroupCreateRequest);

        assertAll(
            () -> assertThat(menuGroupResponse.getId()).isNotNull(),
            () -> assertThat(menuGroupResponse.getName()).isEqualTo(name)
        );
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void list() {
        MenuGroup menuGroup = new MenuGroup(null, "치킨");
        menuGroupRepository.save(menuGroup);

        List<MenuGroupResponseDto> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(1);
    }
}