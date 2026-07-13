package digitalmanagement.inter;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import digitalmanagement.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfo extends BaseMapper<User> {
}
