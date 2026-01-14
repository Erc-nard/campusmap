package com.example.campusmap

import com.google.android.gms.maps.model.LatLng
import kotlin.math.floor

data class PlaceData(
    val title: String,
    val category: String,
    val buildingCode: String? = null,
    val floor: Int? = 1,
    val locationDescription: String? = null,
    private val coordinates: LatLng? = null,
    val keywords: List<String> = listOf(),
    val description: String = "",
    val imageURL: String = ""
) {
    fun testTextQuery(query: String): Boolean {
        if (title.contains(query))
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
        title = "동맛골 (학생식당)",
        category = "식당",
        buildingCode = "E5",
        coordinates = LatLng(36.369115119669836, 127.36372948971835),
        keywords = listOf("동측식당", "동측"),
        description = "학식"
    ),
    PlaceData(
        title = "동맛골 (교직원식당)",
        category = "식당",
        buildingCode = "E5",
        floor = 2,
        coordinates = LatLng(36.369115119669836, 127.36372948971835),
        keywords = listOf("동측식당", "동측"),
        description = "학식"
    ),
    PlaceData(
        title = "서맛골",
        category = "식당",
        buildingCode = "W2",
        coordinates = LatLng(36.366930630459535, 127.36047400474106),
        keywords = listOf("서측식당", "서측"),
        description = "학식"
    ),
    PlaceData(
        title = "더큰도시락",
        category = "식당",
        buildingCode = "W2",
        floor = 2,
        coordinates = LatLng(36.36691719709483, 127.36044608597967),
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
        floor = 2,
        coordinates = LatLng(36.37304832156488, 127.36014841940174),
        description = "설렁탕, 해장국"
    ),
    PlaceData(
        title = "제순식당",
        category = "식당",
        buildingCode = "N13",
        floor = 2,
        coordinates = LatLng(36.37304832156488, 127.36014841940174),
        description = "제육볶음"
    ),
    PlaceData(
        title = "역전우동",
        category = "식당",
        buildingCode = "N13",
        floor = 2,
        coordinates = LatLng(36.37304832156488, 127.36014841940174),
        description = "우동"
    ),
    PlaceData(
        title = "풀빛마루",
        category = "식당",
        buildingCode = "N12",
        coordinates = LatLng(36.37413290617624, 127.35984139413212),
        keywords = listOf("풀마", "브리또"),
        description = "덮밥, 샐러드, 부리또"
    ),

    PlaceData(
        title = "퀴즈노스",
        category = "베이커리",
        buildingCode = "E13-1",
        floor = 2,
        coordinates = LatLng(36.37338355831737, 127.36029761797428),
        keywords = listOf("샌드위치", "큊"),
        description = "샌드위치"
    ),
    PlaceData(
        title = "써브웨이",
        category = "베이커리",
        buildingCode = "E16-1",
        coordinates = LatLng(36.371233053424405, 127.36218763964615),
        keywords = listOf("샌드위치", "서브웨이", "섭웨", "썹웨", "썹픽", "섭픽", "Subway", "subway"),
        description = "샌드위치"
    ),
    PlaceData(
        title = "캘리포니아 베이커리",
        category = "베이커리",
        buildingCode = "E6-5",
        coordinates = LatLng(36.370151, 127.363667),
        keywords = listOf("캘포", "빵집"),
        description = "빵, 커피, 차, 음료"
    ),
    PlaceData(
        title = "던킨도너츠",
        category = "베이커리",
        buildingCode = "W1",
        coordinates = LatLng(36.36590090105745, 127.36126594897014),
        description = "도넛, 커피"
    ),

    PlaceData(
        title = "주스킹",
        category = "카페",
        buildingCode = "N11",
        coordinates = LatLng(36.37363534403037, 127.35898103931018),
        description = "생과일주스, 컵과일, 커피"
    ),
    PlaceData(
        title = "카페드롭탑",
        category = "카페",
        buildingCode = "W8",
        coordinates = LatLng(36.37001683351277, 127.35983079527352),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "카페드림",
        category = "카페",
        buildingCode = "N7",
        coordinates = LatLng(36.37234765613437, 127.358651961529),
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
        floor = 2,
        coordinates = LatLng(36.374148, 127.365409),
        keywords = listOf("탐탐"),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "카페 오가다",
        category = "카페",
        buildingCode = "E9",
        floor = 2,
        coordinates = LatLng(36.369237, 127.362653),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "카페 잇",
        category = "카페",
        buildingCode = "W2-1",
        coordinates = LatLng(36.367409480330686, 127.36006671609844),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "북카페 엔젤리너스",
        category = "카페",
        buildingCode = "E13-1",
        floor = 2,
        coordinates = LatLng(36.37302455732518, 127.36055783677611),
        keywords = listOf("책", "엔제리너스"),
        description = "커피, 차, 음료"
    ),

    PlaceData(
        title = "교직원회관 매점",
        category = "매점",
        buildingCode = "E5",
        coordinates = LatLng(36.369039993453896, 127.3639854294829)
    ),
    PlaceData(
        title = "세종관 매점",
        category = "매점",
        buildingCode = "E8",
        coordinates = LatLng(36.37119330690585, 127.36709051922043)
    ),
    PlaceData(
        title = "서측 학생회관 매점",
        category = "매점",
        buildingCode = "W2",
        coordinates = LatLng(36.367363850736, 127.36100249956368)
    ),
    PlaceData(
        title = "희망·다솜관 매점",
        category = "매점",
        buildingCode = "W4-4",
        coordinates = LatLng(36.36826388854173, 127.35688099460728)
    ),
    PlaceData(
        title = "미르·나래관 매점",
        category = "매점",
        buildingCode = "W6",
        coordinates = LatLng(36.37028863681106, 127.3555837018073)
    ),
    PlaceData(
        title = "북측 학생회관 매점",
        category = "매점",
        buildingCode = "N12",
        coordinates = LatLng(36.374270282650656, 127.3598587425258)
    ),
    PlaceData(
        title = "잡화점",
        category = "매점",
        buildingCode = "N13",
        coordinates = LatLng(36.37320373800772, 127.36016027943238),
        description = "잡화, 전자제품, 학용품"
    ),

    PlaceData(
        title = "학생식당",
        category = "셔틀",
        buildingCode = null,
        floor = null,
        coordinates = LatLng(36.37345943371136, 127.3590415211297),
        description = "OLEV 교내 셔틀 기·종점"
    ),
    PlaceData(
        title = "스포츠컴플렉스",
        category = "셔틀",
        buildingCode = null,
        floor = null,
        coordinates = LatLng(36.372754444159476, 127.3619690353069),
        description = "OLEV 교내 셔틀 정류장"
    ),
    PlaceData(
        title = "창의학습관",
        category = "셔틀",
        buildingCode = null,
        floor = null,
        coordinates = LatLng(36.37078039403093, 127.36287085242897),
        description = "OLEV 교내 셔틀 정류장"
    ),
    PlaceData(
        title = "의과학연구센터",
        category = "셔틀",
        buildingCode = null,
        floor = null,
        coordinates = LatLng(36.370523689113455, 127.36578361530479),
        description = "OLEV 교내 셔틀 정류장"
    ),
    PlaceData(
        title = "클리닉",
        category = "셔틀",
        buildingCode = null,
        floor = null,
        coordinates = LatLng(36.36936551110714, 127.36949997738208),
        description = "OLEV 교내 셔틀 정류장"
    ),
    PlaceData(
        title = "나노종합기술원",
        category = "셔틀",
        buildingCode = null,
        floor = null,
        coordinates = LatLng(36.367645376452295, 127.36694849518227),
        description = "OLEV 교내 셔틀 정류장"
    ),
    PlaceData(
        title = "정문",
        category = "셔틀",
        buildingCode = null,
        floor = null,
        coordinates = LatLng(36.36629677538879, 127.36370522918322),
        description = "OLEV 교내 셔틀 정류장"
    ),
    PlaceData(
        title = "신소재공학동",
        category = "셔틀",
        buildingCode = null,
        floor = null,
        coordinates = LatLng(36.364872448655326, 127.36163446613284),
        description = "OLEV 교내 셔틀 정류장"
    ),
    PlaceData(
        title = "KISTI",
        category = "셔틀",
        buildingCode = null,
        floor = null,
        coordinates = LatLng(36.364642917929835, 127.35855531757228),
        description = "OLEV 교내 셔틀 정류장"
    ),
    PlaceData(
        title = "희망·다솜관",
        category = "셔틀",
        buildingCode = null,
        floor = null,
        coordinates = LatLng(36.36813275487686, 127.35628146520352),
        description = "OLEV 교내 셔틀 정류장"
    ),
    PlaceData(
        title = "외국인교수아파트",
        category = "셔틀",
        buildingCode = null,
        floor = null,
        coordinates = LatLng(36.371480222449705, 127.35565598479604),
        description = "OLEV 교내 셔틀 정류장"
    ),
)