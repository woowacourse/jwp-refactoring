package kitchenpos.application;

import kitchenpos.application.dto.MenuGroupCreateRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴그룹을 생성한다.")
    @Test
    void create() {
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("단일 메뉴");
        MenuGroupResponse response = menuGroupService.create(request);

        MenuGroup findMenuGroup = menuGroupDao.findById(response.getId())
                .orElseThrow(RuntimeException::new);

        assertThat(findMenuGroup.getName()).isEqualTo(response.getName());
    }

    @DisplayName("메뉴그룹을 조회한다.")
    @Test
    void list() {
        MenuGroup menuGroup1 = new MenuGroup("단일 메뉴");
        MenuGroup menuGroup2 = new MenuGroup("세트 메뉴");

        menuGroupDao.save(menuGroup1);
        menuGroupDao.save(menuGroup2);

        assertThat(menuGroupService.list()).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        menuGroupDao.deleteAll();
    }
}