package kitchenpos.dao;

import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_FALSE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableRepositoryTest extends KitchenPosDaoTest {

    @DisplayName("OrderTable 저장 - 성공")
    @Test
    void save_Success() {
        Table table = new Table();
        table.changeNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        table.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        Table savedTable = tableRepository.save(table);

        assertThat(savedTable.getId()).isNotNull();
        assertThat(savedTable.getNumberOfGuests())
            .isEqualTo(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        assertThat(savedTable.isEmpty()).isEqualTo(TEST_ORDER_TABLE_EMPTY_FALSE);
        assertThat(savedTable.getTableGroup()).isNull();
    }

    @DisplayName("OrderTable ID로 OrderTable 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnOrderTable() {
        Table table = new Table();
        table.changeNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        table.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        Table savedTable = tableRepository.save(table);

        Table foundTable = tableRepository.findById(savedTable.getId())
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 orderTable이 없습니다."));

        assertThat(foundTable.getId()).isEqualTo(savedTable.getId());
        assertThat(foundTable.getTableGroup()).isEqualTo(savedTable.getTableGroup());
        assertThat(foundTable.getNumberOfGuests())
            .isEqualTo(savedTable.getNumberOfGuests());
        assertThat(foundTable.isEmpty()).isEqualTo(savedTable.isEmpty());
    }

    @DisplayName("OrderTable ID로 OrderTable 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        Table table = new Table();
        table.changeNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        table.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        Table savedTable = tableRepository.save(table);

        Optional<Table> foundOrderTable = tableRepository
            .findById(savedTable.getId() + 1);
        assertThat(foundOrderTable.isPresent()).isFalse();
    }

    @DisplayName("전체 OrderTable 조회 - 성공")
    @Test
    void findAll_Success() {
        Table table = new Table();
        table.changeNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        table.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        Table savedTable = tableRepository.save(table);

        List<Table> tables = tableRepository.findAll();
        assertThat(tables).isNotNull();
        assertThat(tables).isNotEmpty();

        List<Long> orderTableIds = getIds(tables);
        assertThat(orderTableIds).contains(savedTable.getId());
    }

    @DisplayName("OrderTable ID들로 OrderTable들 조회 - 조회됨, OrderTable ID들이 존재하는 경우")
    @Test
    void findAllByIdIn_ExistsOrderTableIds_ReturnOrderTables() {
        Table table = new Table();
        table.changeNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        table.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        Table savedTable = tableRepository.save(table);

        Table otherTable = new Table();
        otherTable.changeNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        otherTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        Table savedOtherTable = tableRepository.save(otherTable);

        Table anotherTable = new Table();
        anotherTable.changeNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        anotherTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        tableRepository.save(anotherTable);

        List<Long> ids = Arrays.asList(savedTable.getId(), savedOtherTable.getId());

        List<Table> tables = tableRepository.findAllByIdIn(ids);
        assertThat(tables).hasSize(ids.size());

        List<Long> orderTableIds = getIds(tables);
        assertThat(orderTableIds).containsAll(orderTableIds);
    }

    @DisplayName("OrderTable ID들로 OrderTable들 조회 - 조회되지 않음, OrderTable ID들이 존재하지 않는 경우")
    @Test
    void findAllByIdIn_NotExistsOrderTableIds_ReturnEmpty() {
        Table table = new Table();
        table.changeNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        table.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        Table savedTable = tableRepository.save(table);

        List<Long> ids = Arrays.asList(savedTable.getId() + 1, savedTable.getId() + 2);

        List<Table> tables = tableRepository.findAllByIdIn(ids);
        assertThat(tables).isEmpty();
    }

    @DisplayName("TableGroup ID로 OrderTable들 조회 - 조회됨, TableGroup ID에 매치되는 경우")
    @Test
    void findAllByTableGroupId_MatchedTableGroupId_ReturnOrderTable() {
        TableGroup tableGroup = getCreatedTableGroup();

        Table table = new Table();
        table.changeNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        table.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        table.setTableGroup(tableGroup);
        Table savedTable = tableRepository.save(table);

        Table otherTable = new Table();
        otherTable.changeNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        otherTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        otherTable.setTableGroup(tableGroup);
        Table savedOtherTable = tableRepository.save(otherTable);

        Table anotherTable = new Table();
        anotherTable.changeNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        anotherTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        tableRepository.save(anotherTable);

        List<Long> ids = Arrays.asList(savedTable.getId(), savedOtherTable.getId());

        List<Table> foundTables = tableRepository
            .findAllByTableGroup_Id(tableGroup.getId());
        assertThat(foundTables).hasSize(ids.size());

        List<Long> foundOrderTableIds = getIds(foundTables);
        assertThat(foundOrderTableIds).containsAll(ids);
    }

    @DisplayName("TableGroup ID로 OrderTable들 조회 - 조회되지 않음, TableGroup ID에 매치되지 않는 경우")
    @Test
    void findAllByTableGroupId_NotMatchedTableGroupId_ReturnOrderTable() {
        Long tableGroupId = getCreatedTableGroupId();

        List<Table> foundTables = tableRepository
            .findAllByTableGroup_Id(tableGroupId);
        assertThat(foundTables).isEmpty();
    }

    private List<Long> getIds(List<Table> tables) {
        return tables.stream()
            .map(Table::getId)
            .collect(Collectors.toList());
    }
}
