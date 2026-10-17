package com.phonesurvival.app

import android.content.Context
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PhoneSurvivalApp(applicationContext) }
    }
}

private data class AppPalette(val id: String, val label: String, val accent: Color, val bg: Color, val panel: Color)
private val appPalettes = listOf(
    AppPalette("green", "שחור וירוק", Color(0xFF39E58C), Color(0xFF080D0B), Color(0xFF121A16)),
    AppPalette("blue", "כחול טכנולוגי", Color(0xFF62B4FF), Color(0xFF080D15), Color(0xFF121C2A)),
    AppPalette("purple", "סגול עמוק", Color(0xFFCB9BFF), Color(0xFF100A16), Color(0xFF20142A)),
    AppPalette("gold", "שחור וזהב", Color(0xFFFFD166), Color(0xFF100E08), Color(0xFF201B10)),
    AppPalette("orange", "כתום אנרגטי", Color(0xFFFFA45B), Color(0xFF120B07), Color(0xFF24170F)),
    AppPalette("teal", "טורקיז", Color(0xFF54E1D0), Color(0xFF061211), Color(0xFF102321)),
    AppPalette("red", "אדום יין", Color(0xFFFF7373), Color(0xFF160809), Color(0xFF281214)),
    AppPalette("pink", "ורוד ניאון", Color(0xFFFF8EC7), Color(0xFF160B13), Color(0xFF291522)),
    AppPalette("cyan", "תכלת קרח", Color(0xFF83E8FF), Color(0xFF071116), Color(0xFF12232A)),
    AppPalette("silver", "כסף ואפור", Color(0xFFD7DEE8), Color(0xFF0B0D11), Color(0xFF191D24)),
    AppPalette("lime", "ירוק ליים", Color(0xFFC6F36B), Color(0xFF0D1106), Color(0xFF1B2410)),
    AppPalette("midnight", "לילה סגול", Color(0xFF9DABFF), Color(0xFF090918), Color(0xFF17172B))
)
private enum class Tab(val label: String, val icon: String) {
    HOME("בית", "⌂"), TOOLS("כלים", "🧰"), GAMES("משחקים", "🎮"), VAULT("זיכרון שחור", "🔐"), SETTINGS("הגדרות", "⚙")
}
private data class Tool(val name: String, val detail: String, val icon: String, val action: String)
private val tools = listOf(
    Tool("מחשבון", "חישובים בסיסיים", "🧮", "calculator"),
    Tool("פתקים", "מחברת מקומית", "📝", "notes"),
    Tool("שעון עצר", "מדידת זמן", "⏱️", "stopwatch"),
    Tool("טיימר", "ספירה לאחור", "⏳", "timer"),
    Tool("המרת יחידות", "אורך, משקל וטמפרטורה", "📐", "converter"),
    Tool("מספר אקראי", "הגרלה בין שני מספרים", "🎲", "random"),
    Tool("מחולל סיסמאות", "יצירת סיסמה מקומית", "🔑", "password"),
    Tool("רשימת חירום", "צ'קליסט נשמר במכשיר", "🧭", "emergency"),
    Tool("סוללה", "אחוזים וטעינה", "🔋", "battery"),
    Tool("ניהול אחסון", "מידע וטיפים", "💾", "storage"),
    Tool("פנס", "הדלקת מבזק המכשיר", "🔦", "flashlight"),
    Tool("מצפן", "כיוון לפי חיישני המכשיר", "🧭", "compass")
)
private data class Game(val id: String, val name: String, val detail: String, val icon: String, val ready: Boolean)
private val games = listOf(
    Game("guess", "נחש את המספר", "מצא מספר סודי בין 1 ל־50", "🎯", true),
    Game("tic", "איקס עיגול", "משחק לשני שחקנים באותו מכשיר", "⭕", true),
    Game("math", "אתגר חשבון", "פתור תרגילים וקבל ניקוד", "➗", true),
    Game("memory", "זיכרון זוגות", "מצא 6 זוגות קלפים", "🧠", true),
    Game("puzzle", "פאזל מספרים", "סדר את המספרים מ־1 עד 8", "🧩", true),
    Game("words", "ערבול מילים", "סדר את האותיות לפי הרמז", "🔤", true),
    Game("reaction", "מבחן תגובה", "בדוק את מהירות התגובה שלך", "⚡", true),
    Game("dice", "הטלת קובייה", "גלגל קובייה וירטואלית", "🎲", true),
    Game("coin", "עץ או פלי", "הטלת מטבע אקראית", "🪙", true),
    Game("sequence", "רצף מספרים", "מצא את המספר הבא", "🔢", true),
    Game("rps", "אבן נייר ומספריים", "שחק מול המחשב", "✊", true),
    Game("quiz", "חידון ידע", "שאלות כלליות בלי אינטרנט", "❓", true),
    Game("tap", "אתגר הקשות", "כמה לחיצות תספיק ב־10 שניות?", "👆", true),
    Game("simon", "זכור את הרצף", "אמן את הזיכרון", "🎨", true),
    Game("higher", "גבוה או נמוך", "נחש אם הקלף הבא גבוה יותר", "🃏", true),
    Game("arcade", "ארקייד", "מתוכנן להרחבה", "👾", false),
    Game("board", "משחקי לוח", "מתוכנן להרחבה", "♟️", false)
)

