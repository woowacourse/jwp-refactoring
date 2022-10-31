package kitchenpos.application;

import static kitchenpos.support.DomainFixture.빈_테이블_생성;
import static kitchenpos.support.DomainFixture.채워진_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.TableChangeEmptyRequest;
import kitchenpos.ui.dto.TableChangeGuestNumberRequest;
import kitchenpos.ui.dto.TableCreateRequest;
import kitchenpos.ui.dto.TableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:truncate.sql")
class TableServiceTest {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    private final TableService tableService;

    @Autowired
    public TableServiceTest(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = new TableService(orderTableRepository);
    }

    @Test
    void 테이블을_생성하고_결과를_반환한다() {
        // given
        final var request = new TableCreateRequest(0, true);

        // when
        final var response = tableService.create(request);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getTableGroupId()).isNull(),
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests())
        );
    }


    @Test
    void 테이블_목록을_조회한다() {
        // given
        테이블_생성_및_저장();
        테이블_생성_및_저장();

        // when
        List<TableResponse> responses = tableService.list();

        // then
        assertThat(responses).hasSizeGreaterThanOrEqualTo(2);
    }

    private OrderTable 테이블_생성_및_저장() {
        return orderTableRepository.save(빈_테이블_생성());
    }

    @Test
    void 비어있는지_여부를_변경한다() {
        // given
        final var table = 테이블_생성_및_저장();
        final var changeRequest = new TableChangeEmptyRequest(false);

        // when
        final var response = tableService.changeEmpty(table.getId(), changeRequest);

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(table.getId()),
                () -> assertThat(response.isEmpty()).isEqualTo(changeRequest.getEmpty())
        );
    }

    @Test
    void 비어있는지_여부_변경시_없는_테이블인_경우_예외를_던진다() {
        // given
        final var changeRequest = new TableChangeEmptyRequest(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(0L, changeRequest))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.TABLE_NOT_FOUND_ERROR);
    }

    @Test
    void 비어있는지_여부_변경시_단체_지정이_되어있는_테이블인_경우_예외를_던진다() {
        // given
        final var table = 빈_테이블_생성();
        tableGroupRepository.save(new TableGroup(List.of(table, 빈_테이블_생성()), LocalDateTime.now()));

        final var changeRequest = new TableChangeEmptyRequest(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), changeRequest))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.TABLE_ALREADY_GROUPED_ERROR);
    }

    @Test
    void 방문_손님_수를_변경한다() {
        // given
        final var table = orderTableRepository.save(채워진_테이블_생성());
        final var request = new TableChangeGuestNumberRequest(5);

        // when
        final var response = tableService.changeNumberOfGuests(table.getId(), request);

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(table.getId()),
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(table.getGuestNumber())
        );
    }

    @Test
    void 방문_손님_수_변경시_방문_손님_수가_0보다_작으면_예외를_던진다() {
        // given
        final var table = orderTableRepository.save(채워진_테이블_생성());
        final var request = new TableChangeGuestNumberRequest(-1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(table.getId(), request))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.TABLE_GUEST_NUMBER_NEGATIVE_ERROR);
    }


    @Test
    void 방문_손님_수_변경시_없는_테이블인_경우_예외를_던진다() {
        // given
        final var request = new TableChangeGuestNumberRequest(5);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, request))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.TABLE_NOT_FOUND_ERROR);
    }

    @Test
    void 방문_손님_수_변경시_비어있는_테이블인_경우_예외를_던진다() {
        // given
        final var table = orderTableRepository.save(빈_테이블_생성());
        final var request = new TableChangeGuestNumberRequest(4);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(table.getId(), request))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.TABLE_EMPTY_BUT_CHANGE_GUEST_NUMBER_ERROR);
    }
}
