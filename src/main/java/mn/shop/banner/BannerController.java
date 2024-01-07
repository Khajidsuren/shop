package mn.shop.banner;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("banner")
public class BannerController {

    private BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }


    @CrossOrigin(origins = "*")
    @GetMapping("list")
    public ResponseEntity<List<Banner>> getBanner(@RequestParam String type) {
        log.info("start to get banner list by type");
        try {
            return ResponseEntity.ok(bannerService.getBanner(type));
        } catch (Exception e) {
            log.error("failed to get banner list");
            throw e;
        }
    }


    @CrossOrigin(origins = "*")
    @GetMapping("list-all")
    public ResponseEntity<List<Banner>> list() {
        log.info("start to get banner list");
        try {
            return ResponseEntity.ok(bannerService.findAll());
        } catch (Exception e) {
            log.error("failed to get banner list");
            throw e;
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("add")
    public ResponseEntity<Banner> addBanner(@RequestBody BannerDTO bannerDTO) {
        log.info("start to add banner");
        try {
            return ResponseEntity.ok(bannerService.addBanner(bannerDTO));
        } catch (Exception e) {
            log.error("failed to add banner");
            throw e;
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("update/{id:[0-9]+}")
    public ResponseEntity<Banner> update(@PathVariable Long id, @RequestBody BannerDTO bannerDTO) {
        log.info("start to update banner");
        try {
            return ResponseEntity.ok(bannerService.update(id, bannerDTO));
        } catch (Exception e) {
            log.error("failed to add banner");
            throw e;
        }
    }

}