@Composable
private val LocalCompactMode = compositionLocalOf { false }

@Composable
private fun PhoneSurvivalApp(context: Context) {
    val prefs = remember(context) { context.getSharedPreferences("phone_survival", Context.MODE_PRIVATE) }
    var paletteId by remember { mutableStateOf(prefs.getString("palette", "green") ?: "green") }
    var compactMode by remember { mutableStateOf(prefs.getBoolean("compact", false)) }
    val palette = appPalettes.firstOrNull { it.id == paletteId } ?: appPalettes.first()
    var tab by remember { mutableStateOf(Tab.HOME) }
    var action by remember { mutableStateOf<String?>(null) }
    var game by remember { mutableStateOf<Game?>(null) }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl, LocalCompactMode provides compactMode) {
        MaterialTheme(colorScheme = darkColorScheme(
            primary = palette.accent, secondary = palette.accent.copy(alpha = .8f),
            background = palette.bg, surface = palette.panel, surfaceVariant = palette.panel,
            onBackground = Color(0xFFF1F5F2), onSurface = Color(0xFFF1F5F2), onPrimary = Color(0xFF061009)
        )) {
            Scaffold(
                containerColor = palette.bg,
                bottomBar = {
                    NavigationBar(containerColor = palette.panel) {
                        Tab.values().forEach { t ->
                            NavigationBarItem(
                                selected = tab == t, onClick = { tab = t },
                                icon = { Text(t.icon, fontSize = 19.sp) },
                                label = { Text(t.label, fontSize = 10.sp) }
                            )
                        }
                    }
                }
            ) { pad ->
                Box(Modifier.fillMaxSize().padding(pad)) {
                    when (tab) {
                        Tab.HOME -> HomeScreen(palette, onTab = { tab = it }, onAction = { action = it })
                        Tab.TOOLS -> ToolsScreen { action = it }
                        Tab.GAMES -> GamesScreen { game = it }
                        Tab.VAULT -> VaultLanding(palette) { action = "vault" }
                        Tab.SETTINGS -> SettingsScreen(
                            palette, paletteId,
                            onPalette = { paletteId = it; prefs.edit().putString("palette", it).apply() },
                            prefs = prefs,
                            compact = compactMode,
                            onCompact = { compactMode = it; prefs.edit().putBoolean("compact", it).apply() }
                        )
                    }
                }
            }
        }
    }
    when (action) {
        "calculator" -> CalculatorDialog { action = null }
        "notes" -> NotesDialog(prefs) { action = null }
        "stopwatch" -> StopwatchDialog { action = null }
        "timer" -> CountdownDialog { action = null }
        "converter" -> UnitConverterDialog { action = null }
        "random" -> RandomNumberDialog { action = null }
        "battery" -> BatteryInfoDialog(context) { action = null }
        "vault" -> VaultDialog(prefs) { action = null }
        "password" -> PasswordDialog { action = null }
        "emergency" -> EmergencyDialog(prefs) { action = null }
        "storage" -> InfoDialog("ניהול אחסון", "הפתקים וההגדרות נשמרים מקומית. האפליקציה אינה יכולה לנקות או לקרוא קבצים פרטיים של אפליקציות אחרות.") { action = null }
        "flashlight" -> FlashlightDialog(context) { action = null }
        "compass" -> CompassDialog(context) { action = null }
        else -> Unit
    }
    game?.let { GameDialog(it) { game = null } }
}

@Composable
private fun Page(content: @Composable ColumnScope.() -> Unit) {
    val compact = LocalCompactMode.current
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(if (compact) 11.dp else 18.dp),
        verticalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 14.dp), content = content
    )
}

@Composable
private fun HomeScreen(p: AppPalette, onTab: (Tab) -> Unit, onAction: (String) -> Unit) {
    Page {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(color = p.accent.copy(alpha = .13f), shape = RoundedCornerShape(17.dp), modifier = Modifier.size(58.dp)) {
                Box(contentAlignment = Alignment.Center) { Text("🧰", fontSize = 30.sp) }
            }
            Column(Modifier.weight(1f)) {
                Text("PHONE SURVIVAL", fontSize = 21.sp, fontWeight = FontWeight.Black)
                Text("ארגז הכלים האישי שלך", color = p.accent, fontSize = 13.sp)
            }
        }
        Surface(color = p.panel, shape = RoundedCornerShape(24.dp), border = BorderStroke(1.dp, p.accent.copy(alpha = .28f))) {
            Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("מוכן לכל מצב", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Text("כלים שימושיים, פתקים פרטיים ומשחקים — במקום אחד, עם תכנון לשימוש ללא אינטרנט.", color = Color(0xFFB9C8BF), fontSize = 14.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Badge("אופליין", p.accent)
                    Badge("מראה אישי", p.accent)
                }
                Button(onClick = { onTab(Tab.TOOLS) }, modifier = Modifier.fillMaxWidth()) { Text("פתיחת ארגז הכלים") }
            }
        }
        Text("גישה מהירה", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            QuickCard("🧮", "מחשבון", Modifier.weight(1f)) { onAction("calculator") }
            QuickCard("🔐", "הכספת", Modifier.weight(1f)) { onTab(Tab.VAULT) }
            QuickCard("🎮", "משחקים", Modifier.weight(1f)) { onTab(Tab.GAMES) }
        }
        FeatureRow("🧰", "כלים חכמים", "מחשבון, שעון עצר, פתקים ועוד", p) { onTab(Tab.TOOLS) }
        FeatureRow("🔐", "כספת פרטית", "הצפנת פתקים באמצעות סיסמה", p) { onTab(Tab.VAULT) }
        FeatureRow("🎨", "התאמה אישית", "${appPalettes.size} ערכות צבע", p) { onTab(Tab.SETTINGS) }
    }
}

