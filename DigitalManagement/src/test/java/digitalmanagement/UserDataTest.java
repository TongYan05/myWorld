package digitalmanagement;

import digitalmanagement.entity.User;
import digitalmanagement.service.ShipCrew.ShipCrewService;
import digitalmanagement.service.ShipCrewAssignments.Shipassignments;
import digitalmanagement.service.ShipInformation.ShipInformationService;
import digitalmanagement.service.ShipVoyage.ShipVoService;
import digitalmanagement.service.User.UserInfoService;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SpringBootTest
public class UserDataTest {

    @Autowired
    private UserInfoService userInfoService;

    private static final Faker faker = new Faker(new Locale("zh-CN"));
    private static final Random random = new Random();

    // 1. 准备200个常见姓氏
    private static final String[] SURNAMES = {
            "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
            "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章",
            "云", "苏", "潘", "葛", "奚", "范", "彭", "郎", "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳",
            "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常",
            "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和", "穆", "萧", "尹",
            "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞",
            "熊", "纪", "舒", "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危",
            "江", "童", "颜", "郭", "梅", "盛", "林", "刁", "钟", "徐", "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍",
            "虞", "万", "支", "柯", "昝", "管", "卢", "莫", "经", "房", "裘", "缪", "干", "解", "应", "宗", "丁", "宣", "贲", "邓",
            "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉", "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁",
            "荀", "羊", "於", "惠", "甄", "曲", "家", "封", "芮", "羿", "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫",
            "乌", "焦", "巴", "弓", "牧", "隗", "山", "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫",
            "宁", "仇", "栾", "暴", "甘", "钭", "厉", "戎", "祖", "武", "符", "刘", "景", "詹", "束", "龙", "叶", "幸", "司", "韶",
            "郜", "黎", "蓟", "薄", "印", "宿", "白", "怀", "蒲", "邰", "从", "鄂", "索", "咸", "籍", "赖", "卓", "蔺", "屠", "蒙",
            "池", "乔", "阴", "郁", "胥", "能", "苍", "双", "闻", "莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵",
            "冉", "宰", "郦", "雍", "却", "璩", "桑", "桂", "濮", "牛", "寿", "通", "边", "扈", "燕", "冀", "郏", "浦", "尚", "农",
            "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹", "习", "宦", "艾", "鱼", "容", "向", "古", "易", "慎",
            "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘", "匡", "国", "文", "寇", "广", "禄", "阙", "东",
            "殴", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩", "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚",
            "那", "简", "饶", "空", "曾", "毋", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关", "蒯", "相", "查", "后", "荆", "红"
    };

