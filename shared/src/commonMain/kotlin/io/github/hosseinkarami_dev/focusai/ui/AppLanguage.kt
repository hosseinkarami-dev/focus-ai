package io.github.hosseinkarami_dev.focusai.ui

import androidx.compose.runtime.compositionLocalOf

val LocalAppLanguage = compositionLocalOf { "en" }

private val persian = mapOf(
    "Today" to "امروز", "Inbox" to "یادداشت‌ها", "Tasks" to "کارها", "Focus" to "تمرکز", "Settings" to "تنظیمات",
    "YOUR SPACE TO FOCUS" to "فضای شما برای تمرکز", "Make today count." to "امروز را پربار کن.", "YOUR DAILY FOCUS" to "تمرکز امروز شما",
    "One thing at\na time." to "هر بار\nیک کار.", "A little clarity\ngoes a long way." to "کمی وضوح\nتفاوت بزرگی می‌سازد.",
    "done" to "انجام‌شده", "Turn a thought into a plan" to "یک فکر را به برنامه تبدیل کن", "Capture a note and review task ideas" to "یادداشت کن و پیشنهادها را بررسی کن",
    "Your priorities" to "اولویت‌های شما", "left" to "باقی‌مانده", "A moment of focus" to "لحظه‌ای برای تمرکز", "Make space for one thing" to "برای یک کار جا باز کن",
    "Your day is open" to "روزت آماده است", "Add a task or start with a note." to "کاری اضافه کن یا با یک یادداشت شروع کن.", "Capture a thought" to "ثبت یک فکر",
    "Tasks" to "کارها", "A clear view of what’s on your plate." to "نمایی روشن از کارهای پیش رو.", "open" to "باز", "completed" to "انجام‌شده",
    "Search tasks" to "جستجوی کارها", "A clear slate" to "شروعی تازه", "Add a task when something needs your attention." to "هر وقت کاری نیاز به توجه داشت، اضافه‌اش کن.", "Add a task" to "افزودن کار",
    "No matching tasks" to "کاری پیدا نشد", "Try another search or filter." to "جستجو یا فیلتر دیگری را امتحان کن.", "Smart Inbox" to "صندوق هوشمند",
    "A safe place for everything on your mind." to "جایی امن برای هر چیزی که در ذهن داری.", "Search notes" to "جستجوی یادداشت‌ها", "Your inbox is clear" to "صندوق یادداشت خالی است",
    "Drop in a thought whenever it arrives.\nIt’ll be here when you need it." to "هر وقت فکری به ذهنت رسید ثبتش کن.\nهر زمان بخواهی در دسترس است.", "No matching notes" to "یادداشتی پیدا نشد",
    "Try a different search or filter." to "جستجو یا فیلتر دیگری را امتحان کن.", "Focus" to "تمرکز", "A little room to do your best work." to "فضایی برای بهترین کارهایت.",
    "FOCUS ON A TASK" to "تمرکز روی یک کار", "Just focus" to "فقط تمرکز", "Start focus" to "شروع تمرکز", "Finish" to "پایان", "Discard" to "دور انداختن",
    "RECENT SESSIONS" to "جلسه‌های اخیر", "Focus completed" to "تمرکز کامل شد", "Focus in progress" to "تمرکز در حال انجام", "Focus paused" to "تمرکز متوقف شد",
    "PAUSED" to "متوقف", "MINDFUL MINUTES" to "دقیقه‌های تمرکز", "STAY WITH IT" to "ادامه بده", "TIME TO FINISH" to "زمان پایان",
    "Take a breath, then begin." to "نفس عمیقی بکش و شروع کن.", "Your time, at your pace." to "زمان تو، با ریتم خودت.",
    "Discard this session?" to "این جلسه کنار گذاشته شود؟", "This focus time will not be included in your history." to "این زمان تمرکز در تاریخچه ذخیره نمی‌شود.", "Keep going" to "ادامه بده",
    "Task completed" to "کار انجام شد", "Task marked open" to "کار دوباره باز شد", "Undo" to "واگرد",
    "Make it yours" to "شخصی‌سازی", "A calmer setup, tuned to you." to "تنظیمی آرام‌تر، مخصوص شما.", "Appearance" to "ظاهر", "Choose the look that feels right." to "نمای دلخواهت را انتخاب کن.",
    "System" to "سیستم", "Light" to "روشن", "Dark" to "تیره", "Focus session" to "جلسه تمرکز", "Pick a comfortable starting length." to "مدت مناسب شروع را انتخاب کن.",
    "New focus sessions will use this duration." to "جلسه‌های جدید با این مدت شروع می‌شوند.", "Privacy" to "حریم خصوصی", "Nothing leaves your device during normal use." to "در استفاده عادی چیزی از دستگاه خارج نمی‌شود.",
    "Suggestions are generated on-device for this preview. Your note is never uploaded." to "پیشنهادها برای این پیش‌نمایش روی دستگاه ساخته می‌شوند. یادداشتت بارگذاری نمی‌شود.",
    "Productivity, with a little more clarity." to "بهره‌وری با کمی وضوح بیشتر.", "Language" to "زبان", "Choose your app language." to "زبان برنامه را انتخاب کن.",
    "Task" to "کار", "Details (optional)" to "جزئیات (اختیاری)", "Due date (optional)" to "مهلت (اختیاری)", "PRIORITY" to "اولویت",
    "Time estimate in minutes" to "زمان تخمینی به دقیقه", "Choose between 5 and 1,440 minutes" to "مدتی بین ۵ تا ۱۴۴۰ دقیقه انتخاب کن",
    "Steps" to "گام‌ها", "Add step" to "افزودن گام", "Break this task into smaller steps when helpful." to "در صورت نیاز، کار را به گام‌های کوچک‌تر تقسیم کن.", "Cancel" to "لغو",
    "Delete this task?" to "این کار حذف شود؟", "Delete this note?" to "این یادداشت حذف شود؟", "Keep task" to "نگه‌داشتن کار", "Keep note" to "نگه‌داشتن یادداشت",
    "This note will be permanently removed from this device." to "این یادداشت برای همیشه از دستگاه حذف می‌شود.", "Details" to "جزئیات", "Save note" to "ذخیره یادداشت",
    "I need to…" to "باید…", "Sample AI preview · no service is contacted" to "پیش‌نمایش هوش مصنوعی · سرویسی فراخوانی نمی‌شود",
    "Write it as it comes. Your note is saved here before any suggestions are reviewed." to "همان‌طور که به ذهنت می‌رسد بنویس. یادداشت پیش از بررسی پیشنهادها اینجا ذخیره می‌شود.",
    "Review task ideas" to "بررسی پیشنهادهای کار", "SAMPLE PREVIEW · NOTHING SAVED YET" to "پیش‌نمایش نمونه · هنوز چیزی ذخیره نشده",
    "Choose what you’d like to keep. You can edit each task later." to "موارد دلخواه را انتخاب کن. بعداً می‌توانی هر کار را ویرایش کنی.", "Keep editing" to "ادامه ویرایش"
)

