package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("MenuGroup을 생성하고 DB에 저장한다.")
    @Test
    void createTest() {
        MenuGroup input = new MenuGroup("새로운_메뉴_그룹");

        MenuGroup result = menuGroupService.create(input);

        MenuGroup savedMenuGroup = menuGroupDao.findById(result.getId())
                .orElseThrow(() -> new NoSuchElementException("저장되지 않았습니다."));
        assertThat(savedMenuGroup.getName()).isEqualTo(input.getName());
    }

    @DisplayName("MenuGroup 목록을 조회한다.")
    @Test
    void listTest() {
        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups).hasSize(menuGroupDao.findAll().size());
    }
}