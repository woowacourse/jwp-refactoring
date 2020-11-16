package kitchenpos.application;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.MenuGroup;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("create: 메뉴 그룹 생성 요청시, 입력 받은 이름을 기반으로 생성 하면, ID 생성 및 입력 값을 통해 생성된다.")
    @Test
    void create() {
        MenuGroup createdMenuGroup = menuGroupService.create(createMenuGroup("치킨 두마리 세트"));

        assertAll(
                () -> assertThat(createdMenuGroup.getId()).isNotNull(),
                () -> assertThat(createdMenuGroup.getName()).isEqualTo("치킨 두마리 세트")
        );
    }

    @DisplayName("list: 현재 저장 되어 있는 메뉴그룹의 목록을 반환한다.")
    @Test
    void list() {
        menuGroupService.create(createMenuGroup("치킨세트"));
        menuGroupService.create(createMenuGroup("치킨단품"));
        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(2);
    }
}