package com.example.projecthub.service.purchase;

import com.example.projecthub.dto.purchase.PurchaseDTO;
import com.example.projecthub.model.Project;
import com.example.projecthub.model.Purchase;
import com.example.projecthub.model.PurchaseStatus;
import com.example.projecthub.model.User;
import com.example.projecthub.repository.ProjectFileRepository;
import com.example.projecthub.repository.ProjectRepository;
import com.example.projecthub.repository.PurchaseRepository;
import com.example.projecthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectFileRepository projectFileRepository;

   public PurchaseDTO purchaseProject(PurchaseDTO purchaseDTO) {
        User user = userRepository.findById(purchaseDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("user not found"));
        Project project = projectRepository.findById(purchaseDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("project not found"));
        Purchase purchase = new Purchase();
        purchase.setBuyer(user);
        purchase.setProject(project);
        purchase.setStatus(PurchaseStatus.PENDING);
        purchase.setTransactionId(UUID.randomUUID().toString());
        if (user.getBalance() < purchase.getProject().getPrice()) {
            throw new RuntimeException("not enough money");
        }

        purchase.setPaidAmount(project.getPrice());
        Purchase save = purchaseRepository.save(purchase);
        return PurchaseDTO.toDTO(save);
    }
}
