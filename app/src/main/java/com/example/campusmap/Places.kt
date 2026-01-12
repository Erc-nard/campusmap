package com.example.campusmap

import com.google.android.gms.maps.model.LatLng

data class PlaceData(
    val title: String,
    val category: String,
    val isBuildingItself: Boolean = false,
    val location: Location,
    val coordinates: LatLng = LatLng(0.0, 0.0),
    val keywords: List<String> = listOf(),
    val description: String = "",
    val imageURL: String = ""
)
val places = listOf(
    PlaceData(
        title = "산업경영학동",
        category = "강의동",
        isBuildingItself = true,
        location = Location("E2", "산업경영학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("산경동", "수리과", "수학과", "산시공", "산공", "산공과"),
        description = "수리과학과, 산업및시스템공학과"
    ),
    PlaceData(
        title = "정보전자공학동",
        category = "강의동",
        isBuildingItself = true,
        location = Location("E3", "정보전자공학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("전산", "전산과", "전전", "전자과"),
        description = "전산학부, 전기및전자공학부"
    ),
    PlaceData(
        title = "자연과학동",
        category = "강의동",
        isBuildingItself = true,
        location = Location("E6", "자연과학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("자과동", "수학과", "수리과", "자연과학부", "물리과", "궁리실험관", "실험"),
        description = "수리과학과, 물리학과, 화학과, 생명과학과"
    ),
    PlaceData(
        title = "창의학습관",
        category = "강의동",
        isBuildingItself = true,
        location = Location("E11", "창의학습관"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("창의관", "터만홀"),
        description = "새내기과정학부"
    ),
    PlaceData(
        title = "정문술빌딩",
        category = "강의동",
        isBuildingItself = true,
        location = Location("E16", "정문술빌딩"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("바뇌과"),
        description = "바이오및뇌공학과"
    ),
    PlaceData(
        title = "양분순빌딩",
        category = "강의동",
        isBuildingItself = true,
        location = Location("E16-1", "양분순빌딩"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("바뇌과"),
        description = "바이오및뇌공학과"
    ),
    PlaceData(
        title = "응용공학동",
        category = "강의동",
        isBuildingItself = true,
        location = Location("W1", "응용공학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("응공동", "소재과", "건환공", "생화공", "화생공", "화학생명공학과"),
        description = "신소재공학과, 건설및환경공학과, 생명화학공학과"
    ),
    PlaceData(
        title = "디지털인문사회과학부동",
        category = "강의동",
        isBuildingItself = true,
        location = Location("N4", "디지털인문사회과학부동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("교양과목", "인사동"),
        description = "디지털인문사회과학부"
    ),
    PlaceData(
        title = "기계공학동",
        category = "강의동",
        isBuildingItself = true,
        location = Location("N7", "기계공학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("기계동", "원양공", "원양과", "항공과", "기계과"),
        description = "원자력및양자공학과, 항공우주공학과, 기계공학과"
    ),

    PlaceData(
        title = "동맛골",
        category = "식당",
        location = Location("E5", "교직원회관"),
        keywords = listOf("동측식당", "동측"),
        description = "학식"
    ),
    PlaceData(
        title = "서맛골",
        category = "식당",
        location = Location("W2", "학생회관-1"),
        keywords = listOf("서측식당", "서측"),
        description = "학식"
    ),
    PlaceData(
        title = "더큰도시락",
        category = "식당",
        location = Location("W2", "학생회관-1", 2),
        keywords = listOf("서측식당", "서측"),
        description = "도시락, 제육볶음, 돈까스, 불고기, 볶음밥, 덮밥"
    ),
    PlaceData(
        title = "카이마루",
        category = "식당",
        location = Location("N11", "카이마루"),
        keywords = listOf("카마", "북측식당", "북측"),
        description = "학식, 푸드코트"
    ),
    PlaceData(
        title = "리틀하노이",
        category = "식당",
        location = Location("N11", "카이마루"),
        keywords = listOf("카마", "북측식당", "북측"),
        description = "베트남음식, 쌀국수"
    ),
    PlaceData(
        title = "별리달리",
        category = "식당",
        location = Location("N11", "카이마루"),
        keywords = listOf("카마", "북측식당", "북측"),
        description = "돈까스, 볶음밥"
    ),
    PlaceData(
        title = "웰차이",
        category = "식당",
        location = Location("N11", "카이마루"),
        keywords = listOf("카마", "북측식당", "북측"),
        description = "중식, 짜장면, 짬뽕, 탕수육"
    ),
    PlaceData(
        title = "롤링파스타",
        category = "식당",
        location = Location("N11", "카이마루"),
        keywords = listOf("카마", "북측식당", "북측"),
        description = "양식, 파스타"
    ),
    PlaceData(
        title = "인생설렁탕",
        category = "식당",
        location = Location("N13", "카이마루", 2),
        description = "설렁탕, 해장국"
    ),
    PlaceData(
        title = "제순식당",
        category = "식당",
        location = Location("N13", "카이마루", 2),
        description = "제육볶음"
    ),
    PlaceData(
        title = "역전우동",
        category = "식당",
        location = Location("N13", "카이마루", 2),
        description = "우동"
    ),

    PlaceData(
        title = "캘리포니아 베이커리",
        category = "카페",
        location = Location("E6-5", "자연과학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("캘포", "빵집"),
        description = "빵, 커피, 차, 음료"
    ),
    PlaceData(
        title = "카페드롭탑",
        category = "카페",
        location = Location("W8", "교육지원동"),
        coordinates = LatLng(0.0, 0.0),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "파스쿠찌",
        category = "카페",
        location = Location("E3", "정보전자공학동"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("파스쿠치"),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "그라찌에",
        category = "카페",
        location = Location("E4", "KI빌딩"),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("그라찌에"),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "탐앤탐스",
        category = "카페",
        location = Location("N1", "김병호IT융합빌딩", 2),
        coordinates = LatLng(0.0, 0.0),
        keywords = listOf("탐탐"),
        description = "커피, 차, 음료"
    ),
    PlaceData(
        title = "카페 오가다",
        category = "카페",
        location = Location("E9", "학술문화관", 2),
        coordinates = LatLng(0.0, 0.0),
        description = "커피, 차, 음료"
    ),

    PlaceData(
        title = "교직원회관 매점",
        category = "매점",
        location = Location("E5", "교직원회관")
    ),
    PlaceData(
        title = "서측 학생회관 매점",
        category = "매점",
        location = Location("W2", "학생회관-2")
    ),
    PlaceData(
        title = "잡화점",
        category = "매점",
        location = Location("N13", "태울관", 2),
        description = "잡화, 전자제품, 학용품"
    ),

    PlaceData(
        title = "사랑관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N14", "사랑관"),
        description = "북측 남학생 기숙사, 학부생"
    ),
    PlaceData(
        title = "소망관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N16", "소망관"),
        description = "북측 남학생 기숙사, 학부생"
    ),
    PlaceData(
        title = "성실관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N17", "성실관"),
        description = "북측 남학생 기숙사, 학부생"
    ),
    PlaceData(
        title = "진리관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N18", "진리관"),
        description = "북측 남학생 기숙사, 학부생"
    ),
    PlaceData(
        title = "아름관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N19", "아름관"),
        description = "북측 여학생 기숙사, 학부생, 체력단련실"
    ),
    PlaceData(
        title = "신뢰관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N20", "신뢰관"),
        description = "북측 남학생 기숙사, 학부생, 체력단련실"
    ),
    PlaceData(
        title = "지혜관",
        category = "기숙사",
        isBuildingItself = true,
        location = Location("N21", "지혜관"),
        description = "북측 남학생 기숙사, 학부생"
    ),
)