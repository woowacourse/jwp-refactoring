package kitchenpos.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/truncate.sql")
@DataJpaTest
public class MenuGroupRepositoryTest {
    private static final String 그룹_이름_두마리_세트 = "두마리 세트";
    private static final String 그룹_이름_세마리_세트 = "세마리 세트";

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @DisplayName("MenuGroup을 DB에 저장할 경우, 올바르게 수행된다.")
    @Test
    void saveTest() {
        MenuGroup menuGroup = new MenuGroup(그룹_이름_두마리_세트);

        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        Long size = menuGroupRepository.count();

        assertThat(size).isEqualTo(1L);
        assertThat(savedMenuGroup.getId()).isEqualTo(1L);
        assertThat(savedMenuGroup.getName()).isEqualTo(그룹_이름_두마리_세트);
    }

    @DisplayName("MenuGroup의 목록 조회를 요청할 경우, 올바르게 수행된다.")
    @Test
    void findAllTest() {
        MenuGroup menuGroup1 = new MenuGroup(그룹_이름_두마리_세트);
        MenuGroup menuGroup2 = new MenuGroup(그룹_이름_세마리_세트);
        menuGroupRepository.save(menuGroup1);
        menuGroupRepository.save(menuGroup2);

        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        assertThat(menuGroups).hasSize(2);
        assertThat(menuGroups.get(0).getId()).isEqualTo(1L);
        assertThat(menuGroups.get(0).getName()).isEqualTo(그룹_이름_두마리_세트);
        assertThat(menuGroups.get(1).getId()).isEqualTo(2L);
        assertThat(menuGroups.get(1).getName()).isEqualTo(그룹_이름_세마리_세트);
    }
}
