package digitalmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import digitalmanagement.entity.Crew;
import digitalmanagement.service.ShipCrew.ShipCrewService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipCrew")
public class ShipCrewController {
    @Autowired
    private ShipCrewService shipCrewService;

    @GetMapping
    public List<Crew> getAll() {
        return shipCrewService.list();
    }

    @GetMapping("/{id}")
    public Crew getById(@PathVariable Long id) {
        return shipCrewService.getById(id);
    }

    @PostMapping
    public boolean save(@RequestBody Crew crew) {
        return shipCrewService.save(crew);
    }

    @PostMapping("/batch")
    public boolean saveBatch(@RequestBody List<Crew> crewList) {
        return shipCrewService.saveBatch(crewList);
    }

    @PutMapping
    public boolean update(@RequestBody Crew crew) {
        return shipCrewService.updateById(crew);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return shipCrewService.removeById(id);
    }

    @GetMapping("/list")
    public IPage<Crew> getPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String jobPosition,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder
    ) {
        if (count > 100) count = 100;
        LambdaQueryWrapper<Crew> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(Crew::getFullName, keyword)
                    .or().like(Crew::getPhone, keyword)
                    .or().like(Crew::getJobPosition, keyword));
        }
        if (jobPosition != null && !jobPosition.isBlank()) {
            wrapper.like(Crew::getJobPosition, jobPosition);
        }
        if (startDate != null && !startDate.isBlank()) {
            wrapper.ge(Crew::getCreateTime, startDate);
        }
        if (endDate != null && !endDate.isBlank()) {
            wrapper.le(Crew::getCreateTime, endDate);
        }
        applySort(wrapper, sortField, sortOrder);
        return shipCrewService.page(new Page<>(page, count), wrapper);
    }

    private void applySort(LambdaQueryWrapper<Crew> wrapper, String sortField, String sortOrder) {
        if (sortField == null || sortField.isBlank()) return;
        String[] allowed = {"crewId", "fullName", "jobPosition", "phone", "createTime"};
        boolean ok = false;
        for (String f : allowed) { if (f.equals(sortField)) { ok = true; break; } }
        if (!ok) return;
        boolean isAsc = "asc".equalsIgnoreCase(sortOrder);
        switch (sortField) {
            case "crewId": wrapper.orderBy(true, isAsc, Crew::getCrewId); break;
            case "fullName": wrapper.orderBy(true, isAsc, Crew::getFullName); break;
            case "jobPosition": wrapper.orderBy(true, isAsc, Crew::getJobPosition); break;
            case "phone": wrapper.orderBy(true, isAsc, Crew::getPhone); break;
            case "createTime": wrapper.orderBy(true, isAsc, Crew::getCreateTime); break;
        }
    }
}
