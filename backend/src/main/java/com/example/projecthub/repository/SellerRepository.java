package com.example.projecthub.repository;

import com.example.projecthub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<User, Long> {

//    @Query("""
//            select new com.example.projecthub.dto.sller.SellerListDTO(
//                u.id,
//                u.fullName,
//                u.email,
//                u.profileImgUrl,
//                cast(count(distinct p.id) as integer ),
//                coalesce(sum (p.purchaseCount),0),
//                coalesce(sum (p.purchaseCount * p.price),0.0),
//                coalesce(avg (p.averageRating),0.0),
//                u.status
//            )
//            from User u
//            left join Project p on p.seller.id = u.id
//            where u.roles = 'SELLER'
//            group by u.id,u.fullName,u.email,u.profileImgUrl,u.status
//            order by u.fullName asc
//            """)
//    Page<SellerListDTO>findAllSellersWithStats(Pageable pageable);
}