@Composable
private fun Badge(label: String, color: Color) {
    Surface(color = color.copy(alpha = .12f), shape = RoundedCornerShape(50), border = BorderStroke(1.dp, color.copy(alpha = .3f))) {
        Text(label, color = color, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
    }
}

@Composable
private fun QuickCard(icon: String, label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(modifier.clickable(onClick = onClick), color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = .18f))) {
        Column(Modifier.padding(vertical = 14.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(icon, fontSize = 25.sp)
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun FeatureRow(icon: String, title: String, detail: String, p: AppPalette, onClick: () -> Unit) {
    Surface(Modifier.fillMaxWidth().clickable(onClick = onClick), color = p.panel, shape = RoundedCornerShape(17.dp), border = BorderStroke(1.dp, p.accent.copy(alpha = .13f))) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(icon, fontSize = 27.sp)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(detail, fontSize = 12.sp, color = Color(0xFFADBDB3))
            }
            Text("‹", fontSize = 24.sp, color = p.accent)
        }
    }
}

@Composable
private fun ToolsScreen(onAction: (String) -> Unit) {
    Page {
        Text("ארגז הכלים", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("כלים שימושיים שנפתחים מתוך האפליקציה", color = Color(0xFFADBDB3))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth().height(500.dp),
            horizontalArrangement = Arrangement.spacedBy(11.dp), verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            items(tools) { tool ->
                Surface(
                    Modifier.fillMaxWidth().height(138.dp).clickable { onAction(tool.action) },
                    color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = .18f))
                ) {
                    Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Text(tool.icon, fontSize = 29.sp)
                        Text(tool.name, fontWeight = FontWeight.Bold)
                        Text(tool.detail, color = Color(0xFFADBDB3), fontSize = 11.sp)
                    }
                }
            }
        }
        Text("פנס ומצפן מסומנים כממתינים למימוש חומרה והרשאות.", fontSize = 12.sp, color = Color(0xFFADBDB3))
    }
}

