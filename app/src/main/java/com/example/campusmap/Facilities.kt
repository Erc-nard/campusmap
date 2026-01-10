package com.example.campusmap

import java.time.LocalDate
import java.time.LocalTime

data class FacilityCategory(override val id: Int, override val title: String, val items: List<FacilityItem>) : FacilityData {
    override val imageURL: String
        get() = items[0].imageURL
}
data class FacilityItem(override val id: Int, override val title: String, override val imageURL: String, val details: ItemDetail) : FacilityData
data class ItemDetail(
    val businessHours: List<BusinessHours> = listOf(),
    val coordinate: Pair<Double, Double> = Pair(0.0, 0.0),
    val contact: String = "",
    val location: Location = Location(),
    val mealHours: List<MealHours> = listOf(),
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
data class Location(val buildingCode: String = "", val buildingName: String = "", val floor: Int = 1) {
    val buildingCodeString: String
        get() = if (!this.buildingCode.isEmpty()) {
            "(${this.buildingCode}) "
        } else { "" }
    val floorString: String
        get() = "${this.floor}층"
    val description: String
        get() = "${this.buildingName} ${this.buildingCodeString}${this.floorString}"
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
        location = Location("학생식당", "N11"),
        mealHours = listOf(
            MealHours("조식", LocalTime.of(8, 0), LocalTime.of(9, 0)),
            MealHours("중식", LocalTime.of(11, 20), LocalTime.of(13, 30)),
            MealHours("석식", LocalTime.of(17, 20), LocalTime.of(19, 0))
        ),
        upcomingMenus = sampleCafeteriaMenuData
    )),
    FacilityItem(id = 1, title = "서맛골", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img02.jpg", details = ItemDetail(
        location = Location("학생회관", "W2"),
        mealHours = listOf(
            MealHours("조식", LocalTime.of(8, 0), LocalTime.of(9, 30)),
            MealHours("중식", LocalTime.of(11, 30), LocalTime.of(13, 30)),
            MealHours("석식", LocalTime.of(17, 0), LocalTime.of(19, 0))
        ),
        upcomingMenus = sampleCafeteriaMenuData
    )),
    FacilityItem(id = 2, title = "동맛골 (학생식당)", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img03.jpg", details = ItemDetail(
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
)
val kaimaru = listOf(
    FacilityItem(id = 0, title = "별리달리", imageURL = "https://postfiles.pstatic.net/MjAyNDA5MjNfMjIz/MDAxNzI3MDg2Nzg2MDMx.jaVP1e5lWzFB8T2iimw7iEVnOxie13iEmXE-qabKF1Eg.-lUK1ybgxaaOLtqEMaUrAryNNDAzrzkEyY6msULTalsg.JPEG/SE-050e2149-4320-40ba-9b9c-d2e59378e737.jpg?type=w3840", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(19, 30)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(10, 30), LocalTime.of(14, 30))
        ),
        contact = "042-350-0891",
        location = Location("N11", "카이마루")
    )),
    FacilityItem(id = 1, title = "더큰식탁", imageURL = "", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(19, 0)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(11, 0), LocalTime.of(14, 0))
        ),
        contact = "042-350-0892",
        location = Location("N11", "카이마루")
    )),
    FacilityItem(id = 2, title = "리틀하노이", imageURL = "https://postfiles.pstatic.net/MjAyNTAzMjRfMTAz/MDAxNzQyNzk0ODc2NDg5.w1BK0qDz0x9me92XSq7IQprInl9lllUtzmBbOVHkCV0g.T2Svhr5rJzi5K-atLOChmm6TYb_-GOps2sC7VCeVvQkg.JPEG/SE-0D7EDD50-FB6C-4730-8A27-1FB3CBA8D2AE.jpg?type=w773", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(19, 30), orderEnd = LocalTime.of(19, 20))
        ),
        location = Location("N11", "카이마루")
    )),
    FacilityItem(id = 3, title = "캠토", imageURL = "https://mblogthumb-phinf.pstatic.net/MjAxODA5MDZfMjM5/MDAxNTM2MjM3MTc1MTQ4.7TIGmOGm2RMlwvwSxq3RPXrq6-MV6-cvbpOgSi8Rja4g.tHRPYsMwnENZe9Pq7id-BGxcRlp1oeInvz-YuskM10wg.JPEG.lpon1393/output_1420680672.jpg?type=w800", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(8, 0), LocalTime.of(19, 0)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(9, 0), LocalTime.of(14, 30))
        ),
        contact = "042-350-0872",
        location = Location("N11", "카이마루")
    )),
    FacilityItem(id = 4, title = "웰차이", imageURL = "https://postfiles.pstatic.net/MjAyNDEyMjNfMTM4/MDAxNzM0OTUxMDM4MDI5.pElZ0HZbMa62sLOFIkWlTUG4Z7JDwdxqwoBHmCqxRPIg.AKBS0tNb5fSxMGN8C7QRXLPdtl5VV6m0MhbJhK--KOog.JPEG/SE-3EFF47A9-8732-462B-A930-505DCF5549A3.jpg?type=w3840", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(11, 0), LocalTime.of(19, 0)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(11, 0), LocalTime.of(14, 30))
        ),
        contact = "042-350-0867",
        location = Location("N11", "카이마루")
    )),
    FacilityItem(id = 5, title = "오니기리와 이규동", imageURL = "https://mblogthumb-phinf.pstatic.net/MjAxOTA3MjdfNzYg/MDAxNTY0MjM0OTUwODQw.Nue2--qxzbswQ51iQZfE5pKV8_6mn8hg7Kq5jnyuumkg.2jHRJYk2a6LsnlOzE9PFVzm5TKCjBK-9ywpSTG1zjRwg.JPEG.ahnsophia/SAM_2134.jpg?type=w800", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(8, 0), LocalTime.of(19, 30)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(8, 0), LocalTime.of(17, 30))
        ),
        contact = "042-350-0874",
        location = Location("N11", "카이마루")
    )),
    FacilityItem(id = 6, title = "롤링파스타", imageURL = "", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(19, 30), orderEnd = LocalTime.of(19, 0)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(11, 0), LocalTime.of(14, 0))
        ),
        contact = "042-350-0873",
        location = Location("N11", "카이마루")
    )),
    FacilityItem(id = 7, title = "베어스타코", imageURL = "https://instagram.ficn2-2.fna.fbcdn.net/v/t51.2885-15/473759162_18027558935626714_2201949660026488566_n.jpg?stp=dst-jpg_e35_tt6&efg=eyJ2ZW5jb2RlX3RhZyI6InRocmVhZHMuQ0FST1VTRUxfSVRFTS5pbWFnZV91cmxnZW4uMTEyNXgxMTI1LnNkci5mNzU3NjEuZGVmYXVsdF9pbWFnZS5jMiJ9&_nc_ht=instagram.ficn2-2.fna.fbcdn.net&_nc_cat=110&_nc_oc=Q6cZ2QEQKHKGfCUxQXEH46qmZ4NpkTsek7dKTRLlug5Z3lsn1msx9FNQFEejqBbbaLccs0E&_nc_ohc=Dpcn2mx_U4YQ7kNvwEJ8GWP&_nc_gid=YEqdE5Zk60I34GIVWWKE8Q&edm=AKr904kBAAAA&ccb=7-5&ig_cache_key=MzU0NTY3NzA3MTE2OTE1NjA5OQ%3D%3D.3-ccb7-5&oh=00_Afo_UfSUbmh3RUUOJTF39MymLAF4J7oOFXTzeJdVeyA6tQ&oe=69679C01&_nc_sid=23467f", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(19, 30)),
            BusinessHours(setOf(DayClass.SATURDAY), false, LocalTime.of(10, 30), LocalTime.of(14, 30))
        ),
        contact = "0507-1410-1303",
        location = Location("N11", "카이마루")
    )),
)
val taeulgwan = listOf(
    FacilityItem(id = 0, title = "제순식당", imageURL = "https://mblogthumb-phinf.pstatic.net/MjAyMzExMTNfMjc1/MDAxNjk5ODgxMzQxMzI5.GzIopBneMFvEeimAaYVB5ocPLCMi99NOgJac2wQCl4gg.LWwPCAl4-KIoJcYHi1ISsgAtz87FzdzU6WjxlFJFfFUg.JPEG.sooblog23/IMG_7926.jpg?type=w400", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(20, 0), LocalTime.of(19, 30))
        ),
        contact = "042-350-0398",
        location = Location("N13", "태울관", 2)
    )),
    FacilityItem(id = 1, title = "인생설렁탕", imageURL = "https://mblogthumb-phinf.pstatic.net/MjAyMzExMTNfMTgw/MDAxNjk5ODgxMzczODMx.Rg2EYh536hW0jbKe3E8pWl7jsvfuDMSUH0xCN0bIHZMg.CZIisywUrlYyaxi9oN9crISVLDuMccpjggyt7DOMrzgg.JPEG.sooblog23/output_3722020421.jpg?type=w800", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(20, 0), LocalTime.of(19, 30))
        ),
        contact = "042-350-0398",
        location = Location("N13", "태울관", 2)
    )),
    FacilityItem(id = 2, title = "역전우동", imageURL = "https://cdn.times.kaist.ac.kr/news/photo/202402/21566_21227_158.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), false, LocalTime.of(10, 30), LocalTime.of(20, 0), LocalTime.of(19, 30))
        ),
        contact = "042-350-0398",
        location = Location("N13", "태울관", 2)
    ))
)
val cafe = listOf(
    FacilityItem(id = 0, title = "그라찌에", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img01.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), begin = LocalTime.of(8, 30), end = LocalTime.of(21, 0)),
            BusinessHours(setOf(DayClass.SATURDAY, DayClass.SUNDAY), begin = LocalTime.of(10, 0), end = LocalTime.of(19, 0))
        ),
        contact = "042-350-0896",
        location = Location("E4", "KI빌딩"),
    )),
    FacilityItem(id = 1, title = "카페 잇", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img02.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(8, 30), end = LocalTime.of(19, 0), orderEnd = LocalTime.of(18, 30))
        ),
        contact = "042-867-5156",
        location = Location("W2-1", "인터내셔널센터")
    )),
    FacilityItem(id = 2, title = "카페 드롭탑", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img03.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(8, 0), end = LocalTime.of(20, 0)),
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = true, begin = LocalTime.of(10, 0), end = LocalTime.of(18, 0))
        ),
        contact = "042-350-0890",
        location = Location("W8", "교육지원동")
    )),
    FacilityItem(id = 3, title = "카페 오가다", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img06.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(9, 30), end = LocalTime.of(19, 30)),
            BusinessHours(setOf(DayClass.SATURDAY, DayClass.SUNDAY), includeHolidays = true, begin = LocalTime.of(11, 30), end = LocalTime.of(18, 30))
        ),
        contact = "042-350-0863",
        location = Location("E9", "학술문화관", 2)
    )),
    FacilityItem(id = 4, title = "카페드림", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img08.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(9, 0), end = LocalTime.of(20, 0)),
            BusinessHours(setOf(DayClass.SATURDAY), includeHolidays = true, begin = LocalTime.of(10, 0), end = LocalTime.of(18, 0))
        ),
        contact = "042-350-0851",
        location = Location("N7", "기계공학동")
    )),
    FacilityItem(id = 5, title = "쥬스킹", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img09.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(9, 30), end = LocalTime.of(20, 0))
        ),
        contact = "042-350-0855",
        location = Location("N11", "카이마루")
    )),
    FacilityItem(id = 6, title = "파스쿠찌", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img10.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS, DayClass.SATURDAY, DayClass.SUNDAY), begin = LocalTime.of(8, 0), end = LocalTime.of(23, 0))
        ),
        contact = "042-863-8497",
        location = Location("E3", "정보전자공학동")
    )),
    FacilityItem(id = 7, title = "탐앤탐즈", imageURL = "https://kaist.ac.kr/kr/img/campus/tom%20and%20toms.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(7, 0), end = LocalTime.of(22, 0)),
            BusinessHours(setOf(DayClass.SATURDAY, DayClass.SUNDAY), includeHolidays = false, begin = LocalTime.of(10, 0), end = LocalTime.of(21, 0)),
            BusinessHours(setOf(), includeHolidays = true, begin = LocalTime.of(10, 0), end = LocalTime.of(17, 0))
        ),
        contact = "042-350-0871",
        location = Location("N1", "김병호IT융합빌딩", 2)
    )),
    FacilityItem(id = 8, title = "북카페 앤젤리너스", imageURL = "https://kaist.ac.kr/site/kr/img/campus/00z7p.jpg", details = ItemDetail(
        businessHours = listOf(
            BusinessHours(setOf(DayClass.WEEKDAYS), includeHolidays = false, begin = LocalTime.of(7, 30), end = LocalTime.of(0, 0)),
            BusinessHours(setOf(DayClass.SATURDAY, DayClass.SUNDAY), includeHolidays = true, begin = LocalTime.of(9, 0), end = LocalTime.of(23, 0))
        ),
        contact = "042-350-0856",
        location = Location("N13-1", "장영신학생회관", 2)
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
val topLevelFacilitiesList = listOf(
    FacilityCategory(id = 0, title = "학식", items = cafeteria),
    FacilityCategory(id = 1, title = "카이마루 푸드코트", items = kaimaru),
    FacilityCategory(id = 2, title = "태울관 푸드코트", items = taeulgwan),
    FacilityCategory(id = 3, title = "프렌차이즈", items = cafeteria),
    FacilityCategory(id = 4, title = "카페", items = cafe),
//    FacilityCategory(id = 5, title = "매점", items = kiosk)
)
