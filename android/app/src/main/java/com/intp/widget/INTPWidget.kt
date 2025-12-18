package com.intp.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

val modeIndexKey = intPreferencesKey("mode_index")
val currentIndexParam = ActionParameters.Key<Int>("current_index")

class INTPWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val prefs = currentState<Preferences>()
            val currentIndex = prefs[modeIndexKey] ?: 0
            val mode = modes[currentIndex % modes.size]
            
            WidgetContent(mode = mode, currentIndex = currentIndex)
        }
    }
}

@Composable
fun WidgetContent(mode: ModeData, currentIndex: Int) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(20.dp)
            .background(mode.bgColor)
            .clickable(
                actionRunCallback<SwitchModeAction>(
                    actionParametersOf(currentIndexParam to currentIndex)
                )
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧：图标 + 标题
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = GlanceModifier.padding(end = 12.dp)
            ) {
                Text(
                    text = mode.icon,
                    style = TextStyle(fontSize = 32.sp)
                )
                Spacer(modifier = GlanceModifier.height(4.dp))
                Text(
                    text = mode.title,
                    style = TextStyle(
                        color = ColorProvider(mode.textColor),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = mode.subtitle,
                    style = TextStyle(
                        color = ColorProvider(mode.textColor.copy(alpha = 0.7f)),
                        fontSize = 10.sp
                    )
                )
            }
            
            // 右侧：目标框 + 按钮
            Column(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 目标框
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .cornerRadius(10.dp)
                        .background(Color.White.copy(alpha = 0.6f))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "🎯 ${mode.goalTitle}",
                            style = TextStyle(
                                color = ColorProvider(mode.textColor),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = mode.goalText,
                            style = TextStyle(
                                color = ColorProvider(mode.textColor.copy(alpha = 0.85f)),
                                fontSize = 10.sp
                            ),
                            maxLines = 2
                        )
                    }
                }
                
                Spacer(modifier = GlanceModifier.height(6.dp))
                
                // 切换按钮
                Box(
                    modifier = GlanceModifier
                        .cornerRadius(6.dp)
                        .background(mode.textColor)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "点击切换",
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

class SwitchModeAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val currentIndex = parameters[currentIndexParam] ?: 0
        val newIndex = (currentIndex + 1) % modes.size
        
        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
            prefs.toMutablePreferences().apply {
                set(modeIndexKey, newIndex)
            }
        }
        INTPWidget().update(context, glanceId)
    }
}
