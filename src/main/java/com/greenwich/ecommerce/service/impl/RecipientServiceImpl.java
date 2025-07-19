package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.mapper.RecipientMapper;
import com.greenwich.ecommerce.dto.request.RecipientRequestDTO;
import com.greenwich.ecommerce.dto.response.RecipientResponseDTO;
import com.greenwich.ecommerce.entity.Recipient;
import com.greenwich.ecommerce.entity.User;
import com.greenwich.ecommerce.exception.NotFoundException;
import com.greenwich.ecommerce.repository.RecipientRepository;
import com.greenwich.ecommerce.service.RecipientService;
import com.greenwich.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipientServiceImpl implements RecipientService {

    private final RecipientRepository recipientRepository;
    private final RecipientMapper recipientMapper;
    private final UserService userService;


    @Override
    public RecipientResponseDTO createRecipient(RecipientRequestDTO requestDTO) {

        Recipient recipient = recipientMapper.toRecipient(requestDTO);
        User user = userService.getUserById(1L);
        recipient.setUser(user);
        recipientRepository.save(recipient);

        return recipientMapper.toRecipientResponse(recipient);
    }

    @Override
    public RecipientResponseDTO getRecipient(Long recipientId) {
        return recipientRepository.findById(recipientId)
                .map(recipientMapper::toRecipientResponse)
                .orElseThrow(() -> new NotFoundException("Recipient not found with ID: " + recipientId));
    }

    @Override
    public List<RecipientResponseDTO> getAllRecipients() {

        List<Recipient> recipients = recipientRepository.findAll();

        return recipients.stream()
                .map(recipientMapper::toRecipientResponse)
                .toList();
    }
}
