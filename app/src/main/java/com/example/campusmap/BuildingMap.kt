package com.example.campusmap

data class BuildingMap(
    val code: String,
    val data: Map<Int, String>, // 층, 지도 URL
    val url: String = ""
) {
    val description: String
        get() = buildings[code]!!.buildingDescription
}

val buildingMaps = mapOf(
    "E3-1" to BuildingMap(
        code = "E3-1",
        data = mapOf(
            1 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map2.svg",
            2 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map3.svg",
            3 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map4.svg",
            4 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map5.svg",
        )
    ),
    "E3-5" to BuildingMap(
        code = "E3-5",
        data = mapOf(
            1 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map5.svg",
            2 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map5.svg",
            3 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map5.svg",
            4 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map5.svg",
            5 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map8.svg",
            6 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map8.svg",
        )
    ),
    "E6-4" to BuildingMap(
        code = "E6-4",
        data = mapOf(
            1 to "https://chem.kaist.ac.kr/public/images/sub/sub01/campusmap/floor-01/01.svg",
            2 to "https://chem.kaist.ac.kr/public/images/sub/sub01/campusmap/floor-01/02.svg",
            3 to "https://chem.kaist.ac.kr/public/images/sub/sub01/campusmap/floor-01/03.svg",
            4 to "https://chem.kaist.ac.kr/public/images/sub/sub01/campusmap/floor-01/04.svg",
            5 to "https://chem.kaist.ac.kr/public/images/sub/sub01/campusmap/floor-01/05.svg",
        )
    ),
    "E6-6" to BuildingMap(
        code = "E6-6",
        data = mapOf(
            1 to "https://chem.kaist.ac.kr/public/images/sub/sub01/campusmap/floor-02/01.svg",
            2 to "https://chem.kaist.ac.kr/public/images/sub/sub01/campusmap/floor-02/02.svg",
            5 to "https://chem.kaist.ac.kr/public/images/sub/sub01/campusmap/floor-02/05.svg",
            6 to "https://chem.kaist.ac.kr/public/images/sub/sub01/campusmap/floor-02/06.svg",
        )
    ),
    "E9" to BuildingMap(
        code = "E9",
        data = mapOf(
            1 to "https://library.kaist.ac.kr/common/images/kor/library_map01.jpg",
            2 to "https://library.kaist.ac.kr/common/images/kor/library_map02.jpg",
            3 to "https://library.kaist.ac.kr/common/images/kor/library_map03.jpg",
            4 to "https://library.kaist.ac.kr/common/images/kor/library_map04.jpg",
        )
    ),

    "N1" to BuildingMap(
        code = "N1",
        data = mapOf(
            1 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map9.svg",
            2 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map10.svg",
            3 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map10.svg",
            4 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map10.svg",
            5 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map10.svg",
            6 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map10.svg",
            7 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map10.svg",
            8 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map13.svg",
            9 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map13.svg",
        )
    ),
    "N5" to BuildingMap(
        code = "N5",
        data = mapOf(
            2 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map14.svg",
            3 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map15.svg",
            4 to "https://cs.kaist.ac.kr/upload_files/facility/ko/Kor_CS_Map15.svg",
        )
    ),
    "N10" to BuildingMap(
        code = "N10",
        data = mapOf(
            1 to "https://library.kaist.ac.kr/common/images/kor/library_map05.jpg",
            2 to "https://library.kaist.ac.kr/common/images/kor/library_map06.jpg",
        )
    ),
)
