package kitchenpos.application;

import static kitchenpos.support.DomainFixture.빈_테이블_생성;
import static kitchenpos.support.DomainFixture.채워진_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.table.OrderTableService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.dto.request.OrderTableChangeGuestNumberRequest;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.table.OrderTableRepository;
import kitchenpos.repository.table.TableGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:truncate.sql")
class OrderTableServiceTest {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    private final OrderTableService orderTableService;

    @Autowired
    public OrderTableServiceTest(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableService = new OrderTableService(orderTableRepository);
    }

    @Test
    void 테이블을_생성하고_결과를_반환한다() {
        // given
        final var request = new OrderTableCreateRequest(0, true);

        // when
        final var response = orderTableService.create(request);

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
        List<OrderTableResponse> responses = orderTableService.list();

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
        final var changeRequest = new OrderTableChangeEmptyRequest(false);

        // when
        final var response = orderTableService.changeEmpty(table.getId(), changeRequest);

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(table.getId()),
                () -> assertThat(response.isEmpty()).isEqualTo(changeRequest.getEmpty())
        );
    }

    @Test
    void 비어있는지_여부_변경시_없는_테이블인_경우_예외를_던진다() {
        // given
        final var changeRequest = new OrderTableChangeEmptyRequest(false);

        // when & then
        assertThatThrownBy(() -> orderTableService.changeEmpty(0L, changeRequest))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.TABLE_NOT_FOUND_ERROR);
    }

    @Test
    void 비어있는지_여부_변경시_단체_지정이_되어있는_테이블인_경우_예외를_던진다() {
        // given
        final var table = 빈_테이블_생성();
        tableGroupRepository.save(new TableGroup(List.of(table, 빈_테이블_생성()), LocalDateTime.now()));

        final var changeRequest = new OrderTableChangeEmptyRequest(false);

        // when & then
        assertThatThrownBy(() -> orderTableService.changeEmpty(table.getId(), changeRequest))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.TABLE_ALREADY_GROUPED_ERROR);
    }

    @Test
    void 방문_손님_수를_변경한다() {
        // given
        final var table = orderTableRepository.save(채워진_테이블_생성());
        final var request = new OrderTableChangeGuestNumberRequest(5);

        // when
        final var response = orderTableService.changeNumberOfGuests(table.getId(), request);

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
        final var request = new OrderTableChangeGuestNumberRequest(-1);

        // when & then
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(table.getId(), request))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.TABLE_GUEST_NUMBER_NEGATIVE_ERROR);
    }


    @Test
    void 방문_손님_수_변경시_없는_테이블인_경우_예외를_던진다() {
        // given
        final var request = new OrderTableChangeGuestNumberRequest(5);

        // when & then
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(0L, request))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.TABLE_NOT_FOUND_ERROR);
    }

    @Test
    void 방문_손님_수_변경시_비어있는_테이블인_경우_예외를_던진다() {
        // given
        final var table = orderTableRepository.save(빈_테이블_생성());
        final var request = new OrderTableChangeGuestNumberRequest(4);

        // when & then
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(table.getId(), request))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.TABLE_EMPTY_BUT_CHANGE_GUEST_NUMBER_ERROR);
    }
}
