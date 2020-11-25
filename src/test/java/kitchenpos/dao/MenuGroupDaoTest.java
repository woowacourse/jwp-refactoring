package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuGroupDaoTest extends DaoTest {

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        assertAll(
            () -> assertThat(menuGroups).hasSize(2),
            () -> assertThat(menuGroups.get(0)).usingRecursiveComparison().isEqualTo(MENU_GROUP_1),
            () -> assertThat(menuGroups.get(1)).usingRecursiveComparison().isEqualTo(MENU_GROUP_2)
        );
    }

    @DisplayName("단건조회 예외 테스트: id에 해당하는 메뉴그룹이 존재하지 않을때")
    @Test
    void findByIdFailByNotExistTest() {
        Optional<MenuGroup> menuGroup = menuGroupDao.findById(-1L);

        assertThat(menuGroup).isEmpty();
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        MenuGroup menuGroup = menuGroupDao.findById(MENU_GROUP_ID_1).get();

        assertThat(menuGroup).usingRecursiveComparison().isEqualTo(MENU_GROUP_1);
    }

    @DisplayName("id로 존재 여부 테스트")
    @Test
    void existsByIdTest() {
        boolean isExist = menuGroupDao.existsById(MENU_GROUP_ID_1);

        assertThat(isExist).isTrue();
    }
}