private val spanish = mapOf(
    "Today" to "Hoy", "Inbox" to "Bandeja", "Tasks" to "Tareas", "Focus" to "Enfoque", "Settings" to "Ajustes",
    "YOUR SPACE TO FOCUS" to "TU ESPACIO PARA CONCENTRARTE", "Make today count." to "Haz que hoy cuente.", "YOUR DAILY FOCUS" to "TU ENFOQUE DIARIO",
    "One thing at\na time." to "Una cosa\na la vez.", "A little clarity\ngoes a long way." to "Un poco de claridad\nllega muy lejos.", "done" to "hechas",
    "Turn a thought into a plan" to "Convierte una idea en un plan", "Capture a note and review task ideas" to "Anota una idea y revisa sugerencias", "Your priorities" to "Tus prioridades", "left" to "pendientes",
    "A moment of focus" to "Un momento de enfoque", "Make space for one thing" to "Haz espacio para una cosa", "Your day is open" to "Tu día está despejado", "Add a task or start with a note." to "Añade una tarea o empieza con una nota.", "Capture a thought" to "Anotar una idea",
    "A clear view of what’s on your plate." to "Una vista clara de tus pendientes.", "open" to "abiertas", "completed" to "completadas", "Search tasks" to "Buscar tareas", "A clear slate" to "Todo despejado", "Add a task when something needs your attention." to "Añade una tarea cuando algo requiera tu atención.", "Add a task" to "Añadir tarea", "No matching tasks" to "No hay tareas coincidentes", "Try another search or filter." to "Prueba otra búsqueda o filtro.",
    "Smart Inbox" to "Bandeja inteligente", "A safe place for everything on your mind." to "Un lugar seguro para todo lo que tienes en mente.", "Search notes" to "Buscar notas", "Your inbox is clear" to "Tu bandeja está vacía", "Drop in a thought whenever it arrives.\nIt’ll be here when you need it." to "Anota tus ideas cuando surjan.\nEstarán aquí cuando las necesites.", "No matching notes" to "No hay notas coincidentes", "Try a different search or filter." to "Prueba otra búsqueda o filtro.",
    "A little room to do your best work." to "Un espacio para dar lo mejor de ti.", "FOCUS ON A TASK" to "ENFÓCATE EN UNA TAREA", "Just focus" to "Solo enfocarme", "Start focus" to "Empezar", "Finish" to "Terminar", "Discard" to "Descartar", "RECENT SESSIONS" to "SESIONES RECIENTES", "Focus completed" to "Enfoque completado", "Focus in progress" to "Enfoque en curso", "Focus paused" to "Enfoque en pausa", "PAUSED" to "EN PAUSA", "MINDFUL MINUTES" to "MINUTOS DE ENFOQUE", "STAY WITH IT" to "SIGUE ASÍ", "TIME TO FINISH" to "HORA DE TERMINAR", "Take a breath, then begin." to "Respira y empieza.", "Your time, at your pace." to "Tu tiempo, a tu ritmo.",
    "Discard this session?" to "¿Descartar esta sesión?", "This focus time will not be included in your history." to "Este tiempo no se incluirá en tu historial.", "Keep going" to "Continuar", "Task completed" to "Tarea completada", "Task marked open" to "Tarea reabierta", "Undo" to "Deshacer",
    "Make it yours" to "Personalízalo", "A calmer setup, tuned to you." to "Un espacio tranquilo, a tu medida.", "Appearance" to "Apariencia", "Choose the look that feels right." to "Elige el aspecto que prefieras.", "System" to "Sistema", "Light" to "Claro", "Dark" to "Oscuro", "Focus session" to "Sesión de enfoque", "Pick a comfortable starting length." to "Elige una duración cómoda.", "New focus sessions will use this duration." to "Las nuevas sesiones usarán esta duración.", "Privacy" to "Privacidad", "Nothing leaves your device during normal use." to "En el uso normal, nada sale de tu dispositivo.", "Suggestions are generated on-device for this preview. Your note is never uploaded." to "Las sugerencias se generan en tu dispositivo. Tu nota nunca se sube.", "Productivity, with a little more clarity." to "Productividad con un poco más de claridad.", "Language" to "Idioma", "Choose your app language." to "Elige el idioma de la aplicación.",
    "Task" to "Tarea", "Details (optional)" to "Detalles (opcional)", "Due date (optional)" to "Fecha límite (opcional)", "PRIORITY" to "PRIORIDAD", "Time estimate in minutes" to "Tiempo estimado en minutos", "Choose between 5 and 1,440 minutes" to "Elige entre 5 y 1.440 minutos", "Steps" to "Pasos", "Add step" to "Añadir paso", "Break this task into smaller steps when helpful." to "Divide la tarea en pasos más pequeños si te ayuda.", "Cancel" to "Cancelar", "Delete this task?" to "¿Eliminar esta tarea?", "Delete this note?" to "¿Eliminar esta nota?", "Keep task" to "Conservar tarea", "Keep note" to "Conservar nota", "This note will be permanently removed from this device." to "Esta nota se eliminará permanentemente del dispositivo.", "Save note" to "Guardar nota", "I need to…" to "Necesito…", "Sample AI preview · no service is contacted" to "Vista previa de IA · no se contacta ningún servicio", "Review task ideas" to "Revisar ideas de tareas", "SAMPLE PREVIEW · NOTHING SAVED YET" to "VISTA PREVIA · AÚN NO SE HA GUARDADO NADA", "Choose what you’d like to keep. You can edit each task later." to "Elige qué quieres conservar. Luego podrás editar cada tarea.", "Keep editing" to "Seguir editando"
)

