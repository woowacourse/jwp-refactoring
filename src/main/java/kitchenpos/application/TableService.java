package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableOrderEmptyValidateService;
import kitchenpos.domain.TableRepository;
import kitchenpos.dto.NumberOfGuestsChangeRequest;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableEmptyChangeRequest;
import kitchenpos.dto.TableResponse;

@Service
public class TableService {
    private final TableRepository tableRepository;
    private final TableOrderEmptyValidateService tableOrderEmptyValidateService;

    public TableService(TableRepository tableRepository,
        TableOrderEmptyValidateService tableOrderEmptyValidateService) {
        this.tableRepository = tableRepository;
        this.tableOrderEmptyValidateService = tableOrderEmptyValidateService;
    }

    @Transactional
    public TableResponse create(TableCreateRequest tableCreateRequest) {
        return TableResponse.of(tableRepository.save(tableCreateRequest.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        return TableResponse.ofList(tableRepository.findAll());
    }

    @Transactional
    public TableResponse changeEmpty(Long tableId, TableEmptyChangeRequest tableEmptyChangeRequest) {
        OrderTable table = tableRepository.findById(tableId).orElseThrow(IllegalArgumentException::new);
        table.changeEmpty(tableEmptyChangeRequest.isEmpty(), tableOrderEmptyValidateService);
        return TableResponse.of(table);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(Long tableId, NumberOfGuestsChangeRequest numberOfGuestsChangeRequest) {
        OrderTable table = tableRepository.findById(tableId).orElseThrow(IllegalArgumentException::new);
        table.changeNumberOfGuests(numberOfGuestsChangeRequest.getNumberOfGuests());
        return TableResponse.of(table);
    }
}
