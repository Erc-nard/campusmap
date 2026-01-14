package com.example.campusmap

import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate
import java.time.LocalTime

data class FacilityCategory(override val id: Int, override val title: String, val items: List<FacilityItem>) : FacilityData {
    override val imageURL: String
        get() = items[0].imageURL
}
data class FacilityItem(override val id: Int, override val title: String, override val imageURL: String, val details: ItemDetail) : FacilityData
data class ItemDetail(
    val building: String? = null,
    val businessHours: List<BusinessHours> = listOf(),
    val coordinate: LatLng = LatLng(0.0, 0.0),
    val contact: String = "",
    val departmentBuildings: List<String> = listOf(),
    val location: Location = Location(),
    val mealHours: List<MealHours> = listOf(),
    val topMenus: List<String> = listOf(),
    val upcomingMenus: List<MealMenu> = listOf(),
    val url: String = ""
)
data class BusinessHours(val days: Set<DayClass>, val includeHolidays: Boolean? = null, val begin: LocalTime, val end: LocalTime, val orderEnd: LocalTime? = null) {
    val dayDescription: String
        get() = if (includeHolidays == true) {
            if (days.size == 3) {
                "매일"
            } else if (days == setOf(DayClass.WEEKDAYS)) {
                "평일·공휴일"
            } else if (days == setOf(DayClass.SATURDAY, DayClass.SUNDAY)) {
                "주말·공휴일"
            } else if (days == setOf(DayClass.SATURDAY)) {
                "토요일·공휴일"
            } else if (days == setOf(DayClass.SUNDAY)) {
                "일요일·공휴일"
            } else {
                "공휴일"
            }
        } else {
            if (days.size == 3) {
                "매일"
            } else if (days == setOf(DayClass.WEEKDAYS)) {
                "평일"
            } else if (days == setOf(DayClass.SATURDAY, DayClass.SUNDAY)) {
                "주말"
            } else if (days == setOf(DayClass.SATURDAY)) {
                "토요일"
            } else {
                "일요일"
            }
        }
    val timeDuration: String
        get() = "${this.begin}~${this.end}"
}
data class Location(val buildingCode: String = "", val buildingName: String = "", val floor: Int? = 1, val annotation: String? = null) {
    val buildingCodeString: String
        get() = if (!this.buildingCode.isEmpty()) {
            "(${this.buildingCode})"
        } else { "" }
    val floorString: String
        get() = if (this.floor == null) "" else "${this.floor}층"
    val description: String
        get() = "${this.buildingName} ${this.buildingCodeString} ${this.floorString}".trim()
}
data class MealHours(val name: String, val begin: LocalTime, val end: LocalTime) {
    val timeDuration: String
        get() = "${this.begin}~${this.end}"
}
data class MealMenu(val date: LocalDate, val mealType: MealType, val price: Int = 0, val menu: List<String>) {
    val title: String
        get() = date.toString() + " · " + mealType.description
}
enum class DayClass {
    WEEKDAYS,
    SATURDAY,
    SUNDAY
}
enum class MealType(val description: String) {
    BREAKFAST("조식"),
    LUNCH("중식"),
    LUNCHONEDISH("중식 (일품)"),
    DINNER("석식"),
    DINNERONEDISH("석식 (일품)")
}
data class TitledText(val title: String, val body: String)

val sampleCafeteriaMenuData = listOf(
    MealMenu(LocalDate.of(2026, 1, 12), MealType.BREAKFAST, 2500, listOf("밥", "국", "김치", "주찬", "부찬1", "부찬2", "부찬3")),
    MealMenu(LocalDate.of(2026, 1, 12), MealType.LUNCH, 5000, listOf("밥", "국", "김치", "주찬", "부찬1", "부찬2", "부찬3")),
    MealMenu(LocalDate.of(2026, 1, 12), MealType.DINNER, 5000, listOf("밥", "국", "김치", "주찬", "부찬1", "부찬2", "부찬3")),
    MealMenu(LocalDate.of(2026, 1, 13), MealType.BREAKFAST, 2500, listOf("밥", "국", "김치", "주찬", "부찬1", "부찬2", "부찬3")),
    MealMenu(LocalDate.of(2026, 1, 13), MealType.LUNCH, 5000, listOf("밥", "국", "김치", "주찬", "부찬1", "부찬2", "부찬3")),
    MealMenu(LocalDate.of(2026, 1, 13), MealType.DINNER, 5000, listOf("밥", "국", "김치", "주찬", "부찬1", "부찬2", "부찬3"))
)

