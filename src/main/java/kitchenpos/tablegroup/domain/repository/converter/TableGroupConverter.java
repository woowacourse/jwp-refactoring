package kitchenpos.tablegroup.domain.repository.converter;

import kitchenpos.support.Converter;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.persistence.dto.TableGroupDataDto;
import org.springframework.stereotype.Component;

@Component
public class TableGroupConverter implements Converter<TableGroup, TableGroupDataDto> {

    @Override
    public TableGroupDataDto entityToData(final TableGroup tableGroup) {
        return new TableGroupDataDto(tableGroup.getId(), tableGroup.getCreatedDate());
    }

    @Override
    public TableGroup dataToEntity(final TableGroupDataDto tableGroupDataDto) {
        final TableGroup tableGroup = new TableGroup(tableGroupDataDto.getId());
        tableGroup.setCreatedDate(tableGroupDataDto.getCreatedDate());

        return tableGroup;
    }
}
