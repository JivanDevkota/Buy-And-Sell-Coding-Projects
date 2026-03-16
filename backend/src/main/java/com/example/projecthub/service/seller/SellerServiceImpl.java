package com.example.projecthub.service.seller;

import com.example.projecthub.dto.seller.SellerListDTO;
import com.example.projecthub.model.Role;
import com.example.projecthub.model.User;
import com.example.projecthub.repository.ProjectRepository;
import com.example.projecthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing sellers.
 * Handles seller-related operations including listing and filtering.
 * 
 * @author Project Hub Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SellerServiceImpl implements SellerService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    /**
     * Retrieves all sellers from the system.
     * Filters users with ROLE_SELLER role.
     *
     * @return SellerListDTO containing list of all sellers
     */
    @Transactional(readOnly = true)
    public SellerListDTO getAllSeller() {
        log.info("Fetching all sellers");
        try {
            List<User> sellers = userRepository.findAll().stream()
                    .filter(user -> user.getRoles() != null && 
                            user.getRoles().stream()
                                    .anyMatch(role -> "ROLE_SELLER".equals(role.getName())))
                    .collect(Collectors.toList());
            
            log.info("Found {} sellers", sellers.size());
            
            SellerListDTO sellerListDTO = new SellerListDTO();
//
//            sellerListDTO.setSellers(sellers);
            return sellerListDTO;
        } catch (Exception e) {
            log.error("Error fetching sellers: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch sellers: " + e.getMessage());
        }
    }
}
