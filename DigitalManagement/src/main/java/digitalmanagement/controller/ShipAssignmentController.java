package digitalmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import digitalmanagement.entity.ShipCrewAssi;
import digitalmanagement.service.ShipCrewAssignments.Shipassignments;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipAssignment")
public class ShipAssignmentController {
    @Autowired
    private Shipassignments shipassignments;

    @GetMapping
    public List<ShipCrewAssi> getAll() {
        return shipassignments.list();
    }

    @GetMapping("/{id}")
    public ShipCrewAssi getById(@PathVariable Long id) {
        return shipassignments.getById(id);
    }

    @PostMapping
    public boolean save(@RequestBody ShipCrewAssi shipCrewAssi) {
        return shipassignments.save(shipCrewAssi);
    }

    @PostMapping("/batch")
    public boolean saveBatch(@RequestBody List<ShipCrewAssi> shipCrewAssiList) {
        return shipassignments.saveBatch(shipCrewAssiList);
    }

    @PutMapping
    public boolean update(@RequestBody ShipCrewAssi shipCrewAssi) {
        return shipassignments.updateById(shipCrewAssi);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return shipassignments.removeById(id);
    }


    @GetMapping("/list")
    public IPage<ShipCrewAssi> searchByPage(
            //这里的page用来?page=1&count=10....这种
            @RequestParam(value = "page", defaultValue = "1")
            int page,
            @RequestParam(value = "count", defaultValue = "10")
            int count,
            //required=false表示这个请求参数不是必须的,如果直接@RequestParam String keyword->表示前端必须传keyword这个参数
            @RequestParam(required = false)
            String keyword,
            //我的前端想要依靠什么来查询这里就传什么参数，尽量和实体类里面的参数名保持一致
            @RequestParam(required = false)
            String jobPosition,
            @RequestParam(required = false)
            Integer shipId,
            @RequestParam(required = false)
            Integer crewId,
            @RequestParam(required = false)
            String startDate,
            @RequestParam(required = false)
            String endDate,
            @RequestParam(required = false)
            String sortField,
            @RequestParam(required = false)
            String sortOrder
    ) {
        if (count > 100) count = 100;
        LambdaQueryWrapper<ShipCrewAssi> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            //创建了一个包装器对象wrapper
            /*
            没有包装器对象，如果要写sql语句则要这么写，容易出错-》写错字段名，sql注入等风险
            * String sql = "SELECT * FROM ship_crew_assi WHERE job_position LIKE '%" + keyword + "%'";
            * */
            //wrapper.and()->我要往当前的查询语句里，追加一组用括号包起来的条件
            //keyword是用户输入的用来匹配ShipCrewAssi用getter方法获取的jobPosition字段
            wrapper.and(w -> w.like(ShipCrewAssi::getJobPosition, keyword).or().like(ShipCrewAssi::getShipId, keyword).or().like(ShipCrewAssi::getCrewId, keyword));
        }
        //这里的jobPosition是前端输入的变量，不是对象所以不用getter方法来调用
        if (jobPosition != null && !jobPosition.isBlank()) {
            wrapper.like(ShipCrewAssi::getJobPosition, jobPosition);
        }
        if (shipId != null) {
            wrapper.eq(ShipCrewAssi::getShipId, shipId);
        }
        if (crewId != null) {
            wrapper.eq(ShipCrewAssi::getCrewId, crewId);
        }
        if (startDate != null && !startDate.isBlank()) {
            wrapper.ge(ShipCrewAssi::getBoardDate, startDate);//greater or  equal
        }
        if (endDate != null && !endDate.isBlank()) {
            wrapper.le(ShipCrewAssi::getBoardDate, endDate);//less or equal
        }
        //sortField是用来指定以个变量（列）作为标准，sortOrder是用来指定排序方式的(升序或降序)
        applySort(wrapper, sortField, sortOrder);
        return shipassignments.page(new Page<>(page, count), wrapper);
    }

    private void applySort(LambdaQueryWrapper<ShipCrewAssi> wrapper, String sortField, String sortOrder) {
        //刚进入页面的时候sortField就是null，如果不设置null->return的话，有可能造成空指针异常，如果是sortField.equals(f)
        if (sortField == null || sortField.isBlank()) return;
        //前端页面提供排序的选项列
        String[] allowed = {"assignmentId", "shipId", "crewId", "jobPosition", "salary", "boardDate", "disembarkDate"};
        boolean ok = false;
        for (String f : allowed) {
            if (f.equals(sortField)) {
                ok = true;
                break;
            }
        }
        if (!ok) return;
        //equalsIgnoreCase表示忽略大小写进行比较
        boolean isAsc = "asc".equalsIgnoreCase(sortOrder);//"asc"和前端传入的sortOrder进行比较
        switch (sortField) {
            case "assignmentId":
                wrapper.orderBy(true, isAsc, ShipCrewAssi::getAssignmentId);
                //true->表示是否执行  isAsc->表示排序方式(true升序false降序)  ShipCrewAssi::getAssignmentId->表示要排序的字段
                break;
            case "shipId":
                wrapper.orderBy(true, isAsc, ShipCrewAssi::getShipId);
                break;
            case "crewId":
                wrapper.orderBy(true, isAsc, ShipCrewAssi::getCrewId);
                break;
            case "jobPosition":
                wrapper.orderBy(true, isAsc, ShipCrewAssi::getJobPosition);
                break;
            case "salary":
                wrapper.orderBy(true, isAsc, ShipCrewAssi::getSalary);
                break;
            case "boardDate":
                wrapper.orderBy(true, isAsc, ShipCrewAssi::getBoardDate);
                break;
            case "disembarkDate":
                wrapper.orderBy(true, isAsc, ShipCrewAssi::getDisembarkDate);
                break;
        }
    }
}