    // 2. 准备500个常见名字
    private static final String[] GIVEN_NAMES = {
            "伟", "芳", "娜", "秀英", "敏", "静", "丽", "强", "磊", "军", "洋", "勇", "艳", "杰", "娟", "涛", "明", "超", "秀兰", "霞",
            "平", "刚", "桂英", "文", "华", "飞", "玲", "芬", "莉", "斌", "宇", "浩", "凯", "鹏", "帅", "倩", "婷", "博", "鑫", "蕾",
            "建", "欣", "琳", "颖", "波", "亮", "辉", "俊", "宁", "琴", "峰", "彬", "丹", "旭", "冬", "浩", "云", "亮", "健", "林",
            "海", "燕", "蓉", "帆", "晨", "佳", "冰", "程", "嘉", "琦", "晶", "婷", "欢", "欢", "敏", "洁", "芳", "萍", "红", "梅",
            "春", "兰", "菊", "竹", "梅", "松", "柏", "柳", "桃", "李", "杏", "梨", "枣", "柿", "橘", "柚", "橙", "柠", "檬", "樱",
            "萱", "瑶", "瑾", "瑜", "璇", "璐", "璧", "环", "璎", "珞", "珠", "宝", "玉", "珍", "珊", "瑚", "琥", "珀", "玛", "瑙",
            "翠", "翡", "翠", "绿", "青", "蓝", "紫", "黛", "黑", "白", "灰", "银", "金", "铜", "铁", "锡", "铅", "锌", "铝", "镁",
            "天", "地", "日", "月", "星", "辰", "风", "云", "雷", "雨", "雪", "霜", "露", "雾", "霾", "虹", "霞", "光", "影", "暗",
            "东", "南", "西", "北", "中", "上", "下", "左", "右", "前", "后", "内", "外", "高", "低", "长", "短", "宽", "窄", "深",
            "浅", "远", "近", "大", "小", "多", "少", "新", "旧", "老", "幼", "男", "女", "阴", "阳", "刚", "柔", "强", "弱", "快",
            "慢", "早", "晚", "迟", "速", "急", "缓", "稳", "乱", "齐", "散", "聚", "分", "合", "开", "闭", "起", "落", "升", "降",
            "生", "死", "存", "亡", "有", "无", "真", "假", "虚", "实", "动", "静", "行", "止", "进", "退", "来", "去", "回", "转",
            "思", "想", "念", "忘", "记", "忆", "梦", "幻", "觉", "醒", "愁", "悲", "喜", "怒", "哀", "乐", "惊", "恐", "忧", "惧",
            "爱", "恨", "情", "仇", "恩", "怨", "亲", "疏", "远", "近", "冷", "热", "温", "凉", "寒", "暑", "干", "湿", "香", "臭",
            "甜", "苦", "酸", "辣", "咸", "淡", "浓", "稀", "稠", "薄", "厚", "重", "轻", "软", "硬", "坚", "脆", "韧", "滑", "涩",
            "粗", "细", "圆", "方", "扁", "平", "凸", "凹", "直", "曲", "弯", "折", "断", "连", "接", "续", "绝", "尽", "穷", "极",
            "初", "末", "始", "终", "先", "后", "前", "后", "古", "今", "中", "外", "华", "夏", "神", "仙", "佛", "魔", "妖", "鬼",
            "龙", "凤", "虎", "豹", "狼", "熊", "鹰", "鹤", "龟", "蛇", "鱼", "虫", "鸟", "兽", "草", "木", "花", "叶", "根", "茎",
            "山", "川", "河", "海", "湖", "泉", "溪", "潭", "渊", "瀑", "崖", "谷", "峰", "岭", "丘", "陵", "原", "野", "林", "森",
            "田", "地", "土", "沙", "石", "玉", "金", "银", "铜", "铁", "锡", "铅", "锌", "铝", "镁", "钙", "钾", "钠", "氯", "硫",
            "氧", "氢", "碳", "氮", "磷", "硅", "硼", "氟", "氖", "氩", "氪", "氙", "氡", "镭", "铀", "钚", "镅", "锔", "锫", "锎",
            "锿", "镄", "钔", "锘", "铹", "𬬻", "𬭊", "𬭳", "𬭶", "𬭸", "鿏", "𬭟", "𬭠", "𬭡", "𬭢", "𬭣", "𬭤", "𬭥", "𬭦", "𬭧",
            "𬭨", "𬭩", "𬭪", "𬭫", "𬭬", "𬭭", "𬭮", "𬭯", "𬭰", "𬭱", "𬭲", "𬭳", "𬭴", "𬭵", "𬭶", "𬭷", "𬭸", "𬭹", "𬭺", "𬭻",
            "国", "家", "人", "民", "族", "群", "众", "党", "团", "队", "组", "织", "机", "构", "单", "位", "企", "业", "公", "司",
            "学", "校", "院", "所", "局", "部", "委", "办", "处", "科", "室", "班", "级", "年", "月", "日", "时", "分", "秒", "刻",
            "春", "夏", "秋", "冬", "季", "节", "候", "气", "温", "度", "风", "力", "雨", "量", "雪", "深", "雾", "浓", "云", "层",
            "雷", "声", "闪", "电", "冰", "雹", "霜", "冻", "露", "水", "气", "压", "湿", "度", "能", "见", "降", "水", "蒸", "发",
            "日", "照", "紫", "外", "辐", "射", "地", "磁", "引", "力", "重", "力", "离", "心", "向", "心", "浮", "力", "阻", "力",
            "摩", "擦", "弹", "性", "塑", "性", "张", "力", "压", "力", "剪", "切", "扭", "曲", "弯", "折", "拉", "伸", "挤", "压"
    };

