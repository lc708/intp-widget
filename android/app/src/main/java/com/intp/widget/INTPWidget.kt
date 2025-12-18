package com.intp.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.color.ColorProvider as GlanceColorProvider
import androidx.glance.appwidget.cornerRadius
import androidx.compose.ui.graphics.Color

val modeIndexKey = intPreferencesKey("mode_index")
val currentIndexParam = ActionParameters.Key<Int>("current_index")

class INTPWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

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
            .cornerRadius(24.dp)
            .background(ColorProvider(mode.bgColor, mode.bgColor))
            .clickable(
                actionRunCallback<SwitchModeAction>(
                    actionParametersOf(currentIndexParam to currentIndex)
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 图标
            Text(
                text = mode.icon,
                style = TextStyle(fontSize = 36.sp)
            )
            
            Spacer(modifier = GlanceModifier.height(6.dp))
            
            // 标题
            Text(
                text = mode.title,
                style = TextStyle(
                    color = ColorProvider(mode.textColor, mode.textColor),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            
            // 副标题
            Text(
                text = mode.subtitle,
                style = TextStyle(
                    color = ColorProvider(
                        mode.textColor.copy(alpha = 0.7f),
                        mode.textColor.copy(alpha = 0.7f)
                    ),
                    fontSize = 11.sp
                )
            )
            
            Spacer(modifier = GlanceModifier.height(10.dp))
            
            // 目标框
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .cornerRadius(12.dp)
                    .background(ColorProvider(Color.White.copy(alpha = 0.6f), Color.White.copy(alpha = 0.6f)))
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = "🎯 ${mode.goalTitle}",
                        style = TextStyle(
                            color = ColorProvider(mode.textColor, mode.textColor),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = GlanceModifier.height(4.dp))
                    Text(
                        text = mode.goalText,
                        style = TextStyle(
                            color = ColorProvider(
                                mode.textColor.copy(alpha = 0.85f),
                                mode.textColor.copy(alpha = 0.85f)
                            ),
                            fontSize = 11.sp
                        ),
                        maxLines = 3
                    )
                }
            }
            
            Spacer(modifier = GlanceModifier.height(10.dp))
            
            // 切换按钮提示
            Box(
                modifier = GlanceModifier
                    .cornerRadius(8.dp)
                    .background(ColorProvider(mode.textColor, mode.textColor))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "点击切换状态",
                    style = TextStyle(
                        color = ColorProvider(Color.White, Color.White),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
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
                this[modeIndexKey] = newIndex
            }
        }
        INTPWidget().update(context, glanceId)
    }
}