val cafeteria = listOf(
    FacilityItem(id = 0, title = "카이마루", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img09.jpg", details = ItemDetail(
        coordinate = LatLng(36.373750, 127.359234),
        location = Location("학생식당", "N11"),
        mealHours = listOf(
            MealHours("조식", LocalTime.of(8, 0), LocalTime.of(9, 0)),
            MealHours("중식", LocalTime.of(11, 20), LocalTime.of(13, 30)),
            MealHours("석식", LocalTime.of(17, 20), LocalTime.of(19, 0))
        ),
        upcomingMenus = sampleCafeteriaMenuData
    )),
    FacilityItem(id = 1, title = "서맛골", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img02.jpg", details = ItemDetail(
        coordinate = LatLng(36.367063, 127.360462),
        location = Location("학생회관", "W2"),
        mealHours = listOf(
            MealHours("조식", LocalTime.of(8, 0), LocalTime.of(9, 30)),
            MealHours("중식", LocalTime.of(11, 30), LocalTime.of(13, 30)),
            MealHours("석식", LocalTime.of(17, 0), LocalTime.of(19, 0))
        ),
        upcomingMenus = sampleCafeteriaMenuData
    )),
    FacilityItem(id = 2, title = "동맛골 (학생식당)", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img03.jpg", details = ItemDetail(
        coordinate = LatLng(36.369180, 127.363866),
        location = Location("교직원회관", "E5"),
        mealHours = listOf(
            MealHours("조식", LocalTime.of(8, 0), LocalTime.of(10, 0)),
            MealHours("중식", LocalTime.of(11, 30), LocalTime.of(14, 0)),
            MealHours("라면", LocalTime.of(14, 0), LocalTime.of(15, 30)),
            MealHours("석식", LocalTime.of(17, 20), LocalTime.of(19, 0))
        ),
        upcomingMenus = sampleCafeteriaMenuData
    )),
    FacilityItem(id = 3, title = "동맛골 (교직원식당)", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img10.jpg", details = ItemDetail(
        coordinate = LatLng(36.369180, 127.363866),
        location = Location("교직원회관", "E5", 2),
        mealHours = listOf(
            MealHours("중식", LocalTime.of(11, 30), LocalTime.of(13, 30)),
            MealHours("석식", LocalTime.of(17, 30), LocalTime.of(19, 0))
        ),
        upcomingMenus = listOf(
            MealMenu(LocalDate.of(2026, 1, 8), MealType.LUNCH, 5000, listOf("밥", "국", "김치", "주찬", "부찬1", "부찬2", "부찬3")),
            MealMenu(LocalDate.of(2026, 1, 8), MealType.DINNER, 5000, listOf("밥", "국", "김치", "주찬", "부찬1", "부찬2", "부찬3")),
            MealMenu(LocalDate.of(2026, 1, 9), MealType.LUNCH, 6500, listOf("잡곡밥", "맑은순두부국", "삼치무조림", "옛날소시지전", "궁채들깨무침", "석박지", "그린샐러드", "누룽지", "자스민차")),
            MealMenu(LocalDate.of(2026, 1, 9), MealType.LUNCHONEDISH, 7000, listOf("모듬까스(생선까스*치킨까스)&콘타르*치플레소스", "후리가케밥", "미니온모밀", "단무지유자무침", "석박지", "그린샐러드")),
            MealMenu(LocalDate.of(2026, 1, 9), MealType.DINNER, 6500, listOf("잡곡밥", "매콤어묵국", "표고버섯불고기", "브로콜리맛살볶음", "매콤콩나물무침", "배추김치", "그린샐러드", "누룽지", "자스민차"))
        )
    )),
    FacilityItem(id = 4, title = "더큰도시락", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img11.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, LocalTime.of(10, 0), LocalTime.of(19, 0)),
        ),
        contact = "042-350-0875",
        coordinate = LatLng(36.36691719709483, 127.36044608597967),
        location = Location("학생회관–1", "W2", 2),
        topMenus = listOf("도시락", "제육볶음", "돈까스", "불고기", "볶음밥")
    )),
    FacilityItem(id = 5, title = "풀빛마루", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0508_img06.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, LocalTime.of(8, 20), LocalTime.of(18, 45), orderEnd = LocalTime.of(18, 30)),
            BusinessHours(setOf(DayClass.SATURDAY), includeHolidays = false, begin = LocalTime.of(11, 20), LocalTime.of(18, 30))
        ),
        contact = "042-350-0365",
        coordinate = LatLng(36.37413290617624, 127.35984139413212),
        location = Location("학생회관–2", "N13"),
        topMenus = listOf("덮밥", "샐러드", "부리또")
    )),
)
val kaimaru = listOf(
    FacilityItem(id = 0, title = "별리달리", imageURL = "https://postfiles.pstatic.net/MjAyNDA5MjNfMjIz/MDAxNzI3MDg2Nzg2MDMx.jaVP1e5lWzFB8T2iimw7iEVnOxie13iEmXE-qabKF1Eg.-lUK1ybgxaaOLtqEMaUrAryNNDAzrzkEyY6msULTalsg.JPEG/SE-050e2149-4320-40ba-9b9c-d2e59378e737.jpg?type=w3840", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(19, 30)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(10, 30), LocalTime.of(14, 30))
        ),
        contact = "042-350-0891",
        coordinate = LatLng(36.373712, 127.359256),
        location = Location("N11", "카이마루", annotation = "정문으로 들어가서 좌측에 있습니다."),
        topMenus = listOf("별리달리 버터알밥", "김치찌개", "뚝배기 제육덮밥")
    )),
    FacilityItem(id = 1, title = "더큰식탁", imageURL = "https://mblogthumb-phinf.pstatic.net/MjAyNDA1MTBfMTU1/MDAxNzE1MzE5MDE5NTg4.bfQx2y0ONYrSLxSrhdH_Q5hNMOc2wOuvJkUZ4h3yc8Qg.btTB9ydlT4LS4Ka5jOTKuZ2sRHw-H_fhDD795b0ylPIg.JPEG/20240509%EF%BC%BF183138.jpg?type=w800", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(19, 0)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(11, 0), LocalTime.of(14, 0))
        ),
        contact = "042-350-0892",
        coordinate = LatLng(36.373712, 127.359256),
        location = Location("N11", "카이마루", annotation = "정문으로 들어가서 좌측에 있습니다."),
        topMenus = listOf("더큰김밥", "왕돈까스", "새우튀김우동", "냉모밀소바")
    )),
    FacilityItem(id = 2, title = "리틀하노이", imageURL = "https://postfiles.pstatic.net/MjAyNTAzMjRfMTAz/MDAxNzQyNzk0ODc2NDg5.w1BK0qDz0x9me92XSq7IQprInl9lllUtzmBbOVHkCV0g.T2Svhr5rJzi5K-atLOChmm6TYb_-GOps2sC7VCeVvQkg.JPEG/SE-0D7EDD50-FB6C-4730-8A27-1FB3CBA8D2AE.jpg?type=w773", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(19, 30), orderEnd = LocalTime.of(19, 20))
        ),
        coordinate = LatLng(36.373712, 127.359256),
        location = Location("N11", "카이마루", annotation = "정문으로 들어가서 좌측에 있습니다."),
        topMenus = listOf("양지쌀국수", "마라쌀국수", "하노이분짜")
    )),
    FacilityItem(id = 3, title = "캠토", imageURL = "https://mblogthumb-phinf.pstatic.net/MjAxODA5MDZfMjM5/MDAxNTM2MjM3MTc1MTQ4.7TIGmOGm2RMlwvwSxq3RPXrq6-MV6-cvbpOgSi8Rja4g.tHRPYsMwnENZe9Pq7id-BGxcRlp1oeInvz-YuskM10wg.JPEG.lpon1393/output_1420680672.jpg?type=w800", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(8, 0), LocalTime.of(19, 0)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(9, 0), LocalTime.of(14, 30))
        ),
        contact = "042-350-0872",
        coordinate = LatLng(36.373712, 127.359256),
        location = Location("N11", "카이마루", annotation = "정문으로 들어가서 우측에 있습니다."),
        topMenus = listOf("에그 토스트", "떡갈비 토스트", "떡볶이")
    )),
    FacilityItem(id = 4, title = "웰차이", imageURL = "https://postfiles.pstatic.net/MjAyNDEyMjNfMTM4/MDAxNzM0OTUxMDM4MDI5.pElZ0HZbMa62sLOFIkWlTUG4Z7JDwdxqwoBHmCqxRPIg.AKBS0tNb5fSxMGN8C7QRXLPdtl5VV6m0MhbJhK--KOog.JPEG/SE-3EFF47A9-8732-462B-A930-505DCF5549A3.jpg?type=w3840", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(11, 0), LocalTime.of(19, 0)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(11, 0), LocalTime.of(14, 30))
        ),
        contact = "042-350-0867",
        coordinate = LatLng(36.373712, 127.359256),
        location = Location("N11", "카이마루", annotation = "정문으로 들어가서 우측에 있습니다."),
        topMenus = listOf("짜장면", "해물볶음밥", "짜장", "군만두")
    )),
    FacilityItem(id = 5, title = "오니기리와 이규동", imageURL = "https://mblogthumb-phinf.pstatic.net/MjAxOTA3MjdfNzYg/MDAxNTY0MjM0OTUwODQw.Nue2--qxzbswQ51iQZfE5pKV8_6mn8hg7Kq5jnyuumkg.2jHRJYk2a6LsnlOzE9PFVzm5TKCjBK-9ywpSTG1zjRwg.JPEG.ahnsophia/SAM_2134.jpg?type=w800", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(8, 0), LocalTime.of(19, 30)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(8, 0), LocalTime.of(17, 30))
        ),
        contact = "042-350-0874",
        coordinate = LatLng(36.373712, 127.359256),
        location = Location("N11", "카이마루", annotation = "정문으로 들어가서 좌측에 있습니다."),
        topMenus = listOf("트리플치즈규동", "사누끼우동", "떡갈비 오니기리")
    )),
    FacilityItem(id = 6, title = "롤링파스타", imageURL = "https://postfiles.pstatic.net/MjAyNTAyMThfOSAg/MDAxNzM5ODY0MjA0Mjgz.3kyL2V8bQ7Fsc_WniSHLQ3ZTKlApmyyOEVPse_7zHLEg.85ATlVi-sVunOy6dlYh4Of_f1OiUdWN9OzL3QuRM_S0g.JPEG/IMG_8653.jpg?type=w773", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(19, 30), orderEnd = LocalTime.of(19, 0)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(11, 0), LocalTime.of(14, 0))
        ),
        contact = "042-350-0873",
        coordinate = LatLng(36.373712, 127.359256),
        location = Location("N11", "카이마루"),
        topMenus = listOf("치즈듬뿍오븐파스타", "라구파스타", "베이컨김치도리아")
    )),
    FacilityItem(id = 7, title = "베어스타코", imageURL = "https://instagram.ficn2-2.fna.fbcdn.net/v/t51.2885-15/473759162_18027558935626714_2201949660026488566_n.jpg?stp=dst-jpg_e35_tt6&efg=eyJ2ZW5jb2RlX3RhZyI6InRocmVhZHMuQ0FST1VTRUxfSVRFTS5pbWFnZV91cmxnZW4uMTEyNXgxMTI1LnNkci5mNzU3NjEuZGVmYXVsdF9pbWFnZS5jMiJ9&_nc_ht=instagram.ficn2-2.fna.fbcdn.net&_nc_cat=110&_nc_oc=Q6cZ2QEQKHKGfCUxQXEH46qmZ4NpkTsek7dKTRLlug5Z3lsn1msx9FNQFEejqBbbaLccs0E&_nc_ohc=Dpcn2mx_U4YQ7kNvwEJ8GWP&_nc_gid=YEqdE5Zk60I34GIVWWKE8Q&edm=AKr904kBAAAA&ccb=7-5&ig_cache_key=MzU0NTY3NzA3MTE2OTE1NjA5OQ%3D%3D.3-ccb7-5&oh=00_Afo_UfSUbmh3RUUOJTF39MymLAF4J7oOFXTzeJdVeyA6tQ&oe=69679C01&_nc_sid=23467f", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(19, 30)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(10, 30), LocalTime.of(14, 30))
        ),
        contact = "0507-1410-1303",
        coordinate = LatLng(36.373712, 127.359256),
        location = Location("N11", "카이마루"),
        topMenus = listOf("치킨 타코", "비프 부리또", "쉬림프 치미창가")
    )),
)

