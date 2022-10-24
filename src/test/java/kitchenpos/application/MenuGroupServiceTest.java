package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setName("한판 메뉴");
    }

    @Test
    void 메뉴그룹을_생성한다() {
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(menuGroupDao.existsById(savedMenuGroup.getId())).isTrue();
    }

    @Test
    void 메뉴그룹_리스트를_반환한다() {
        int beforeSize = menuGroupService.list().size();
        menuGroupService.create(menuGroup);

        assertThat(menuGroupService.list().size()).isEqualTo(beforeSize + 1);
    }
}
