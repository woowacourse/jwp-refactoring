package kitchenpos.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        // given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("신메뉴");

        // when
        MenuGroupResponse actual = menuGroupService.create(menuGroupRequest);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo("신메뉴"),
            () -> assertThat(menuGroupDao.findAll()).hasSize(5)
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회한다..")
    @Test
    void list() {
        // when
        List<MenuGroupResponse> actual = menuGroupService.list();

        // then
        assertThat(actual)
            .extracting("name")
            .containsOnly("한마리메뉴", "두마리메뉴", "순살파닭두마리메뉴", "신메뉴");
    }
}
