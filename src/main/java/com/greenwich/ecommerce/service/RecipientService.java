package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.RecipientRequestDTO;
import com.greenwich.ecommerce.dto.response.RecipientResponseDTO;
import com.greenwich.ecommerce.entity.Recipient;

import java.util.List;

public interface RecipientService {

    RecipientResponseDTO createRecipient(RecipientRequestDTO requestDTO);
    RecipientResponseDTO getRecipient(Long recipientId);

    List<RecipientResponseDTO> getAllRecipients();
}
