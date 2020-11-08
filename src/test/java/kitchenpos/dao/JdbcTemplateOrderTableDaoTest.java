package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static kitchenpos.application.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.application.fixture.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DaoTest
class JdbcTemplateOrderTableDaoTest {
    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private Long tableGroupId;

    @BeforeEach
    void setUp() {
        tableGroupId =
                tableGroupDao.save(createTableGroup(null, LocalDateTime.now(), Collections.emptyList())).getId();
    }

    @Test
    @DisplayName("주문 테이블 엔티티를 저장하면 id가 부여되고, 엔티티의 필드인 메뉴 상품 리스트는 저장되지 않는다")
    void insert() {
        OrderTable orderTable = createOrderTable(null, false, tableGroupId, 4);

        OrderTable result = orderTableDao.save(orderTable);

        assertAll(
                () -> assertThat(result).isEqualToIgnoringGivenFields(result, "id"),
                () -> assertThat(result.getId()).isNotNull()
        );
    }


    @Test
    @DisplayName("존재하는 id로 엔티티를 조회하면 저장되어있는 엔티티가 조회된다")
    void findExist() {
        OrderTable orderTable = createOrderTable(null, false, tableGroupId, 4);
        OrderTable persisted = orderTableDao.save(orderTable);

        OrderTable result = orderTableDao.findById(persisted.getId()).get();

        assertThat(result).isEqualToComparingFieldByField(persisted);
    }

    @Test
    @DisplayName("저장되어있지 않은 엔티티를 조회하면 빈 optional 객체가 반환된다")
    void findNotExist() {
        assertThat(orderTableDao.findById(0L)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("모든 엔티티를 조회하면 저장되어 있는 엔티티들이 반환된다")
    void findAll() {
        orderTableDao.save(createOrderTable(null, false, tableGroupId, 4));
        orderTableDao.save(createOrderTable(null, false, tableGroupId, 4));
        orderTableDao.save(createOrderTable(null, false, tableGroupId, 4));

        assertThat(orderTableDao.findAll()).hasSize(3);
    }
}