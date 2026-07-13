package digitalmanagement.inter;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import digitalmanagement.entity.Crew;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShipCrewMembers extends BaseMapper<Crew> {
}