val taeulgwan = listOf(
    FacilityItem(id = 0, title = "제순식당", imageURL = "https://mblogthumb-phinf.pstatic.net/MjAyMzExMTNfMjc1/MDAxNjk5ODgxMzQxMzI5.GzIopBneMFvEeimAaYVB5ocPLCMi99NOgJac2wQCl4gg.LWwPCAl4-KIoJcYHi1ISsgAtz87FzdzU6WjxlFJFfFUg.JPEG.sooblog23/IMG_7926.jpg?type=w400", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(20, 0), LocalTime.of(19, 30))
        ),
        contact = "042-350-0398",
        coordinate = LatLng(36.373033, 127.360034),
        location = Location("N13", "태울관", 2),
        topMenus = listOf("백순두부 정식", "간장제육 정식", "카레순두부 정식")
    )),
    FacilityItem(id = 1, title = "인생설렁탕", imageURL = "https://mblogthumb-phinf.pstatic.net/MjAyMzExMTNfMTgw/MDAxNjk5ODgxMzczODMx.Rg2EYh536hW0jbKe3E8pWl7jsvfuDMSUH0xCN0bIHZMg.CZIisywUrlYyaxi9oN9crISVLDuMccpjggyt7DOMrzgg.JPEG.sooblog23/output_3722020421.jpg?type=w800", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(20, 0), LocalTime.of(19, 30))
        ),
        contact = "042-350-0398",
        coordinate = LatLng(36.373033, 127.360034),
        location = Location("N13", "태울관", 2),
        topMenus = listOf("인생설렁탕", "불스지", "만두")
    )),
    FacilityItem(id = 2, title = "역전우동", imageURL = "https://cdn.times.kaist.ac.kr/news/photo/202402/21566_21227_158.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(20, 0), LocalTime.of(19, 30))
        ),
        contact = "042-350-0398",
        coordinate = LatLng(36.373033, 127.360034),
        location = Location("N13", "태울관", 2),
        topMenus = listOf("옛날우동", "냉모밀", "우볶이")
    ))
)
val franchise = listOf(
    FacilityItem(id = 0, title = "써브웨이", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0508_img05.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS, DayClass.SATURDAY, DayClass.SUNDAY), begin = LocalTime.of(8, 0), end = LocalTime.of(23, 0))
        ),
        contact = "042-863-7001",
        coordinate = LatLng(36.371329, 127.362358),
        location = Location("E16-1", "정문술빌딩"),
        topMenus = listOf("스테이크 & 치즈", "에그마요", "이탈리엔 비엠티")
    )),
    FacilityItem(id = 1, title = "오샐러드", imageURL = "https://kaist.ac.kr/kr/img/campus/00hou.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), begin = LocalTime.of(9, 0), end = LocalTime.of(20, 0), orderEnd = LocalTime.of(19, 30)),
            BusinessHours(setOf(DayClass.SATURDAY, DayClass.SUNDAY), begin = LocalTime.of(10, 0), end = LocalTime.of(19, 0))
        ),
        contact = "0507-1336-3599",
        coordinate = LatLng(36.371956, 127.359045),
        location = Location("N7-1", "원자력 및 양자공학과"),
        topMenus = listOf("채소볼", "파스타볼", "두부면랩")
    )),
    FacilityItem(id = 2, title = "캘리포니아 베이커리 & 카페", imageURL = "https://kaist.ac.kr/kr/img/campus/00zmp.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), begin = LocalTime.of(8, 0), end = LocalTime.of(21, 0)),
            BusinessHours(setOf(DayClass.SATURDAY, DayClass.SUNDAY), begin = LocalTime.of(8, 0), end = LocalTime.of(20, 0))
        ),
        contact = "042-350-0899",
        coordinate = LatLng(36.370151, 127.363667),
        location = Location("E16", "자연과학동", annotation = "본 건물과 출입구가 분리되어 있습니다."),
        topMenus = listOf("소금빵", "에스프레소", "밀크티")
    )),
    FacilityItem(id = 3, title = "퀴즈노스", imageURL = "https://kaist.ac.kr/kr/img/campus/00nfy.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS, DayClass.SATURDAY, DayClass.SUNDAY), begin = LocalTime.of(8, 0), end = LocalTime.of(22, 0))
        ),
        contact = "042-861-9987",
        coordinate = LatLng(36.373467, 127.360461),
        location = Location("N13-1", "장영신학생회관", 2, annotation = "2층 출입구 우측에 있습니다."),
        topMenus = listOf("트래디셔널 밀박스", "치킨까르보나라 샌드위치", "슈림프샐러드")
    )),
    FacilityItem(id = 4, title = "던킨도너츠", imageURL = "https://kaist.ac.kr/kr/img/campus/00ofn.png", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), begin = LocalTime.of(7, 0), end = LocalTime.of(21, 0)),
            BusinessHours(setOf(DayClass.SATURDAY, DayClass.SUNDAY), begin = LocalTime.of(8, 0), end = LocalTime.of(20, 0))
        ),
        contact = "042-350-0866",
        coordinate = LatLng(36.365662, 127.361258),
        location = Location("W1", "응용공학동"),
        topMenus = listOf("스트로베리 필드", "올리브 츄이스티", "페이머스글레이즈드")
    )),
)
val cafe = listOf(
    FacilityItem(id = 0, title = "그라찌에", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img01.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), begin = LocalTime.of(8, 30), end = LocalTime.of(21, 0)),
            BusinessHours(setOf(DayClass.SATURDAY, DayClass.SUNDAY), begin = LocalTime.of(10, 0), end = LocalTime.of(19, 0))
        ),
        contact = "042-350-0896",
        coordinate = LatLng(36.368242, 127.363863),
        location = Location("E4", "KI빌딩"),
        topMenus = listOf("민트초코 프라페", "아이스티", "허브티", "레몬콤부차")
    )),
    FacilityItem(id = 1, title = "카페 잇", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img02.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(8, 30), end = LocalTime.of(19, 0), orderEnd = LocalTime.of(18, 30))
        ),
        contact = "042-867-5156",
        coordinate = LatLng(36.367780, 127.360508),
        location = Location("W2-1", "인터내셔널센터"),
        topMenus = listOf("아메리카노", "제주녹차라떼", "곡물라떼")
    )),
    FacilityItem(id = 2, title = "카페 드롭탑", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img03.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(8, 0), end = LocalTime.of(20, 0)),
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = true, begin = LocalTime.of(10, 0), end = LocalTime.of(18, 0))
        ),
        contact = "042-350-0890",
        coordinate = LatLng(36.370207, 127.359939),
        location = Location("W8", "교육지원동"),
        topMenus = listOf("꿀 카페라떼", "유기농 말차 딸기라떼", "플레인 크로플")
    )),
    FacilityItem(id = 3, title = "카페 오가다", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img06.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(9, 30), end = LocalTime.of(19, 30)),
            BusinessHours(setOf(DayClass.SATURDAY, DayClass.SUNDAY), includeHolidays = true, begin = LocalTime.of(11, 30), end = LocalTime.of(18, 30))
        ),
        contact = "042-350-0863",
        coordinate = LatLng(36.369237, 127.362653),
        location = Location("E9", "학술문화관", 2, annotation = "문화관 쪽에 있습니다."),
        topMenus = listOf("연유라떼", "달콤밀크웨이", "콜드브루", "딥초코 라떼")
    )),
    FacilityItem(id = 4, title = "카페드림", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img08.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(9, 0), end = LocalTime.of(20, 0)),
            BusinessHours(setOf(DayClass.SATURDAY), includeHolidays = true, begin = LocalTime.of(10, 0), end = LocalTime.of(18, 0))
        ),
        contact = "042-350-0851",
        coordinate = LatLng(36.369401, 127.364430),
        location = Location("N7", "기계공학동"),
        topMenus = listOf("카페모카", "마롱라떼", "팥빙수", "초코 멜츠브레드")
    )),
    FacilityItem(id = 5, title = "쥬스킹", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img09.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(9, 30), end = LocalTime.of(20, 0))
        ),
        contact = "042-350-0855",
        coordinate = LatLng(36.373893, 127.359150),
        location = Location("N11", "카이마루", annotation = "정문으로 들어가서 좌측에 있습니다."),
        topMenus = listOf("딸기바나나쥬스", "컵과일", "당근 케이크")
    )),
    FacilityItem(id = 6, title = "파스쿠찌", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img10.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS, DayClass.SATURDAY, DayClass.SUNDAY), begin = LocalTime.of(8, 0), end = LocalTime.of(23, 0))
        ),
        contact = "042-863-8497",
        coordinate = LatLng(36.368667, 127.364606),
        location = Location("E3", "정보전자공학동"),
        topMenus = listOf("에소플 아포가또", "카페 헤이 오트", "파니니 불고기 에그")
    )),
    FacilityItem(id = 7, title = "탐앤탐스", imageURL = "https://kaist.ac.kr/kr/img/campus/tom%20and%20toms.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(7, 0), end = LocalTime.of(22, 0)),
            BusinessHours(setOf(DayClass.SATURDAY, DayClass.SUNDAY), includeHolidays = false, begin = LocalTime.of(10, 0), end = LocalTime.of(21, 0)),
            BusinessHours(setOf(), includeHolidays = true, begin = LocalTime.of(10, 0), end = LocalTime.of(17, 0))
        ),
        contact = "042-350-0871",
        coordinate = LatLng(36.374148, 127.365409),
        location = Location("N1", "김병호IT융합빌딩", 2, annotation = "건물 외부와 연결되어 있습니다."),
        topMenus = listOf("아이스 아메리카노", "흑당 버블티", "레몬 마들렌")
    )),
    FacilityItem(id = 8, title = "북카페 앤젤리너스", imageURL = "https://kaist.ac.kr/site/kr/img/campus/00z7p.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(7, 30), end = LocalTime.of(0, 0)),
            BusinessHours(setOf(DayClass.SATURDAY, DayClass.SUNDAY), includeHolidays = true, begin = LocalTime.of(9, 0), end = LocalTime.of(23, 0))
        ),
        contact = "042-350-0856",
        coordinate = LatLng(36.373434, 127.360557),
        location = Location("N13-1", "장영신학생회관", 2),
        topMenus = listOf("제주 한라봉 뱅쇼", "피스타치오 크림푸딩케이크", "카페오트")
    )),
)
//val kiosk = listOf(
//    FacilityItem(id = 0, title = "북측 학생회관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
//    FacilityItem(id = 1, title = "서측 학생회관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
//    FacilityItem(id = 2, title = "동맛골", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
//    FacilityItem(id = 3, title = "나들/여울관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
//    FacilityItem(id = 4, title = "희망/다솜관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
//    FacilityItem(id = 5, title = "미르/나래관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
//    FacilityItem(id = 6, title = "세종관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
//    FacilityItem(id = 7, title = "김병호 IT융합센터", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
//)
val departments = listOf(
    FacilityItem(
        id = 0,
        title = "수리과학과",
        imageURL = "https://mathsci.kaist.ac.kr/cms/wp-content/uploads/2022/04/MA_hori_en-ko.png",
        details = ItemDetail(
            coordinate = LatLng(36.36946653264515, 127.36448328344173),
            departmentBuildings = listOf("E2", "E2-1", "E6", "E6-1"),
            location = Location("E6-1", "수리과학과", 2),
            url = "https://mathsci.kaist.ac.kr"
        )
    ),
    FacilityItem(
        id = 1,
        title = "물리학과",
        imageURL = "https://physics.kaist.ac.kr/layouts/jit_layouts_2018/resource/images/common/logo.png",
        details = ItemDetail(
            coordinate = LatLng(36.36985742247803, 127.3641118116951),
            departmentBuildings = listOf("E6", "E6-2"),
            location = Location("E6-2", "물리학과", 1),
            url = "https://physics.kaist.ac.kr"
        )
    ),
    FacilityItem(
        id = 2,
        title = "화학과",
        imageURL = "https://chem.kaist.ac.kr/public/images/sub/sub01/campusmap/building/img01.jpg",
        details = ItemDetail(
            coordinate = LatLng(36.370444936630456, 127.36427612613731),
            departmentBuildings = listOf("E6", "E6-4", "E6-6", "E22"),
            location = Location("E6-4", "화학과", 1),
            url = "https://chem.kaist.ac.kr"
        )
    ),
    FacilityItem(
        id = 3,
        title = "전산학부",
        imageURL = "https://cs.kaist.ac.kr/upload_files/mcontent/4/202506/68621c383fbdf.png",
        details = ItemDetail(
            coordinate = LatLng(36.36802315868566, 127.3657106244192),
            departmentBuildings = listOf("E3", "E3-1", "E3-5", "N1", "N5"),
            location = Location("E3-1", "전산학부", 1),
            url = "https://cs.kaist.ac.kr"
        )
    ),
    FacilityItem(
        id = 4,
        title = "전기및전자공학부",
        imageURL = "https://ee.kaist.ac.kr/wp-content/uploads/2025/06/visual-logo06.png",
        details = ItemDetail(
            coordinate = LatLng(36.36802315868566, 127.3657106244192),
            departmentBuildings = listOf("E3", "E3-2", "E3-4", "N1"),
            location = Location("E3-2", "전기및전자공학부", 1),
            url = "https://ee.kaist.ac.kr"
        )
    )
)
val studyPlaces = listOf(
    FacilityItem(
        id = 0,
        title = "학술문화관",
        imageURL = "https://library.kaist.ac.kr/common/images/library_img_libraries.jpg",
        details = ItemDetail(
            building = "E9",
            coordinate = LatLng(36.36959433368902, 127.36246419283363),
            location = Location("E9", "학술문화관", null),
            url = "https://library.kaist.ac.kr"
        )
    ),
    FacilityItem(
        id = 1,
        title = "교양분관",
        imageURL = "https://library.kaist.ac.kr/common/images/library_img_undergraduate.jpg",
        details = ItemDetail(
            building = "N10",
            coordinate = LatLng(36.37416732341235, 127.36038202466077),
            location = Location("N10", "교양분관", null),
            url = "https://library.kaist.ac.kr"
        )
    )
)
val topLevelFacilitiesList = listOf(
    FacilityCategory(id = 0, title = "식당", items = cafeteria),
    FacilityCategory(id = 1, title = "카이마루", items = kaimaru),
    FacilityCategory(id = 2, title = "태울관", items = taeulgwan),
    FacilityCategory(id = 3, title = "프랜차이즈", items = franchise),
    FacilityCategory(id = 4, title = "카페", items = cafe),
    FacilityCategory(id = 5, title = "학과", items = departments),
    FacilityCategory(id = 6, title = "학습공간", items = studyPlaces),
//    FacilityCategory(id = 7, title = "매점", items = kiosk)
)
