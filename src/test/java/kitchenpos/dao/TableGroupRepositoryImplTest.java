package kitchenpos.dao;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup2;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.support.JdbcTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupRepositoryImplTest extends JdbcTestHelper {

  @Autowired
  private TableGroupRepositoryImpl tableGroupRepository;

  @Test
  @DisplayName("save() : 테이블 그룹을 저장할 수 있다.")
  void test_save() throws Exception {
    //given
    final TableGroup2 tableGroup = TableGroupFixture.createTableGroup();

    //when
    final TableGroup2 savedTableGroup = tableGroupRepository.save(tableGroup);

    //then
    assertAll(
        () -> assertNotNull(savedTableGroup.getId()),
        () -> Assertions.assertThat(savedTableGroup)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(tableGroup)
    );
  }

  @Test
  @DisplayName("findById() : id를 통해 테이블 그룹을 조회할 수 있다.")
  void test_findById() throws Exception {
    //given
    final TableGroup2 tableGroup = tableGroupRepository.save(
        TableGroupFixture.createTableGroup()
    );

    //when
    final Optional<TableGroup2> savedTableGroup = tableGroupRepository.findById(tableGroup.getId());

    //then
    assertAll(
        () -> assertTrue(savedTableGroup.isPresent()),
        () -> Assertions.assertThat(savedTableGroup.get())
            .usingRecursiveComparison()
            .isEqualTo(tableGroup)
    );
  }

  @Test
  @DisplayName("findAll() : 모든 테이블 그룹을 조회할 수 있다.")
  void test_findAll() throws Exception {
    //given
    final TableGroup2 tableGroup1 = tableGroupRepository.save(
        TableGroupFixture.createTableGroup()
    );
    final TableGroup2 tableGroup2 = tableGroupRepository.save(
        TableGroupFixture.createTableGroup()
    );

    //when
    final List<TableGroup2> tableGroups = tableGroupRepository.findAll();

    //then
    Assertions.assertThat(tableGroups)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(List.of(tableGroup1, tableGroup2));
  }
}