    // 3. 准备真实的手机号段
    private static final String[] PHONE_PREFIXES = {
            "130", "131", "132", "133", "134", "135", "136", "137", "138", "139",
            "150", "151", "152", "153", "155", "156", "157", "158", "159",
            "170", "171", "172", "173", "175", "176", "177", "178",
            "180", "181", "182", "183", "184", "185", "186", "187", "188", "189",
            "190", "191", "193", "195", "196", "197", "198", "199"
    };

    @Test
    void generateUserData() {
        System.out.println("========== 开始生成用户数据 ==========");
        long start = System.currentTimeMillis();

        int userCount = 1_000_000;
        List<User> users = new ArrayList<>(1000);

        for (int i = 0; i < userCount; i++) {
            User user = new User();

            // 用户名：使用随机生成的中文姓名拼音或固定格式
            user.setUsername("user_" + String.format("%08d", i));
            user.setPassword("123456");

            // 随机组合姓名
            String fullName = SURNAMES[random.nextInt(SURNAMES.length)] + GIVEN_NAMES[random.nextInt(GIVEN_NAMES.length)];
            user.setName(fullName);

            user.setEmail(faker.internet().emailAddress());

            // 生成真实的手机号
            String phone = PHONE_PREFIXES[random.nextInt(PHONE_PREFIXES.length)]
                    + String.format("%08d", random.nextInt(100000000));
            user.setPhone(phone);

            user.setStatus(1);
            user.setLoginFailCount(0);
            user.setLastLoginIp(faker.internet().ipV4Address());
            user.setLastLoginTime(LocalDateTime.now().minusDays(random.nextInt(30)));
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            user.setIsDeleted(0);

            users.add(user);

            if (users.size() >= 1000) {
                userInfoService.saveBatch(users);
                users.clear();
                if ((i + 1) % 100000 == 0) {
                    System.out.println("已生成用户: " + (i + 1) + "/" + userCount);
                }
            }
        }

        if (!users.isEmpty()) {
            userInfoService.saveBatch(users);
        }

        long cost = (System.currentTimeMillis() - start) / 1000;
        System.out.println("========== 用户数据生成完毕 ==========");
        System.out.println("总计生成: " + userCount + " 条");
        System.out.println("耗时: " + cost + " 秒");
    }

    // ... 上面是你原来的 generateUserData 方法 ...


    @Autowired
    private ShipInformationService shipInformationService;

    @Autowired
    private ShipCrewService shipCrewService;

    @Autowired
    private ShipVoService shipVoService;

    @Autowired
    private Shipassignments shipassignments;



    /**
     * 这是一个新的测试方法，用来查询数据量
     * 注意：这个方法必须写在 public class UserDataTest { ... } 的大括号里面
     */
    @Test
    void checkTotalDataCount() {
        System.out.println("正在查询数据库总人数...");

        // 调用 Service 的 count 方法
        long count1 = shipInformationService.count();
        long count2 = shipCrewService.count();
        long count3 = shipVoService.count();
        long count4 = shipassignments.count();
        long count5 = userInfoService.count();
        long sum = count1 + count2 + count3 + count4 + count5;

        System.out.println("==========================================");
        System.out.println("当前 ship_info 表中的总数据量为: " + count1 + " 条");
        System.out.println("当前 ship_crew_members 表中的总数据量为: " + count2 + " 条");
        System.out.println("当前 ship_voyages 表中的总数据量为: " + count3 + " 条");
        System.out.println("当前 ship_crew_assignments 表中的总数据量为: " + count4 + " 条");
        System.out.println("当前 user_info 表中的总数据量为: " + count5 + " 条");
        System.out.println("当前 ship 库中的总数据量为: " + sum + " 条");
        System.out.println("==========================================");
    }

} // <--- 确保你的新方法是在这个最终的大括号之前



