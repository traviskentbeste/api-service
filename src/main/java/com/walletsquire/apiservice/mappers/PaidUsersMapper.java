package com.walletsquire.apiservice.mappers;

import com.walletsquire.apiservice.dtos.PaidDTO;
import com.walletsquire.apiservice.dtos.PaidUsersDTO;
import com.walletsquire.apiservice.entities.Paid;
import com.walletsquire.apiservice.entities.PaidUsers;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PaidUsersMapper {

    PaidUsersMapper INSTANCE = Mappers.getMapper(PaidUsersMapper.class);

    PaidUsers toEntity(PaidUsersDTO paidUsersDto);

    PaidUsersDTO toDto(PaidUsers paidUsers);

    // expression = "java(new PaidDTO().builder().id(paidUsers.getPaid().getId()).type(paidUsers.getPaid().getType()).hasExtraPenny(paidUsers.getPaid().getHasExtraPenny()).calculatedSplitAmount(paidUsers.getPaid().getCalculatedSplitAmount()).build())")
    @Mappings({
            @Mapping(target = "paid", source="paidUsers.paid", qualifiedByName = "processPaid")
    })
    PaidUsersDTO toDtoWithoutPaid(PaidUsers paidUsers);

    @Named("processPaid")
    default PaidDTO processPaid(Paid paid) {
//        System.out.println("processPaid");
        if (paid != null) {
//            System.out.println("    paid is not null");
            PaidDTO paidDTO = new PaidDTO().builder()
                    .id(paid.getId())
                    .type(paid.getType())
                    .hasExtraPenny(paid.getHasExtraPenny())
                    .calculatedSplitAmount(paid.getCalculatedSplitAmount())
                    .build();
            return paidDTO;
        }
//        System.out.println("    paid is null");

        return null;
    }
}