package com.example.campusmap   // ← MainActivity랑 같은 패키지

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoMapSdk.init(this, "fca9ed231cef34a2a39ab6293c59720f")
    }
}
