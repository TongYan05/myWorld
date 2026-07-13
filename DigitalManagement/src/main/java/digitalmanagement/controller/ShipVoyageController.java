package digitalmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import digitalmanagement.entity.ShipVoy;
import digitalmanagement.service.ShipVoyage.ShipVoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipVoyage")
public class ShipVoyageController {
    @Autowired
    private ShipVoService shipvoservice;

    @GetMapping
    public List<ShipVoy> getall() {
        return shipvoservice.list();
    }

    @GetMapping("/{id}")
    public ShipVoy getById(@PathVariable Long id) {
        return shipvoservice.getById(id);
    }

    @PostMapping
    public boolean save(@RequestBody ShipVoy shipVoy) {
        return shipvoservice.save(shipVoy);
    }

    @PostMapping("/batch")
    public boolean saveBatch(@RequestBody List<ShipVoy> shipVoyList) {
        return shipvoservice.saveBatch(shipVoyList);
    }

    @PutMapping
    public boolean update(@RequestBody ShipVoy shipVoy) {
        return shipvoservice.updateById(shipVoy);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return shipvoservice.removeById(id);
    }

    @GetMapping("/list")
    public IPage<ShipVoy> GetByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String departurePort,
            @RequestParam(required = false) String arrivalPort,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder
    ) {
        if (count > 100) count = 100;
        LambdaQueryWrapper<ShipVoy> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(ShipVoy::getVoyageNo, keyword)
                    .or().like(ShipVoy::getShipId, keyword));
        }
        if (departurePort != null && !departurePort.isBlank()) {
            wrapper.like(ShipVoy::getDeparturePort, departurePort);
        }
        if (arrivalPort != null && !arrivalPort.isBlank()) {
            wrapper.like(ShipVoy::getArrivalPort, arrivalPort);
        }
        if (startDate != null && !startDate.isBlank()) {
            wrapper.ge(ShipVoy::getEtd, startDate);
        }
        if (endDate != null && !endDate.isBlank()) {
            wrapper.le(ShipVoy::getEtd, endDate);
        }
        applySort(wrapper, sortField, sortOrder);
        return shipvoservice.page(new Page<>(page, count), wrapper);
    }

    private void applySort(LambdaQueryWrapper<ShipVoy> wrapper, String sortField, String sortOrder) {
        if (sortField == null || sortField.isBlank()) return;
        String[] allowed = {"voyageId", "shipId", "voyageNo", "departurePort", "arrivalPort", "etd", "ata", "cargoWeight"};
        boolean ok = false;
        for (String f : allowed) { if (f.equals(sortField)) { ok = true; break; } }
        if (!ok) return;
        boolean isAsc = "asc".equalsIgnoreCase(sortOrder);
        switch (sortField) {
            case "voyageId": wrapper.orderBy(true, isAsc, ShipVoy::getVoyageId); break;
            case "shipId": wrapper.orderBy(true, isAsc, ShipVoy::getShipId); break;
            case "voyageNo": wrapper.orderBy(true, isAsc, ShipVoy::getVoyageNo); break;
            case "departurePort": wrapper.orderBy(true, isAsc, ShipVoy::getDeparturePort); break;
            case "arrivalPort": wrapper.orderBy(true, isAsc, ShipVoy::getArrivalPort); break;
            case "etd": wrapper.orderBy(true, isAsc, ShipVoy::getEtd); break;
            case "ata": wrapper.orderBy(true, isAsc, ShipVoy::getAta); break;
            case "cargoWeight": wrapper.orderBy(true, isAsc, ShipVoy::getCargoWeight); break;
        }
    }
}