private val persianExtra = mapOf(
    "All" to "همه", "Open" to "باز", "Completed" to "انجام‌شده", "To review" to "برای بررسی", "Reviewed" to "بررسی‌شده", "Archived" to "بایگانی‌شده",
    "Low" to "کم", "Medium" to "متوسط", "High" to "زیاد", "Use year-month-day format" to "از قالب سال-ماه-روز استفاده کن",
    "Enter a real calendar date, such as 2026-10-12" to "یک تاریخ معتبر وارد کن، مثل ۲۰۲۶-۱۰-۱۲", "YYYY-MM-DD" to "سال-ماه-روز",
    "Edit note" to "ویرایش یادداشت", "Untitled note" to "یادداشت بدون عنوان",
    "Soft lilac canvas and apricot accents." to "زمینه یاسی ملایم با رگه‌های زردآلویی.", "Ink surfaces with luminous lilac accents." to "سطوح جوهری با رگه‌های یاسی روشن.", "Matches your device appearance." to "هماهنگ با ظاهر دستگاه."
)
private val spanishExtra = mapOf(
    "All" to "Todas", "Open" to "Abiertas", "Completed" to "Completadas", "To review" to "Por revisar", "Reviewed" to "Revisadas", "Archived" to "Archivadas",
    "Low" to "Baja", "Medium" to "Media", "High" to "Alta", "Use year-month-day format" to "Usa el formato año-mes-día",
    "Enter a real calendar date, such as 2026-10-12" to "Introduce una fecha válida, como 2026-10-12", "YYYY-MM-DD" to "AAAA-MM-DD",
    "Edit note" to "Editar nota", "Untitled note" to "Nota sin título",
    "Soft lilac canvas and apricot accents." to "Fondo lila suave con toques de albaricoque.", "Ink surfaces with luminous lilac accents." to "Superficies tinta con acentos lilas luminosos.", "Matches your device appearance." to "Se adapta al aspecto del dispositivo."
)

