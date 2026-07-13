package digitalmanagement;

import digitalmanagement.entity.Crew;
import digitalmanagement.entity.ShipCrewAssi;
import digitalmanagement.entity.ShipInfo;
import digitalmanagement.entity.ShipVoy;
import digitalmanagement.service.ShipCrew.ShipCrewService;
import digitalmanagement.service.ShipCrewAssignments.Shipassignments;
import digitalmanagement.service.ShipInformation.ShipInformationService;
import digitalmanagement.service.ShipVoyage.ShipVoService;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class DigitalManagementApplicationTests {

    @Autowired
    private ShipInformationService shipInformationService;

    @Autowired
    private ShipCrewService shipCrewService;

    @Autowired
    private ShipVoService shipVoService;

    @Autowired
    private Shipassignments shipassignments;

    private static final Faker faker = new Faker(new Locale("zh-CN"));
    private static final Random random = new Random(42);

    private static final String[] SHIP_PREFIXES = {
            "远洋", "星辰", "东方", "海鹏", "蓝天", "长风", "碧波", "金龙", "凤凰", "银河",
            "太平洋", "南海", "北海", "东海", "西海", "朝阳", "明月", "青云", "飞鱼", "海鸥",
            "巨浪", "雷霆", "极光", "曙光", "凯旋", "胜利", "和平", "繁荣", "富强", "民主",
            "和谐", "自由", "正义", "勇气", "智慧", "力量", "希望", "梦想", "追求", "超越",
            "卓越", "辉煌", "灿烂", "光明", "黑暗", "黎明", "黄昏", "拂晓", "正午", "子夜"
    };
    private static final String[] SURNAMES = {
            "王", "李", "张", "刘", "陈", "杨", "赵", "黄", "周", "吴",
            "徐", "孙", "胡", "朱", "高", "林", "何", "郭", "马", "罗",
            "梁", "宋", "郑", "谢", "韩", "唐", "冯", "于", "董", "萧",
            "程", "曹", "袁", "邓", "许", "傅", "沈", "曾", "彭", "吕",
            "苏", "卢", "蒋", "蔡", "贾", "丁", "魏", "薛", "叶", "阎"
    };

    private static final String[] GIVEN_NAMES = {
            "伟", "芳", "娜", "敏", "静", "丽", "强", "磊", "洋", "勇",
            "艳", "杰", "娟", "涛", "明", "超", "秀英", "华", "慧", "建华",
            "建国", "建军", "志强", "志明", "海燕", "海涛", "晓明", "小红", "小刚", "小伟",
            "文", "斌", "辉", "峰", "鹏", "飞", "波", "军", "林", "浩",
            "鑫", "宇", "欣", "怡", "婷", "雪", "琳", "萍", "红", "丹",
            "建", "国", "平", "琴", "云", "青", "翠", "兰", "竹", "菊",
            "松", "柏", "杉", "柳", "桃", "梅", "荷", "莲", "兰", "桂",
            "天成", "志明", "国强", "家豪", "子涵", "梓轩", "浩然", "皓轩", "宇轩", "浩宇",
            "思远", "俊杰", "俊豪", "文博", "天佑", "嘉懿", "煜城", "懿轩", "烨伟", "苑博",
            "伟泽", "熠彤", "鸿煊", "烨霖", "昊强", "昊天", "聪健", "雨泽", "致远", "俊驰"
    };

    private static final String[] PORTS = {
            "上海港", "宁波舟山港", "深圳港", "广州港", "青岛港", "天津港", "大连港", "厦门港",
            "苏州港", "营口港", "连云港", "烟台港", "日照港", "南通港", "镇江港", "南京港",
            "福州港", "泉州港", "海口港", "三亚港", "威海港", "北海港", "防城港", "钦州港",
            "新加坡港", "鹿特丹港", "汉堡港", "洛杉矶港", "长滩港", "釜山港", "东京港", "横滨港",
            "神户港", "高雄港", "香港港", "迪拜港", "安特卫普港", "费利克斯托港", "勒阿弗尔港", "比雷埃夫斯港"
    };

    private static final String[] SHIP_TYPES = {"散货船", "集装箱船", "油轮", "化学品船", "LNG船", "滚装船", "客轮", "拖轮"};
    private static final String[] REGISTRY_PORTS = {"上海", "大连", "广州", "深圳", "青岛", "天津", "厦门", "宁波", "福州", "海口"};
    private static final String[] SHIPYARDS = {"江南造船", "大连船舶", "沪东中华", "外高桥造船", "广船国际", "招商局重工", "扬子江船业", "新时代造船"};
    private static final String[] COMPANIES = {"中远海运", "招商轮船", "中国海运", "长荣海运", "东方海外", "太平洋航运", "海丰国际", "安通控股"};
    private static final String[] ENGINE_MODELS = {"MAN B&W 6S50ME", "Wärtsilä 6RT-flex50", "Sulzer 6RTA48T", "MAN B&W 8G70ME", "Wärtsilä 8RT-flex68", "Doosan-MAN 9S60ME"};
    private static final String[] STATUSES = {"在航", "锚泊", "靠泊", "修船", "停航"};
    private static final String[] POSITIONS = {"船长", "大副", "二副", "三副", "轮机长", "大管轮", "二管轮", "三管轮", "水手", "机工", "厨师", "政委"};
    private static final String[] CERT_NAMES = {"海员证", "适任证书", "培训合格证", "GMDSS证书", "油轮操作证", "化学品操作证"};

    @Test
    void generateMassiveData() throws Exception {
        long totalStart = System.currentTimeMillis();
        System.out.println("========== 开始生成模拟数据 ==========");

        List<ShipInfo> ships = generateShips(10_000);
        List<Crew> crews = generateCrews(200_000);

        System.out.println("船舶和船员数据已入库，开始并行生成航次和排班数据...");

        int cpuCores = Runtime.getRuntime().availableProcessors();
        int threadCount = Math.min(cpuCores * 2, 16);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger voyageTotal = new AtomicInteger(0);
        AtomicInteger assignmentTotal = new AtomicInteger(0);

        executor.submit(() -> {
            try {
                generateVoyagesParallel(ships, 100, executor, voyageTotal, threadCount);
            } finally {
                latch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                generateAssignmentsParallel(crews, 20, executor, assignmentTotal, threadCount);
            } finally {
                latch.countDown();
            }
        });

        latch.await();
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.MINUTES);

        long totalCost = (System.currentTimeMillis() - totalStart) / 1000;
        System.out.println("========== 全部数据生成完毕 ==========");
        System.out.println("船舶: 10,000 条");
        System.out.println("船员: 200,000 条");
        System.out.println("航次: " + voyageTotal.get() + " 条");
        System.out.println("排班: " + assignmentTotal.get() + " 条");
        System.out.println("总耗时: " + totalCost + " 秒");
    }

    // ==================== 船舶数据生成 ====================
    private List<ShipInfo> generateShips(int count) {
        System.out.println("开始生成船舶数据: " + count + " 条");
        long start = System.currentTimeMillis();
        List<ShipInfo> allShips = new ArrayList<>();
        List<ShipInfo> batch = new ArrayList<>(1000);

        for (int i = 0; i < count; i++) {
            ShipInfo ship = new ShipInfo();
            ship.setShipName(SHIP_PREFIXES[i % SHIP_PREFIXES.length] + "-" + String.format("%05d", i / SHIP_PREFIXES.length) + "号");
            ship.setImo(String.format("%07d", 9100000 + i));
            ship.setRegistryPort(REGISTRY_PORTS[random.nextInt(REGISTRY_PORTS.length)]);
            ship.setShipType(SHIP_TYPES[random.nextInt(SHIP_TYPES.length)]);
            ship.setBuildYear(2000 + random.nextInt(25));
            ship.setLengthOverall(BigDecimal.valueOf(100 + random.nextInt(300)).setScale(1, RoundingMode.HALF_UP));
            ship.setBeam(BigDecimal.valueOf(15 + random.nextInt(50)).setScale(1, RoundingMode.HALF_UP));
            ship.setDraft(BigDecimal.valueOf(5 + random.nextInt(20)).setScale(1, RoundingMode.HALF_UP));
            ship.setDimensionLbd(ship.getLengthOverall() + "x" + ship.getBeam() + "x" + ship.getDraft());
            ship.setDisplacement(BigDecimal.valueOf(5000 + random.nextInt(200000)).setScale(0, RoundingMode.HALF_UP));
            ship.setMainEngineModel(ENGINE_MODELS[random.nextInt(ENGINE_MODELS.length)]);
            ship.setSailingSpeed(BigDecimal.valueOf(10 + random.nextInt(20)).setScale(1, RoundingMode.HALF_UP));
            ship.setShipyardBuilder(SHIPYARDS[random.nextInt(SHIPYARDS.length)]);
            ship.setOperatingCompany(COMPANIES[random.nextInt(COMPANIES.length)]);
            ship.setSurveyValidDate(java.time.LocalDate.of(2024 + random.nextInt(3), 1 + random.nextInt(12), 1 + random.nextInt(28)));
            ship.setShipStatus(STATUSES[random.nextInt(STATUSES.length)]);
            ship.setCreateTime(LocalDateTime.now());
            ship.setUpdateTime(LocalDateTime.now());

            batch.add(ship);
            if (batch.size() >= 1000) {
                shipInformationService.saveBatch(batch, 1000);
                allShips.addAll(batch);
                batch.clear();
                if (allShips.size() % 10000 == 0) {
                    System.out.println("船舶数据已生成: " + allShips.size() + "/" + count);
                }
            }
        }
        if (!batch.isEmpty()) {
            shipInformationService.saveBatch(batch, 1000);
            allShips.addAll(batch);
        }
        System.out.println("船舶数据生成完毕: " + allShips.size() + " 条, 耗时: " + (System.currentTimeMillis() - start) / 1000 + " 秒");
        return allShips;
    }

    // ==================== 船员数据生成 ====================
    private List<Crew> generateCrews(int count) {
        System.out.println("开始生成船员数据: " + count + " 条");
        long start = System.currentTimeMillis();
        List<Crew> allCrews = new ArrayList<>();
        List<Crew> batch = new ArrayList<>(1000);

        for (int i = 0; i < count; i++) {
            Crew crew = new Crew();
            String surname = SURNAMES[i % SURNAMES.length];
            String givenName = GIVEN_NAMES[i % GIVEN_NAMES.length];
            crew.setFullName(surname + givenName + "-" + String.format("%06d", i / (SURNAMES.length)));
            crew.setJobPosition(POSITIONS[random.nextInt(POSITIONS.length)]);

            List<Crew.Certificate> certs = new ArrayList<>(1 + random.nextInt(3));
            for (int c = 0; c < certs.size(); c++) {
                Crew.Certificate cert = new Crew.Certificate();
                cert.setCertName(CERT_NAMES[random.nextInt(CERT_NAMES.length)]);
                cert.setCertNo("CERT" + String.format("%010d", (long) i * 10 + c));
                certs.add(cert);
            }
            crew.setCertificate(certs);

            crew.setPhone("1" + (30 + random.nextInt(60)) + String.format("%08d", random.nextInt(100000000)));
            crew.setEmergencyContact(faker.name().fullName());
            crew.setEmergencyPhone("1" + (30 + random.nextInt(60)) + String.format("%08d", random.nextInt(100000000)));
            crew.setCreateTime(LocalDateTime.now());
            crew.setUpdateTime(LocalDateTime.now());

            batch.add(crew);
            if (batch.size() >= 1000) {
                shipCrewService.saveBatch(batch, 1000);
                allCrews.addAll(batch);
                batch.clear();
                if (allCrews.size() % 20000 == 0) {
                    System.out.println("船员数据已生成: " + allCrews.size() + "/" + count);
                }
            }
        }
        if (!batch.isEmpty()) {
            shipCrewService.saveBatch(batch, 1000);
            allCrews.addAll(batch);
        }
        System.out.println("船员数据生成完毕: " + allCrews.size() + " 条, 耗时: " + (System.currentTimeMillis() - start) / 1000 + " 秒");
        return allCrews;
    }

    // ==================== 航次数据并行生成 ====================
    private void generateVoyagesParallel(List<ShipInfo> ships, int perShip, ExecutorService executor, AtomicInteger total, int threadCount) {
        System.out.println("开始并行生成船舶航行记录: " + (ships.size() * perShip) + " 条");
        long start = System.currentTimeMillis();

        int totalRecords = ships.size() * perShip;
        int chunkSize = Math.max(1, ships.size() / threadCount);

        List<Future<?>> futures = new ArrayList<>();
        for (int t = 0; t < threadCount; t++) {
            int fromIndex = Math.min(t * chunkSize, ships.size());
            int toIndex = Math.min((t + 1) * chunkSize, ships.size());
            if (fromIndex >= toIndex) break;

            final int shipFrom = fromIndex;
            final int shipTo = toIndex;

            futures.add(executor.submit(() -> {
                List<ShipVoy> batch = new ArrayList<>(1000);
                int localCount = 0;
                for (int s = shipFrom; s < shipTo; s++) {
                    ShipInfo ship = ships.get(s);
                    LocalDateTime cursor = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

                    for (int v = 0; v < perShip; v++) {
                        ShipVoy voy = new ShipVoy();
                        voy.setShipId(ship.getId().intValue());
                        voy.setVoyageNo("V" + String.format("%012d", (long) s * perShip + v));
                        voy.setDeparturePort(PORTS[random.nextInt(PORTS.length)]);
                        voy.setArrivalPort(PORTS[random.nextInt(PORTS.length)]);

                        int sailingDays = 3 + random.nextInt(25);
                        int stayDays = 1 + random.nextInt(5);

                        voy.setEtd(cursor);
                        LocalDateTime ata = cursor.plusDays(sailingDays).plusHours(random.nextInt(24));
                        voy.setAta(ata);
                        voy.setCargoWeight(BigDecimal.valueOf(500 + random.nextInt(50000)).setScale(1, RoundingMode.HALF_UP));
                        voy.setCreateTime(LocalDateTime.now());
                        voy.setUpdateTime(LocalDateTime.now());

                        batch.add(voy);
                        localCount++;

                        if (batch.size() >= 1000) {
                            shipVoService.saveBatch(batch, 1000);
                            batch.clear();
                        }

                        cursor = ata.plusHours(4 + random.nextInt(48));
                    }
                }
                if (!batch.isEmpty()) {
                    shipVoService.saveBatch(batch, 1000);
                }
                int current = total.addAndGet(localCount);
                if (current % 100000 < 1000 || current >= totalRecords) {
                    System.out.println("船舶航行记录已生成: " + current + "/" + totalRecords);
                }
            }));
        }

        for (Future<?> f : futures) {
            try { f.get(); } catch (Exception e) { throw new RuntimeException(e); }
        }
        System.out.println("船舶航行记录生成完毕: " + total.get() + " 条, 耗时: " + (System.currentTimeMillis() - start) / 1000 + " 秒");
    }

    // ==================== 排班数据并行生成 ====================
    private void generateAssignmentsParallel(List<Crew> crews, int perCrew, ExecutorService executor, AtomicInteger total, int threadCount) {
        System.out.println("开始并行生成船员排班记录: " + (crews.size() * perCrew) + " 条");
        long start = System.currentTimeMillis();

        int totalRecords = crews.size() * perCrew;
        int chunkSize = Math.max(1, crews.size() / threadCount);

        List<Future<?>> futures = new ArrayList<>();
        for (int t = 0; t < threadCount; t++) {
            int fromIndex = Math.min(t * chunkSize, crews.size());
            int toIndex = Math.min((t + 1) * chunkSize, crews.size());
            if (fromIndex >= toIndex) break;

            final int crewFrom = fromIndex;
            final int crewTo = toIndex;

            futures.add(executor.submit(() -> {
                List<ShipCrewAssi> batch = new ArrayList<>(1000);
                int localCount = 0;
                for (int c = crewFrom; c < crewTo; c++) {
                    Crew crew = crews.get(c);
                    LocalDateTime cursor = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

                    for (int a = 0; a < perCrew; a++) {
                        ShipCrewAssi assi = new ShipCrewAssi();
                        assi.setShipId(1 + random.nextInt(10000));
                        assi.setCrewId(crew.getCrewId());
                        assi.setJobPosition(crew.getJobPosition());
                        assi.setSalary(BigDecimal.valueOf(5000 + random.nextInt(45000)).setScale(0, RoundingMode.HALF_UP));

                        int contractDays = 30 + random.nextInt(300);
                        int restDays = 15 + random.nextInt(60);

                        assi.setBoardDate(cursor);
                        LocalDateTime disembark = cursor.plusDays(contractDays).plusHours(random.nextInt(24));
                        assi.setDisembarkDate(disembark);
                        assi.setCreateTime(LocalDateTime.now());
                        assi.setUpdateTime(LocalDateTime.now());

                        batch.add(assi);
                        localCount++;

                        if (batch.size() >= 1000) {
                            shipassignments.saveBatch(batch, 1000);
                            batch.clear();
                        }

                        cursor = disembark.plusDays(restDays);
                    }
                }
                if (!batch.isEmpty()) {
                    shipassignments.saveBatch(batch, 1000);
                }
                int current = total.addAndGet(localCount);
                if (current % 100000 < 1000 || current >= totalRecords) {
                    System.out.println("船员排班记录已生成: " + current + "/" + totalRecords);
                }
            }));
        }

        for (Future<?> f : futures) {
            try { f.get(); } catch (Exception e) { throw new RuntimeException(e); }
        }
        System.out.println("船员排班记录生成完毕: " + total.get() + " 条, 耗时: " + (System.currentTimeMillis() - start) / 1000 + " 秒");
    }
}
