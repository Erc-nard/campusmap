package com.example.campusmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BusinessHoursView(businessHoursData: List<BusinessHours>) {
    val allDays = setOf(
        DayClass.WEEKDAYS,
        DayClass.SATURDAY,
        DayClass.SUNDAY
    )
    val determinedDays =
        businessHoursData.map { businessHours -> businessHours.days }
            .flatten().toSet()
    val undeterminedDays = allDays.subtract(determinedDays)
    val holidays =
        businessHoursData.map { businessHours -> businessHours.includeHolidays }
    val isHolidaysDetermined =
        holidays.contains(true) || holidays.contains(null)
    DetailView("영업 시간") { innerPadding ->
        Column(
            modifier = Modifier.padding(horizontal = innerPadding)
        ) {
            businessHoursData.forEach { businessHours ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = businessHours.dayDescription,
                        modifier = Modifier.width(120.dp)
                    )
                    Text(
                        text = businessHours.timeDuration,
                        fontWeight = FontWeight.Light
                    )
                }
            }
            if (undeterminedDays.isNotEmpty() || !isHolidaysDetermined) {
                Row {
                    val breakDayDescription =
                        if (isHolidaysDetermined) {
                            if (undeterminedDays == setOf(
                                    DayClass.SATURDAY
                                )
                            ) {
                                "토요일"
                            } else if (undeterminedDays == setOf(
                                    DayClass.SUNDAY
                                )
                            ) {
                                "일요일"
                            } else {
                                "주말"
                            }
                        } else {
                            if (undeterminedDays == setOf(
                                    DayClass.SATURDAY,
                                    DayClass.SUNDAY
                                )
                            ) {
                                "주말·공휴일"
                            } else if (undeterminedDays == setOf(
                                    DayClass.SATURDAY
                                )
                            ) {
                                "토요일·공휴일"
                            } else if (undeterminedDays == setOf(
                                    DayClass.SUNDAY
                                )
                            ) {
                                "일요일·공휴일"
                            } else {
                                "공휴일"
                            }
                        }
                    Text(
                        text = breakDayDescription,
                        modifier = Modifier.width(120.dp)
                    )
                    Text(
                        text = "휴무",
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}