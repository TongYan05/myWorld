package digitalmanagement.service.ShipInformation;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import digitalmanagement.entity.ShipInfo;
import digitalmanagement.inter.ShipInformation;
import org.springframework.stereotype.Service;

@Service
public class ShipInfoService extends ServiceImpl<ShipInformation, ShipInfo> implements ShipInformationService {
}
