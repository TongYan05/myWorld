package digitalmanagement.service.User;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import digitalmanagement.entity.User;
import digitalmanagement.inter.UserInfo;
import org.springframework.stereotype.Service;

@Service
public class UserInformationService extends ServiceImpl<UserInfo, User> implements UserInfoService {
}
