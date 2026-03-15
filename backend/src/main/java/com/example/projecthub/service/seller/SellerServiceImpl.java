package com.example.projecthub.service.seller;

import com.example.projecthub.dto.seller.SellerListDTO;
import com.example.projecthub.model.User;
import com.example.projecthub.repository.ProjectRepository;
import com.example.projecthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public SellerListDTO getAllSeller(){
        List<User> sellers = userRepository.findAll();


        return null;
    }
}
