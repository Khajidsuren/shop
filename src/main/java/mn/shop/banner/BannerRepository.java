package mn.shop.banner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query("select b from Banner as b order by b.id desc")
    List<Banner> findAll();

    @Query("select b from Banner as b where b.type = :type order by b.id desc")
    List<Banner> getBannerByType(String type);


}
