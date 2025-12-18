package com.intp.widget

import androidx.compose.ui.graphics.Color

data class ModeData(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: String,
    val goalTitle: String,
    val goalText: String,
    val bgColor: Color,
    val textColor: Color
)

val modes = listOf(
    ModeData(
        id = "A",
        title = "隐士模式",
        subtitle = "架构师 / 思考者",
        icon = "🧠",
        goalTitle = "只做一件大事",
        goalText = "思考战略、画流程图、或解决一个死胡同。做完就停，哪怕只做了30分钟。",
        bgColor = Color(0xFFECFDF5),
        textColor = Color(0xFF047857)
    ),
    ModeData(
        id = "B",
        title = "将军模式",
        subtitle = "救火队长 / 决策者",
        icon = "⚔️",
        goalTitle = "速战速决",
        goalText = "集中处理签字、回复消息、听汇报等杂事。发指令给 ISTJ 落地。",
        bgColor = Color(0xFFEFF6FF),
        textColor = Color(0xFF1D4ED8)
    ),
    ModeData(
        id = "C",
        title = "补给模式",
        subtitle = "生活家 / 运动者",
        icon = "🔋",
        goalTitle = "生发阳气",
        goalText = "物理排毒（运动、户外）。向戊土女友充电。只谈风月，不谈工作。",
        bgColor = Color(0xFFF5F3FF),
        textColor = Color(0xFF6D28D9)
    )
)
