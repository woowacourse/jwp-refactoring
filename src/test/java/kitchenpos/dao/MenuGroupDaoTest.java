package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class MenuGroupDaoTest {

    @Autowired
    DataSource dataSource;

    MenuGroupDao sut;

    @BeforeEach
    void setUp() {
        sut = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    @DisplayName("MenuGroup을 저장하고 저장된 MenuGroup을 반환한다")
    void save() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("치킨");

        // when
        MenuGroup savedMenuGroup = sut.save(menuGroup);

        // then
        MenuGroup findMenuGroup = sut.findById(savedMenuGroup.getId()).get();
        assertThat(savedMenuGroup).isEqualTo(findMenuGroup);
    }

    @Test
    @DisplayName("입력받은 id에 해당하는 MenuGroup이 존재하지 않으면 빈 객체를 반환한다")
    void returnOptionalEmpty_WhenMenuGroupNonExist() {
        Optional<MenuGroup> actual = sut.findById(0L);
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("저장된 모든 MenuGroup을 조회한다")
    void findAll() {
        // given
        List<MenuGroup> previouslySaved = sut.findAll();

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("치킨");
        sut.save(menuGroup);

        // when
        List<MenuGroup> actual = sut.findAll();

        // then
        assertThat(actual.size()).isEqualTo(previouslySaved.size() + 1);
    }

    @Test
    @DisplayName("입력된 id에 해당하는 MenuGroup이 있으면 참을 반환한다")
    void returnTrue_WhenExist() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("치킨");
        Long menuGroupId = sut.save(menuGroup).getId();

        // when
        boolean actual = sut.existsById(menuGroupId);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("입력된 id에 해당하는 MenuGroup이 없으면 거짓을 반환한다")
    void returnFalse_WhenNonExist() {
        boolean actual = sut.existsById(0L);

        assertThat(actual).isFalse();
    }
}