fun localizedText(text: String, language: String): String {
    if (language == "en") return text
    val dictionary = if (language == "fa") persian else spanish
    dictionary[text]?.let { return it }
    val extra = if (language == "fa") persianExtra else spanishExtra
    extra[text]?.let { return it }
    return when {
        language == "fa" && text == "No tasks planned yet" -> "هنوز کاری برنامه‌ریزی نشده"
        language == "es" && text == "No tasks planned yet" -> "Aún no hay tareas planificadas"
        language == "fa" && text.endsWith(" min planned") -> "${text.removeSuffix(" min planned")} دقیقه برنامه‌ریزی‌شده"
        language == "es" && text.endsWith(" min planned") -> "${text.removeSuffix(" min planned")} min planificados"
        language == "fa" && text.startsWith("Add ") && text.endsWith(" tasks") -> "افزودن ${text.removePrefix("Add ").removeSuffix(" tasks")} کار"
        language == "es" && text.startsWith("Add ") && text.endsWith(" tasks") -> "Añadir ${text.removePrefix("Add ").removeSuffix(" tasks")} tareas"
        language == "fa" && text.startsWith("Due ") -> "مهلت ${text.removePrefix("Due ")}"
        language == "es" && text.startsWith("Due ") -> "Fecha límite: ${text.removePrefix("Due ")}"
        language == "fa" && text.startsWith("Updated ") -> "به‌روزرسانی ${text.removePrefix("Updated ")}"
        language == "es" && text.startsWith("Updated ") -> "Actualizada: ${text.removePrefix("Updated ")}"
        language == "fa" && text.endsWith(" min") -> "${text.removeSuffix(" min")} دقیقه"
        language == "es" && text.endsWith(" min") -> "${text.removeSuffix(" min")} min"
        language == "fa" && text.matches(Regex("\\d+ done")) -> "${text.substringBefore(' ')} انجام‌شده"
        language == "es" && text.matches(Regex("\\d+ done")) -> "${text.substringBefore(' ')} hechas"
        language == "fa" && text.matches(Regex("\\d+ left")) -> "${text.substringBefore(' ')} باقی‌مانده"
        language == "es" && text.matches(Regex("\\d+ left")) -> "${text.substringBefore(' ')} pendientes"
        language == "fa" && text.matches(Regex("\\d+ open  ·  \\d+ completed")) -> "${text.substringBefore(" open")} باز · ${text.substringAfter("·  ").removeSuffix(" completed")} انجام‌شده"
        language == "es" && text.matches(Regex("\\d+ open  ·  \\d+ completed")) -> "${text.substringBefore(" open")} abiertas · ${text.substringAfter("·  ").removeSuffix(" completed")} completadas"
        language == "fa" && text.startsWith("Step ") -> "گام ${text.removePrefix("Step ")}"
        language == "es" && text.startsWith("Step ") -> "Paso ${text.removePrefix("Step ")}"
        language == "fa" && text == "Add task" -> "افزودن کار"
        language == "es" && text == "Add task" -> "Añadir tarea"
        language == "fa" && text == "Capture a note" -> "ثبت یادداشت"
        language == "es" && text == "Capture a note" -> "Anotar"
        language == "fa" && text == "Start a focus session" -> "شروع جلسه تمرکز"
        language == "es" && text == "Start a focus session" -> "Empezar sesión de enfoque"
        language == "fa" && text == "Delete note" -> "حذف یادداشت"
        language == "es" && text == "Delete note" -> "Eliminar nota"
        language == "fa" && text == "Delete task" -> "حذف کار"
        language == "es" && text == "Delete task" -> "Eliminar tarea"
        language == "fa" && text.startsWith("Remove step ") -> "حذف گام ${text.removePrefix("Remove step ")}"
        language == "es" && text.startsWith("Remove step ") -> "Eliminar paso ${text.removePrefix("Remove step ")}"
        language == "fa" && text.startsWith("Move ") && text.endsWith(" up") -> "انتقال ${text.removePrefix("Move ").removeSuffix(" up")} به بالا"
        language == "es" && text.startsWith("Move ") && text.endsWith(" up") -> "Mover ${text.removePrefix("Move ").removeSuffix(" up")} arriba"
        language == "fa" && text.startsWith("Move ") && text.endsWith(" down") -> "انتقال ${text.removePrefix("Move ").removeSuffix(" down")} به پایین"
        language == "es" && text.startsWith("Move ") && text.endsWith(" down") -> "Mover ${text.removePrefix("Move ").removeSuffix(" down")} abajo"
        language == "fa" && text.startsWith("Edit ") -> "ویرایش ${text.removePrefix("Edit ")}"
        language == "es" && text.startsWith("Edit ") -> "Editar ${text.removePrefix("Edit ")}"
        language == "fa" && text.startsWith("“") && text.endsWith("” will be permanently removed.") -> "«${text.removePrefix("“").removeSuffix("” will be permanently removed.")}» برای همیشه حذف می‌شود."
        language == "es" && text.startsWith("“") && text.endsWith("” will be permanently removed.") -> "“${text.removePrefix("“").removeSuffix("” will be permanently removed.")}” se eliminará permanentemente."
        else -> text
    }
}
