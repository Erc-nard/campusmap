package com.example.campusmap

data class FacilityCategory(override val id: Int, override val title: String, val items: List<FacilityItem>) : FacilityData {
    override val imageURL: String
        get() = items[0].imageURL
}
data class FacilityItem(override val id: Int, override val title: String, override val imageURL: String) : FacilityData

val cafeteria = listOf(
    FacilityItem(id = 0, title = "카이마루", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img09.jpg"),
    FacilityItem(id = 1, title = "동맛골 (학생식당)", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img03.jpg"),
    FacilityItem(id = 2, title = "동맛골 (교직원식당)", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img10.jpg"),
    FacilityItem(id = 3, title = "서맛골", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0503_img02.jpg")
)
val cafe = listOf(
    FacilityItem(id = 0, title = "그라찌에", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img01.jpg"),
    FacilityItem(id = 1, title = "카페 잇", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img02.jpg"),
    FacilityItem(id = 2, title = "드롭탑", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img03.jpg"),
    FacilityItem(id = 3, title = "카페 오가다", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img06.jpg"),
    FacilityItem(id = 4, title = "카페드림", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img08.jpg"),
    FacilityItem(id = 5, title = "주스킹", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img09.jpg"),
    FacilityItem(id = 6, title = "파스쿠찌", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0507_img10.jpg"),
    FacilityItem(id = 7, title = "탐앤탐즈", imageURL = "https://kaist.ac.kr/kr/img/campus/tom%20and%20toms.jpg"),
    FacilityItem(id = 8, title = "앤제리너스", imageURL = "https://kaist.ac.kr/site/kr/img/campus/00z7p.jpg")
)
val kiosk = listOf(
    FacilityItem(id = 0, title = "북측 학생회관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 1, title = "서측 학생회관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 2, title = "동맛골", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 3, title = "나들/여울관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 4, title = "희망/다솜관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 5, title = "미르/나래관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 6, title = "세종관", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
    FacilityItem(id = 7, title = "김병호 IT융합센터", imageURL = "https://kaist.ac.kr/kr/img/content/sub05/sub0505_img01.jpg"),
)
val topLevelFacilitiesList = listOf(
    FacilityCategory(id = 0, title = "식당", items = cafeteria),
    FacilityCategory(id = 1, title = "카페", items = cafe),
    FacilityCategory(id = 2, title = "매점", items = kiosk)
)
