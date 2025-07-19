package com.greenwich.ecommerce.common.mapper;

import com.greenwich.ecommerce.dto.request.RecipientRequestDTO;
import com.greenwich.ecommerce.dto.response.RecipientResponseDTO;
import com.greenwich.ecommerce.entity.Recipient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface RecipientMapper {

    Recipient toRecipient(RecipientRequestDTO requestDTO);

    RecipientResponseDTO toRecipientResponse(Recipient recipient);
}
