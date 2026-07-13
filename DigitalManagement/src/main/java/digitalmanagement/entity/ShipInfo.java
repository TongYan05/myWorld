package digitalmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ship_info")
public class ShipInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String shipName;
    private String imo;
    private String registryPort;
    private String shipType;
    private Integer buildYear;
    private BigDecimal lengthOverall;
    private BigDecimal beam;
    private BigDecimal draft;
    private String dimensionLbd;
    private BigDecimal displacement;
    private String mainEngineModel;
    private BigDecimal sailingSpeed;
    private String shipyardBuilder;
    private String operatingCompany;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate surveyValidDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String shipStatus;
}
