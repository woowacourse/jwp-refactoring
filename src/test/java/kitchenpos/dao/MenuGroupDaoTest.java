package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupDaoTest extends DaoTest {

    @Autowired
    MenuGroupDao menuGroupDao;

    @DisplayName("MenuGroup 저장 - 성공")
    @Test
    void save_Success() {
        // given && when
        final MenuGroup savedMenuGroup = saveMenuGroup("메뉴 그룹");

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("MenuGroup ID로 MenuGroup 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnMenuGroup() {
        // given
        final MenuGroup savedMenuGroup = saveMenuGroup("메뉴 그룹");

        // when
        final MenuGroup foundMenuGroup = menuGroupDao.findById(savedMenuGroup.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 MenuGroup이 없습니다."));

        assertThat(foundMenuGroup.getId()).isEqualTo(savedMenuGroup.getId());
    }

    @DisplayName("MenuGroup ID로 MenuGroup 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        // given
        final MenuGroup savedMenuGroup = saveMenuGroup("메뉴 그룹");

        // when
        final Optional<MenuGroup> foundMenuGroup = menuGroupDao.findById(savedMenuGroup.getId()+1);

        // then
        assertThat(foundMenuGroup.isPresent()).isFalse();
    }

    @DisplayName("전체 MenuGroup 조회 - 성공")
    @Test
    void findAll_Success() {
        // given
        final MenuGroup savedMenuGroup1 = saveMenuGroup("메뉴 그룹1");
        final MenuGroup savedMenuGroup2 = saveMenuGroup("메뉴 그룹2");
        final MenuGroup savedMenuGroup3 = saveMenuGroup("메뉴 그룹3");

        // when
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();

        // then
        assertThat(menuGroups).hasSize(3);
    }

    @DisplayName("MenuGroup ID 존재여부 확인 - True, ID 존재")
    @Test
    void existsById_ExistsId_ReturnTrue() {
        // given
        final MenuGroup savedMenuGroup = saveMenuGroup("메뉴 그룹");

        // when
        boolean existsMenuGroup = menuGroupDao.existsById(savedMenuGroup.getId());

        // then
        assertThat(existsMenuGroup).isTrue();
    }

    @DisplayName("MenuGroup ID 존재여부 확인 - False, ID 존재하지 않음")
    @Test
    void existsById_NotExistsId_ReturnFalse() {
        // given
        final MenuGroup savedMenuGroup = saveMenuGroup("메뉴 그룹");

        // when
        boolean existsMenuGroup = menuGroupDao.existsById(savedMenuGroup.getId()+1);

        // then
        assertThat(existsMenuGroup).isFalse();
    }
}
