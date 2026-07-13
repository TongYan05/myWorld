package digitalmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "ship_crew_members",autoResultMap = true)
public class Crew {
    @TableId(type = IdType.AUTO)
    private Long crewId;
    private String fullName;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Certificate> certificate;
    private String jobPosition;
    private String phone;
    private String emergencyContact;
    private String emergencyPhone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Certificate {
        private String certName;
        private String certNo;
    }
}