@Composable
private fun GamesScreen(onGame: (Game) -> Unit) {
    Page {
        Text("מרכז המשחקים", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("${games.count { it.ready }} משחקונים לא מקוונים זמינים כרגע. הרחבת הספרייה ל־250 משחקי קוד פתוח עדיין לא הושלמה.", fontSize = 13.sp, color = Color(0xFFADBDB3))
        games.forEach { g ->
            Surface(
                Modifier.fillMaxWidth().clickable { if (g.ready) onGame(g) },
                color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(17.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = if (g.ready) .22f else .08f))
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(g.icon, fontSize = 28.sp)
                    Column(Modifier.weight(1f)) {
                        Text(g.name, fontWeight = FontWeight.Bold)
                        Text(g.detail, fontSize = 12.sp, color = Color(0xFFADBDB3))
                    }
                    Text(if (g.ready) "שחק" else "בהמשך", color = if (g.ready) MaterialTheme.colorScheme.primary else Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun VaultLanding(p: AppPalette, onOpen: () -> Unit) {
    Page {
        Text("הכספת הפרטית", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Surface(color = p.panel, shape = RoundedCornerShape(24.dp), border = BorderStroke(1.dp, p.accent.copy(alpha = .3f))) {
            Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(13.dp)) {
                Text("🔐", fontSize = 52.sp)
                Text("פתקים מוצפנים במכשיר", fontSize = 19.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Text("הטקסט מוצפן עם AES-GCM ומפתח שמופק מהסיסמה. אם תאבד את הסיסמה, ייתכן שלא ניתן יהיה לשחזר את המידע.", textAlign = TextAlign.Center, fontSize = 13.sp, color = Color(0xFFADBDB3))
                Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) { Text("פתיחה או יצירת כספת") }
            }
        }
        Text("הגרסה הראשונית שומרת פתקים מוצפנים. קבצים מצורפים וגיבוי מוצפן מתוכננים לשלב הבא.", fontSize = 12.sp, color = Color(0xFFADBDB3))
    }
}

@Composable
private fun SettingsScreen(p: AppPalette, selected: String, onPalette: (String) -> Unit, prefs: android.content.SharedPreferences, compact: Boolean, onCompact: (Boolean) -> Unit) {
    var resetConfirm by remember { mutableStateOf(false) }
    Page {
        Text("הגדרות והתאמה אישית", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Text("בחר אחת מ־${appPalettes.size} ערכות העיצוב. הבחירה נשמרת במכשיר.", color = Color(0xFFADBDB3))
        appPalettes.forEach { item ->
            Surface(
                Modifier.fillMaxWidth().clickable { onPalette(item.id) },
                color = item.panel, shape = RoundedCornerShape(16.dp),
                border = BorderStroke(if (selected == item.id) 2.dp else 1.dp, item.accent.copy(alpha = if (selected == item.id) 1f else .2f))
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(color = item.accent, shape = RoundedCornerShape(50), modifier = Modifier.size(25.dp)) {}
                    Column(Modifier.weight(1f)) {
                        Text(item.label, fontWeight = FontWeight.Bold)
                        Text("ערכת צבע כהה", color = Color(0xFFADBDB3), fontSize = 11.sp)
                    }
                    if (selected == item.id) Text("✓ נבחר", color = item.accent)
                }
            }
        }
        Divider(color = p.accent.copy(alpha = .2f))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Column(Modifier.weight(1f)) {
                Text("ממשק קומפקטי", fontWeight = FontWeight.Bold)
                Text("צמצום הרווחים במסכי האפליקציה", fontSize = 11.sp, color = Color(0xFFADBDB3))
            }
            Switch(checked = compact, onCheckedChange = onCompact)
        }
        FeatureRow("🛡️", "פרטיות", "אין הרשאת אינטרנט במניפסט", p) {}\n        FeatureRow("🎮", "משחקים לא מקוונים", "${games.count { it.ready }} משחקונים פעילים בגרסה זו", p) {}
        FeatureRow("ℹ️", "אודות", "PHONE SURVIVAL · גרסה 1.0.0", p) {}
        OutlinedButton(onClick = { resetConfirm = true }, modifier = Modifier.fillMaxWidth()) { Text("איפוס צבע לעיצוב המקורי") }
    }
    if (resetConfirm) {
        AlertDialog(
            onDismissRequest = { resetConfirm = false }, title = { Text("לאפס עיצוב?") },
            text = { Text("הצבע יחזור לשחור וירוק. הפתקים והכספת לא יימחקו.") },
            confirmButton = { TextButton(onClick = { onPalette("green"); compact = false; prefs.edit().putBoolean("compact", false).apply(); resetConfirm = false }) { Text("איפוס") } },
            dismissButton = { TextButton(onClick = { resetConfirm = false }) { Text("ביטול") } }
        )
    }
}

@Composable
private fun CalculatorDialog(onDismiss: () -> Unit) {
    var display by remember { mutableStateOf("0") }
    var first by remember { mutableStateOf<Double?>(null) }
    var op by remember { mutableStateOf<String?>(null) }
    var expression by remember { mutableStateOf("") }
    var next by remember { mutableStateOf(false) }
    val keys = listOf("7","8","9","÷","4","5","6","×","1","2","3","−","C","0","=","+")
    AlertDialog(
        onDismissRequest = onDismiss, title = { Text("מחשבון") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(expression.ifBlank { "מוכן לחישוב" }, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, color = Color(0xFFADBDB3), fontSize = 12.sp)
                Text(display, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, fontSize = 29.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                keys.chunked(4).forEach { line ->
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        line.forEach { k ->
                            OutlinedButton(
                                onClick = {
                                    when (k) {
                                        "C" -> { display = "0"; first = null; op = null; expression = ""; next = false }
                                        "+", "−", "×", "÷" -> { first = display.toDoubleOrNull(); op = k; expression = "$display $k"; next = true }
                                        "=" -> {
                                            val a = first; val b = display.toDoubleOrNull(); val o = op
                                            if (a != null && b != null && o != null) {
                                                val n = when (o) { "+" -> a+b; "−" -> a-b; "×" -> a*b; "÷" -> if (b != 0.0) a/b else Double.NaN; else -> b }
                                                display = if (n.isFinite()) neat(n) else "שגיאה"
                                                expression = "$a $o $b ="
                                                first = null; op = null; next = true
                                            }
                                        }
                                        else -> { display = if (next || display == "0" || display == "שגיאה") k else display + k; next = false }
                                    }
                                },
                                modifier = Modifier.weight(1f), contentPadding = PaddingValues(vertical = 4.dp, horizontal = 0.dp)
                            ) { Text(k) }
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}
private fun neat(n: Double): String = if (n == n.toLong().toDouble()) n.toLong().toString() else String.format(java.util.Locale.US, "%.5f", n).trimEnd('0').trimEnd('.')

@Composable
private fun NotesDialog(prefs: android.content.SharedPreferences, onDismiss: () -> Unit) {
    var text by remember { mutableStateOf(prefs.getString("notes", "") ?: "") }
    var saved by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss, title = { Text("הפתקים שלי") },
        text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("הפתקים נשמרים מקומית. לתוכן רגיש השתמש בכספת.", fontSize = 12.sp, color = Color(0xFFADBDB3))
            OutlinedTextField(text, { text = it; saved = false }, modifier = Modifier.fillMaxWidth().height(175.dp), label = { Text("תוכן") })
            if (saved) Text("נשמר במכשיר", color = MaterialTheme.colorScheme.primary)
        } },
        confirmButton = { TextButton(onClick = { prefs.edit().putString("notes", text).apply(); saved = true }) { Text("שמירה") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}

@Composable
private fun StopwatchDialog(onDismiss: () -> Unit) {
    var seconds by remember { mutableIntStateOf(0) }
    var running by remember { mutableStateOf(false) }
    LaunchedEffect(running) { while (running) { delay(1000); seconds++ } }
    AlertDialog(
        onDismissRequest = onDismiss, title = { Text("שעון עצר") },
        text = { Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(13.dp)) {
            Text(String.format("%02d:%02d:%02d", seconds/3600, (seconds/60)%60, seconds%60), fontSize = 30.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { running = !running }) { Text(if (running) "השהיה" else "התחלה") }
                OutlinedButton(onClick = { running = false; seconds = 0 }) { Text("איפוס") }
            }
        } },
        confirmButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}

@Composable
private fun VaultDialog(prefs: android.content.SharedPreferences, onDismiss: () -> Unit) {
    val vault = remember(prefs) { VaultStore(prefs) }
    var password by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var unlocked by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (unlocked) "הכספת פתוחה" else if (vault.exists()) "פתיחת הכספת" else "יצירת כספת") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                if (!unlocked) {
                    Text(if (vault.exists()) "הזן את הסיסמה כדי לפענח את התוכן." else "צור סיסמה באורך 10 תווים לפחות. הסיסמה עצמה אינה נשמרת.", fontSize = 12.sp, color = Color(0xFFADBDB3))
                    OutlinedTextField(password, { password = it; error = "" }, modifier = Modifier.fillMaxWidth(), label = { Text("סיסמה") }, singleLine = true, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
                    if (error.isNotBlank()) Text(error, color = Color(0xFFFF8A8A), fontSize = 12.sp)
                } else {
                    OutlinedTextField(note, { note = it; saved = false }, modifier = Modifier.fillMaxWidth().height(175.dp), label = { Text("פתקים מוצפנים") })
                    if (saved) Text("נשמר בהצפנה", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                    if (error.isNotBlank()) Text(error, color = Color(0xFFFF8A8A), fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            if (!unlocked) TextButton(onClick = {
                when {
                    password.length < 10 -> error = "יש לבחור סיסמה בת 10 תווים לפחות."
                    !vault.exists() -> try { vault.create(password); unlocked = true } catch (_: Exception) { error = "יצירת הכספת נכשלה." }
                    else -> { val result = vault.open(password); if (result == null) error = "סיסמה שגויה או מידע פגום." else { note = result; unlocked = true } }
                }
            }) { Text(if (vault.exists()) "פתיחה" else "יצירה") }
            else TextButton(onClick = { try { vault.save(password, note); saved = true } catch (_: Exception) { error = "השמירה נכשלה." } }) { Text("שמירה מוצפנת") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}

private class VaultStore(private val prefs: android.content.SharedPreferences) {
    fun exists() = prefs.getString("vault_salt", null) != null && prefs.getString("vault_data", null) != null
    fun create(password: String) {
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        prefs.edit().putString("vault_salt", Base64.encodeToString(salt, Base64.NO_WRAP)).apply()
        save(password, "")
    }
    fun save(password: String, value: String) {
        val salt = Base64.decode(prefs.getString("vault_salt", null) ?: error("No salt"), Base64.NO_WRAP)
        val iv = ByteArray(12).also { SecureRandom().nextBytes(it) }
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key(password, salt), GCMParameterSpec(128, iv))
        val data = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        prefs.edit().putString("vault_iv", Base64.encodeToString(iv, Base64.NO_WRAP))
            .putString("vault_data", Base64.encodeToString(data, Base64.NO_WRAP)).apply()
    }
    fun open(password: String): String? {
        return try {
            val saltText = prefs.getString("vault_salt", null) ?: return null
            val ivText = prefs.getString("vault_iv", null) ?: return null
            val dataText = prefs.getString("vault_data", null) ?: return null
            val salt = Base64.decode(saltText, Base64.NO_WRAP)
            val iv = Base64.decode(ivText, Base64.NO_WRAP)
            val data = Base64.decode(dataText, Base64.NO_WRAP)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, key(password, salt), GCMParameterSpec(128, iv))
            String(cipher.doFinal(data), Charsets.UTF_8)
        } catch (_: Exception) { null }
    }
    private fun key(password: String, salt: ByteArray): SecretKeySpec {
        val spec = PBEKeySpec(password.toCharArray(), salt, 160_000, 256)
        return try { SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded, "AES") }
        finally { spec.clearPassword() }
    }
}

@Composable
private fun PasswordDialog(onDismiss: () -> Unit) {
    val alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#%+-_"
    var length by remember { mutableStateOf("20") }
    var result by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss, title = { Text("מחולל סיסמאות") },
        text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(length, { length = it.filter(Char::isDigit).take(2) }, label = { Text("אורך (8–40)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            if (result.isNotBlank()) Text(result, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
            Text("הסיסמה נוצרת מקומית ואינה נשלחת לרשת.", color = Color(0xFFADBDB3), fontSize = 12.sp)
        } },
        confirmButton = { TextButton(onClick = { val n = (length.toIntOrNull() ?: 20).coerceIn(8, 40); result = (1..n).map { alphabet[SecureRandom().nextInt(alphabet.length)] }.joinToString("") }) { Text("יצירה") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}

@Composable
private fun EmergencyDialog(onDismiss: () -> Unit) {
    val items = listOf("שמור על סוללת המכשיר", "ודא גישה לפנס ולתאורה בטוחה", "שמור אנשי קשר לשעת חירום", "בדוק מים וציוד חיוני", "עדכן אדם קרוב על מצבך")
    var checked by remember { mutableStateOf(setOf<Int>()) }
    AlertDialog(
        onDismissRequest = onDismiss, title = { Text("רשימת בדיקה לחירום") },
        text = { Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
            items.forEachIndexed { i, item ->
                Row(Modifier.fillMaxWidth().clickable { checked = if (i in checked) checked - i else checked + i }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (i in checked) "☑" else "□", color = MaterialTheme.colorScheme.primary, fontSize = 20.sp)
                    Text(item, fontSize = 13.sp)
                }
            }
        } },
        confirmButton = { TextButton(onClick = onDismiss) { Text("סיום") } }
    )
}

@Composable
private fun InfoDialog(title: String, detail: String, onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text(title) }, text = { Text(detail) }, confirmButton = { TextButton(onClick = onDismiss) { Text("הבנתי") } })
}

@Composable
private fun GameDialog(game: Game, onDismiss: () -> Unit) {
    when (game.id) {
        "guess" -> GuessDialog(onDismiss)
        "tic" -> TicDialog(onDismiss)
        "math" -> MathDialog(onDismiss)
        "memory" -> MemoryPairsDialog(onDismiss)
        "puzzle" -> NumberPuzzleDialog(onDismiss)
        "words" -> WordScrambleDialog(onDismiss)
        "reaction" -> ReactionDialog(onDismiss)
        "dice" -> DiceDialog(onDismiss)
        "coin" -> CoinDialog(onDismiss)
        "sequence" -> SequenceDialog(onDismiss)
        else -> InfoDialog(game.name, "המשחק עדיין בתכנון ולא נכלל בגרסה הנוכחית.") { onDismiss() }
    }
}

@Composable
private fun GuessDialog(onDismiss: () -> Unit) {
    var secret by remember { mutableIntStateOf(Random.nextInt(1, 51)) }
    var guess by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("בחר מספר בין 1 ל־50") }
    var won by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss, title = { Text("🎯 נחש את המספר") },
        text = { Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Text(message, color = MaterialTheme.colorScheme.primary)
            OutlinedTextField(guess, { guess = it.filter(Char::isDigit).take(2) }, label = { Text("המספר שלך") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
        } },
        confirmButton = { TextButton(onClick = {
            if (won) { secret = Random.nextInt(1, 51); guess = ""; won = false; message = "מספר חדש נבחר" }
            else {
                val n = guess.toIntOrNull()
                message = when { n == null || n !in 1..50 -> "הזן מספר בין 1 ל־50"; n < secret -> "נסה מספר גבוה יותר"; n > secret -> "נסה מספר נמוך יותר"; else -> { won = true; "נכון! המספר הוא $secret" } }
            }
        }) { Text(if (won) "משחק חדש" else "בדיקה") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}

@Composable
private fun TicDialog(onDismiss: () -> Unit) {
    var cells by remember { mutableStateOf(List(9) { "" }) }
    var xTurn by remember { mutableStateOf(true) }
    val winner = remember(cells) { winnerOf(cells) }
    AlertDialog(
        onDismissRequest = onDismiss, title = { Text("⭕ איקס עיגול") },
        text = { Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(when (winner) { "X" -> "איקס ניצח"; "O" -> "עיגול ניצח"; "D" -> "תיקו"; else -> if (xTurn) "תור X" else "תור O" }, color = MaterialTheme.colorScheme.primary)
            (0..2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    (0..2).forEach { col ->
                        val i = row * 3 + col
                        OutlinedButton(onClick = {
                            if (cells[i].isEmpty() && winner == null) { cells = cells.toMutableList().also { it[i] = if (xTurn) "X" else "O" }; xTurn = !xTurn }
                        }, modifier = Modifier.size(62.dp), contentPadding = PaddingValues(0.dp)) { Text(cells[i].ifEmpty { "·" }, fontSize = 22.sp) }
                    }
                }
            }
            OutlinedButton(onClick = { cells = List(9) { "" }; xTurn = true }) { Text("משחק חדש") }
        } },
        confirmButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}
private fun winnerOf(c: List<String>): String? {
    val lines = listOf(listOf(0,1,2), listOf(3,4,5), listOf(6,7,8), listOf(0,3,6), listOf(1,4,7), listOf(2,5,8), listOf(0,4,8), listOf(2,4,6))
    for (line in lines) if (c[line[0]].isNotEmpty() && c[line[0]] == c[line[1]] && c[line[1]] == c[line[2]]) return c[line[0]]
    return if (c.all { it.isNotEmpty() }) "D" else null
}

@Composable
private fun MathDialog(onDismiss: () -> Unit) {
    var a by remember { mutableIntStateOf(Random.nextInt(2, 30)) }
    var b by remember { mutableIntStateOf(Random.nextInt(2, 30)) }
    var answer by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("פתור את התרגיל") }
    AlertDialog(
        onDismissRequest = onDismiss, title = { Text("➗ אתגר חשבון") },
        text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("$a + $b = ?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            OutlinedTextField(answer, { answer = it.filter(Char::isDigit).take(3) }, label = { Text("התשובה") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            Text("ניקוד: $score", color = MaterialTheme.colorScheme.primary)
            Text(message, fontSize = 12.sp)
        } },
        confirmButton = { TextButton(onClick = {
            if (answer.toIntOrNull() == a + b) { score++; message = "נכון! ממשיכים."; a = Random.nextInt(2, 30); b = Random.nextInt(2, 30); answer = "" }
            else message = "לא הפעם — נסה שוב."
        }) { Text("בדיקה") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}


@Composable
private fun MemoryPairsDialog(onDismiss: () -> Unit) {
    val cards = remember { listOf("🐱", "🚀", "🌟", "🍀", "🎈", "🍉", "🐱", "🚀", "🌟", "🍀", "🎈", "🍉").shuffled() }
    var first by remember { mutableStateOf<Int?>(null) }
    var second by remember { mutableStateOf<Int?>(null) }
    var matched by remember { mutableStateOf(setOf<Int>()) }
    var moves by remember { mutableIntStateOf(0) }

    LaunchedEffect(second) {
        val a = first
        val b = second
        if (a != null && b != null) {
            delay(650)
            if (cards[a] == cards[b]) matched = matched + a + b
            first = null
            second = null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🧠 זיכרון זוגות") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(if (matched.size == cards.size) "כל הזוגות נמצאו! מהלך מצוין 🎉" else "זוגות שנמצאו: ${matched.size / 2} מתוך 6 · מהלכים: $moves", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxWidth().height(232.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(cards.indices.toList()) { i ->
                        val shown = i in matched || i == first || i == second
                        Surface(
                            modifier = Modifier.fillMaxWidth().aspectRatio(1f).clickable {
                                if (i !in matched && i != first && second == null) {
                                    if (first == null) first = i else { second = i; moves++ }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = if (i in matched) MaterialTheme.colorScheme.primary.copy(alpha = .20f) else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(if (shown) cards[i] else "?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { first = null; second = null; matched = emptySet(); moves = 0 }) { Text("משחק חדש") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}

private fun shuffledPuzzle(): List<Int> {
    val values = (1..8).toMutableList().apply { add(0) }
    var blank = 8
    repeat(100) {
        val options = listOf(
            blank - 3,
            blank + 3,
            if (blank % 3 > 0) blank - 1 else -1,
            if (blank % 3 < 2) blank + 1 else -1
        ).filter { it in 0..8 }
        val next = options.random()
        values[blank] = values[next]
        values[next] = 0
        blank = next
    }
    return values
}

@Composable
private fun NumberPuzzleDialog(onDismiss: () -> Unit) {
    var cells by remember { mutableStateOf(shuffledPuzzle()) }
    var moves by remember { mutableIntStateOf(0) }
    val solved = cells == ((1..8).toList() + 0)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🧩 פאזל מספרים") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text(if (solved) "נפתר! 🎉" else "סדר את המספרים מ־1 עד 8", color = MaterialTheme.colorScheme.primary)
                (0..2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        (0..2).forEach { col ->
                            val i = row * 3 + col
                            OutlinedButton(
                                onClick = {
                                    val blank = cells.indexOf(0)
                                    val adjacent = (i / 3 == blank / 3 && abs(i % 3 - blank % 3) == 1) ||
                                        (i % 3 == blank % 3 && abs(i / 3 - blank / 3) == 1)
                                    if (!solved && cells[i] != 0 && adjacent) {
                                        val next = cells.toMutableList()
                                        next[blank] = next[i]
                                        next[i] = 0
                                        cells = next
                                        moves++
                                    }
                                },
                                modifier = Modifier.size(68.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) { Text(if (cells[i] == 0) " " else cells[i].toString(), fontSize = 21.sp, fontWeight = FontWeight.Bold) }
                        }
                    }
                }
                Text("מהלכים: $moves", fontSize = 12.sp)
            }
        },
        confirmButton = { TextButton(onClick = { cells = shuffledPuzzle(); moves = 0 }) { Text("ערבוב מחדש") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}

private val wordChallenges = listOf(
    "מחשב" to "מכשיר לעבודה, לימוד ומשחק",
    "כדור" to "חפץ עגול שמשחקים בו",
    "ספר" to "קוראים בו סיפורים או מידע",
    "מפתח" to "פותח מנעול",
    "שולחן" to "רהיט שאפשר לכתוב או לאכול עליו",
    "טלפון" to "מכשיר לשיחות ולהודעות",
    "מחברת" to "כותבים בה",
    "משקפיים" to "עוזרים לראות בבירור",
    "כיסא" to "רהיט שיושבים עליו",
    "דלת" to "דרכה נכנסים לחדר ויוצאים ממנו"
)

private fun scrambledWord(word: String): String {
    val shuffled = word.toList().shuffled().joinToString("")
    return if (shuffled == word && word.length > 1) word.reversed() else shuffled
}

@Composable
private fun WordScrambleDialog(onDismiss: () -> Unit) {
    var challenge by remember { mutableStateOf(wordChallenges.random()) }
    var scramble by remember { mutableStateOf(scrambledWord(challenge.first)) }
    var answer by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(0) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🔤 ערבול מילים") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("סדר את האותיות למילה הנכונה:", fontSize = 13.sp)
                Text(scramble, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("רמז: ${challenge.second}", fontSize = 13.sp)
                OutlinedTextField(answer, { answer = it; message = "" }, label = { Text("המילה") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                if (message.isNotBlank()) Text(message, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                Text("ניקוד: $score", fontSize = 12.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (answer.trim().replace(" ", "") == challenge.first) {
                    score++
                    message = "נכון מאוד!"
                    challenge = wordChallenges.filter { it.first != challenge.first }.random()
                    scramble = scrambledWord(challenge.first)
                    answer = ""
                } else message = "לא בדיוק — נסה שוב."
            }) { Text("בדיקה") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}

@Composable
private fun ReactionDialog(onDismiss: () -> Unit) {
    var phase by remember { mutableStateOf("idle") }
    var startedAt by remember { mutableLongStateOf(0L) }
    var reactionMs by remember { mutableLongStateOf(0L) }
    LaunchedEffect(phase) {
        if (phase == "waiting") {
            delay(Random.nextLong(1500L, 3500L))
            if (phase == "waiting") {
                startedAt = System.nanoTime() / 1_000_000L
                phase = "ready"
            }
        }
    }
    val title = when (phase) {
        "waiting" -> "המתן לצבע הירוק…"
        "ready" -> "עכשיו! לחץ מהר!"
        "early" -> "לחצת מוקדם מדי"
        "result" -> "זמן התגובה: $reactionMs מילישניות"
        else -> "בדוק את מהירות התגובה"
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("⚡ מבחן תגובה") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(title, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                Button(
                    onClick = {
                        when (phase) {
                            "idle", "early", "result" -> { reactionMs = 0L; phase = "waiting" }
                            "waiting" -> phase = "early"
                            "ready" -> { reactionMs = (System.nanoTime() / 1_000_000L) - startedAt; phase = "result" }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (phase == "ready") Color(0xFF20C870) else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(when (phase) { "waiting" -> "לא ללחוץ עדיין"; "ready" -> "לחץ!"; "early" -> "נסה שוב"; "result" -> "שחק שוב"; else -> "התחלה" })
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}

@Composable
private fun DiceDialog(onDismiss: () -> Unit) {
    var value by remember { mutableIntStateOf(Random.nextInt(1, 7)) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🎲 הטלת קובייה") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")[value - 1], fontSize = 78.sp, color = MaterialTheme.colorScheme.primary)
                Text("יצא המספר $value", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Button(onClick = { value = Random.nextInt(1, 7) }) { Text("גלגל שוב") }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}

@Composable
private fun CoinDialog(onDismiss: () -> Unit) {
    var result by remember { mutableStateOf(if (Random.nextBoolean()) "עץ" else "פלי") }
    var flips by remember { mutableIntStateOf(1) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🪙 עץ או פלי") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(if (result == "עץ") "🌳" else "🪙", fontSize = 66.sp)
                Text(result, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("הטלות: $flips", fontSize = 12.sp)
                Button(onClick = { result = if (Random.nextBoolean()) "עץ" else "פלי"; flips++ }) { Text("הטל שוב") }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}

@Composable
private fun SequenceDialog(onDismiss: () -> Unit) {
    var start by remember { mutableIntStateOf(Random.nextInt(1, 10)) }
    var step by remember { mutableIntStateOf(Random.nextInt(2, 7)) }
    var answer by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🔢 רצף מספרים") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("${start}, ${start + step}, ${start + step * 2}, ${start + step * 3}, ?", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(answer, { answer = it.filter(Char::isDigit).take(4) }, label = { Text("המספר הבא") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                Text("ניקוד: $score")
                if (message.isNotBlank()) Text(message, fontSize = 12.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (answer.toIntOrNull() == start + step * 4) {
                    score++
                    message = "נכון!"
                    start = Random.nextInt(1, 10)
                    step = Random.nextInt(2, 7)
                    answer = ""
                } else message = "נסה לחשב את ההפרש הקבוע."
            }) { Text("בדיקה") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("סגירה") } }
    )
}
