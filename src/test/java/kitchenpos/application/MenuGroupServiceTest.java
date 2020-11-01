package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import kitchenpos.menu.service.MenuGroupService;

@SpringBootTest
@Sql(value = "/truncate.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void createMenu() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("음료");

        Long id = menuGroupService.create(menuGroupCreateRequest);

        assertThat(id).isNotNull();
    }

    @DisplayName("메뉴 리스트를 조회한다.")
    @Test
    void list() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("음료");

        Long id = menuGroupService.create(menuGroupCreateRequest);
        List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(
            () -> assertThat(menuGroups).hasSize(1),
            () -> assertThat(menuGroups.get(0).getId()).isEqualTo(id),
            () -> assertThat(menuGroups.get(0).getName()).isEqualTo("음료")
        );
    }
}
