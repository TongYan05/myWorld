package digitalmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import digitalmanagement.entity.User;
import digitalmanagement.service.User.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = userInfoService.getOne(wrapper);
        if (user != null && user.getPassword().equals(request.getPassword())) {
            return user;
        }
        return null;
    }

    @GetMapping("/check")
    public boolean checkUsername(@RequestParam String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return !userInfoService.exists(wrapper);
    }

    @GetMapping("/list")
    public IPage<User> getUserPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder
    ) {
        if (count > 100) count = 100;
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getName, keyword)
                    .or().like(User::getEmail, keyword)
                    .or().like(User::getPhone, keyword));
        }
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        if (startDate != null && !startDate.isBlank()) {
            wrapper.ge(User::getCreateTime, startDate);
        }
        if (endDate != null && !endDate.isBlank()) {
            wrapper.le(User::getCreateTime, endDate);
        }
        applySort(wrapper, sortField, sortOrder,
                new String[]{"id", "username", "name", "createTime", "lastLoginTime", "loginFailCount"});
        return userInfoService.page(new Page<>(page, count), wrapper);
    }

    @GetMapping("/{id}")
    public User getbyid(@PathVariable Long id) {
        return userInfoService.getById(id);
    }

    @PostMapping
    public boolean add(@RequestBody User user) {
        return userInfoService.save(user);
    }

    @PostMapping("/batch")
    public boolean addbatch(@RequestBody List<User> users) {
        return userInfoService.saveBatch(users);
    }

    @PutMapping
    public boolean update(@RequestBody User user) {
        return userInfoService.updateById(user);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return userInfoService.removeById(id);
    }

    private void applySort(LambdaQueryWrapper<User> wrapper, String sortField, String sortOrder, String[] allowedFields) {
        if (sortField == null || sortField.isBlank()) return;
        boolean allowed = false;
        for (String f : allowedFields) { if (f.equals(sortField)) { allowed = true; break; } }
        if (!allowed) return;
        boolean isAsc = "asc".equalsIgnoreCase(sortOrder);
        switch (sortField) {
            case "id": wrapper.orderBy(true, isAsc, User::getId); break;
            case "username": wrapper.orderBy(true, isAsc, User::getUsername); break;
            case "name": wrapper.orderBy(true, isAsc, User::getName); break;
            case "createTime": wrapper.orderBy(true, isAsc, User::getCreateTime); break;
            case "lastLoginTime": wrapper.orderBy(true, isAsc, User::getLastLoginTime); break;
            case "loginFailCount": wrapper.orderBy(true, isAsc, User::getLoginFailCount); break;
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}