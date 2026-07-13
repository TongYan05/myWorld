package digitalmanagement.service.ShipVoyage;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import digitalmanagement.entity.ShipVoy;
import digitalmanagement.inter.ShipVogyage;
import org.springframework.stereotype.Service;

@Service
public class ShipVoyageService extends ServiceImpl<ShipVogyage, ShipVoy> implements ShipVoService {
}
