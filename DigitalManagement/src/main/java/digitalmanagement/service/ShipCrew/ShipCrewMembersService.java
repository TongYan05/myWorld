package digitalmanagement.service.ShipCrew;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import digitalmanagement.entity.Crew;
import digitalmanagement.inter.ShipCrewMembers;
import org.springframework.stereotype.Service;

@Service
public class ShipCrewMembersService extends ServiceImpl<ShipCrewMembers, Crew> implements ShipCrewService {
}
