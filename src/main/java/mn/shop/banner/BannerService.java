package mn.shop.banner;


import mn.shop.utils.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService {
    private final BannerRepository bannerRepository;

    public BannerService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public List<Banner> getList() {
        return bannerRepository.findAll().stream().limit(2).toList();
    }

    public List<Banner> findAll() {
        return bannerRepository.findAll();
    }

    public Banner addBanner(BannerDTO bannerDTO) {
        return bannerRepository.save(new Banner(bannerDTO));
    }

    public List<Banner> getBanner(String type) {
        return bannerRepository.getBannerByType(type);
    }

    public Banner update(Long id, BannerDTO bannerDTO) {
        Banner banner = bannerRepository.findById(id).orElseThrow(() ->
                new ValidationException("Баннер олдсонгүй"));
        banner.setUrl(bannerDTO.getUrl());
        return bannerRepository.save(banner);
    }

}
