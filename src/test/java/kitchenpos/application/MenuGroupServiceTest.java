package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("create: 메뉴 그룹 생성 테스트")
    @Test
    void createTest() {
        final MenuGroupRequest menuGroup = new MenuGroupRequest("추천 메뉴");

        final MenuGroupResponse actual = menuGroupService.create(menuGroup);

        assertThat(actual.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("findMenuGroups: 전체 메뉴 그룹 목록 조회 테스트")
    @Test
    void findMenuGroupsTest() {
        menuGroupDao.save(new MenuGroup("한마리 메뉴"));
        menuGroupDao.save(new MenuGroup("두마리 메뉴"));

        final List<MenuGroupResponse> menuGroups = menuGroupService.list();

        assertAll(
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(menuGroups.get(0).getName()).isEqualTo("한마리 메뉴"),
                () -> assertThat(menuGroups.get(1).getName()).isEqualTo("두마리 메뉴")
        );
    }
}

