package com.example.campusmap

import com.google.android.gms.maps.model.LatLng

data class PlaceData(
    private val title: String = "",
    val category: String,
    val isBuildingItself: Boolean = false,
    val buildingCode: String? = null,
    val floor: Int? = 1,
    val locationDescription: String? = null,
    private val coordinates: LatLng? = null,
    val keywords: List<String> = listOf(),
    val description: String = "",
    val imageURL: String = ""
) {
    fun getTitle(): String {
        return if (isBuildingItself) buildings[buildingCode]?.name ?: "" else title
    }
    fun testTextQuery(query: String): Boolean {
        if (getTitle().contains(query))
            return true
        if (category == query)
            return true
        if (keywords.any { it.contains(query) })
            return true
        if (description.contains(query))
            return true
        return false
    }
    fun getPlaceCoordinates(): LatLng? {
        if (coordinates == null) {
            if (buildingCode == null) {
                return null
            } else {
                val building = buildings[buildingCode]
                if (building == null) {
                    return null
                } else {
                    return building.coordinates
                }
            }
        } else {
            return coordinates
        }
    }
}
val places = listOf(
    PlaceData(
        title = "산업경영학동",
        category = "강의동",
        isBuildingItself = true,
        buildingCode = "E2",
        keywords = listOf("산경동", "수리과", "수학과", "산시공", "산공", "산공과"),
        description = "수리과학과, 산업및시스템공학과"
    ),
    PlaceData(
        title = "정보전자공학동",
        category = "강의동",
        isBuildingItself = true,
        buildingCode = "E3",
        keywords = listOf("전산", "전산과", "전전", "전자과"),
        description = "전산학부, 전기및전자공학부"
    ),
    PlaceData(
        title = "자연과학동",
        category = "강의동",
        isBuildingItself = true,
        buildingCode = "E6",
        keywords = listOf("자과동", "수학과", "수리과", "자연과학부", "물리과", "궁리실험관", "실험"),
        description = "수리과학과, 물리학과, 화학과, 생명과학과"
    ),
    PlaceData(
        title = "창의학습관",
        category = "강의동",
        isBuildingItself = true,
        buildingCode = "E11",
        keywords = listOf("창의관", "터만홀"),
        description = "새내기과정학부"
    ),
    PlaceData(
        title = "정문술빌딩",
        category = "강의동",
        isBuildingItself = true,
        buildingCode = "E16",
        keywords = listOf("바뇌과"),
        description = "바이오및뇌공학과"
    ),
    PlaceData(
        title = "양분순빌딩",
        category = "강의동",
        isBuildingItself = true,
        buildingCode = "E16-1",
        keywords = listOf("바뇌과"),
        description = "바이오및뇌공학과"
    ),
    PlaceData(
        title = "응용공학동",
        category = "강의동",
        isBuildingItself = true,
        buildingCode = "W1",
        keywords = listOf("응공동", "소재과", "건환공", "생화공", "화생공", "화학생명공학과"),
        description = "신소재공학과, 건설및환경공학과, 생명화학공학과"
    ),
    PlaceData(
        title = "디지털인문사회과학부동",
        category = "강의동",
        isBuildingItself = true,
        buildingCode = "N4",
        keywords = listOf("교양과목", "인사동"),
        description = "디지털인문사회과학부"
    ),
    PlaceData(
        title = "기계공학동",
        category = "강의동",
        isBuildingItself = true,
        buildingCode = "N7",
        keywords = listOf("기계동", "원양공", "원양과", "항공과", "기계과"),
        description = "원자력및양자공학과, 항공우주공학과, 기계공학과"
    ),

    PlaceData(
        title = "동맛골",
        category = "식당",
        buildingCode = "E5",
        keywords = listOf("동측식당", "동측"),
        description = "학식"
    ),
    PlaceData(
        title = "서맛골",
        category = "식당",
        buildingCode = "W2",
        keywords = listOf("서측식당", "서측"),
        description = "학식"
    ),
    PlaceData(
        title = "더큰도시락",
        category = "식당",
        buildingCode = "W2",
        keywords = listOf("서측식당", "서측"),
        description = "도시락, 제육볶음, 돈까스, 불고기, 볶음밥, 덮밥"
    ),
    PlaceData(
        title = "카이마루",
        category = "식당",
        buildingCode = "N11",
        keywords = listOf("카마", "북측식당", "북측"),
        coordinates = LatLng(36.373656, 127.359367),
        description = "학식, 푸드코트"
    ),
    PlaceData(
        title = "리틀하노이",
        category = "식당",
        buildingCode = "N11",
        coordinates = LatLng(36.373656, 127.359367),
        keywords = listOf("카마", "북측식당", "북측"),
        description = "베트남음식, 쌀국수"
    ),
    PlaceData(
        title = "별리달리",
        category = "식당",
        buildingCode = "N11",
        coordinates = LatLng(36.373656, 127.359367),
        keywords = listOf("카마", "북측식당", "북측"),
        description = "돈까스, 볶음밥"
    ),
    PlaceData(
        title = "웰차이",
        category = "식당",
        buildingCode = "N11",
        coordinates = LatLng(36.373656, 127.359367),
        keywords = listOf("카마", "북측식당", "북측"),
        description = "중식, 짜장면, 짬뽕, 탕수육"
    ),
    PlaceData(
        title = "롤링파스타",
        category = "식당",
        buildingCode = "N11",
        coordinates = LatLng(36.373656, 127.359367),
        keywords = listOf("카마", "북측식당", "북측"),
        description = "양식, 파스타"
    ),
    PlaceData(
        title = "인생설렁탕",
        category = "식당",
        buildingCode = "N13",
        description = "설렁탕, 해장국"
    ),
    PlaceData(
        title = "제순식당",
        category = "식당",
        buildingCode = "N13",
        description = "제육볶음"
    ),
    PlaceData(
        title = "역전우동",
        category = "식당",
        buildingCode = "N13",
        description = "우동"
    ),

    PlaceData(
        title = "캘리포니아 베이커리",
        category = "카페",
        buildingCode = "E6-5",
        coordinates = LatLng(36.370151, 127.363667),
        keywords = listOf("캘포", "빵집"),
        description = "빵, 커피, 차, 음료"
    ),
    PlaceData(
        title = "카페드롭탑",
        category = "카페",
        buildingCode = "W8",
        coordinates = LatLng(36.370207, 127.359939),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "파스쿠찌",
        category = "카페",
        buildingCode = "E3",
        coordinates = LatLng(36.368667, 127.364606),
        keywords = listOf("파스쿠치"),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "그라찌에",
        category = "카페",
        buildingCode = "E4",
        coordinates = LatLng(36.368242, 127.363863),
        keywords = listOf("그라찌에"),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "탐앤탐스",
        category = "카페",
        buildingCode = "N1",
        coordinates = LatLng(36.374148, 127.365409),
        keywords = listOf("탐탐"),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "카페 오가다",
        category = "카페",
        buildingCode = "E9",
        coordinates = LatLng(36.369237, 127.362653),
        description = "커피, 차, 음료"
    ),

    PlaceData(
        title = "교직원회관 매점",
        category = "매점",
        buildingCode = "E5"
    ),
    PlaceData(
        title = "서측 학생회관 매점",
        category = "매점",
        buildingCode = "W2"
    ),
    PlaceData(
        title = "잡화점",
        category = "매점",
        buildingCode = "N13",
        description = "잡화, 전자제품, 학용품"
    ),

    PlaceData(
        title = "사랑관",
        category = "기숙사",
        isBuildingItself = true,
        buildingCode = "N14",
        description = "북측 남학생 기숙사, 학부생"
    ),
    PlaceData(
        title = "소망관",
        category = "기숙사",
        isBuildingItself = true,
        buildingCode = "N16",
        description = "북측 남학생 기숙사, 학부생"
    ),
    PlaceData(
        title = "성실관",
        category = "기숙사",
        isBuildingItself = true,
        buildingCode = "N17",
        description = "북측 남학생 기숙사, 학부생"
    ),
    PlaceData(
        title = "진리관",
        category = "기숙사",
        isBuildingItself = true,
        buildingCode = "N18",
        description = "북측 남학생 기숙사, 학부생"
    ),
    PlaceData(
        title = "아름관",
        category = "기숙사",
        isBuildingItself = true,
        buildingCode = "N19",
        description = "북측 여학생 기숙사, 학부생, 체력단련실"
    ),
    PlaceData(
        title = "신뢰관",
        category = "기숙사",
        isBuildingItself = true,
        buildingCode = "N20",
        description = "북측 남학생 기숙사, 학부생, 체력단련실"
    ),
    PlaceData(
        title = "지혜관",
        category = "기숙사",
        isBuildingItself = true,
        buildingCode = "N21",
        description = "북측 남학생 기숙사, 학부생"
    ),
)