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
    "Task completed" to "کار انجام شد", "Task marked open" to "کار دوباره باز شد", "Note deleted" to "یادداشت حذف شد", "Archive note" to "بایگانی یادداشت", "Restore note" to "بازیابی یادداشت", "Delete note" to "حذف یادداشت", "Complete task" to "انجام کار", "Reopen task" to "باز کردن دوباره کار", "Edit task" to "ویرایش کار", "Move task up" to "انتقال کار به بالا", "Move task down" to "انتقال کار به پایین", "Undo" to "واگرد",
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
    "Discard this session?" to "¿Descartar esta sesión?", "This focus time will not be included in your history." to "Este tiempo no se incluirá en tu historial.", "Keep going" to "Continuar", "Task completed" to "Tarea completada", "Task marked open" to "Tarea reabierta", "Note deleted" to "Nota eliminada", "Archive note" to "Archivar nota", "Restore note" to "Restaurar nota", "Delete note" to "Eliminar nota", "Complete task" to "Completar tarea", "Reopen task" to "Reabrir tarea", "Edit task" to "Editar tarea", "Move task up" to "Mover tarea arriba", "Move task down" to "Mover tarea abajo", "Undo" to "Deshacer",
    "Make it yours" to "Personalízalo", "A calmer setup, tuned to you." to "Un espacio tranquilo, a tu medida.", "Appearance" to "Apariencia", "Choose the look that feels right." to "Elige el aspecto que prefieras.", "System" to "Sistema", "Light" to "Claro", "Dark" to "Oscuro", "Focus session" to "Sesión de enfoque", "Pick a comfortable starting length." to "Elige una duración cómoda.", "New focus sessions will use this duration." to "Las nuevas sesiones usarán esta duración.", "Privacy" to "Privacidad", "Nothing leaves your device during normal use." to "En el uso normal, nada sale de tu dispositivo.", "Suggestions are generated on-device for this preview. Your note is never uploaded." to "Las sugerencias se generan en tu dispositivo. Tu nota nunca se sube.", "Productivity, with a little more clarity." to "Productividad con un poco más de claridad.", "Language" to "Idioma", "Choose your app language." to "Elige el idioma de la aplicación.",
    "Task" to "Tarea", "Details (optional)" to "Detalles (opcional)", "Due date (optional)" to "Fecha límite (opcional)", "PRIORITY" to "PRIORIDAD", "Time estimate in minutes" to "Tiempo estimado en minutos", "Choose between 5 and 1,440 minutes" to "Elige entre 5 y 1.440 minutos", "Steps" to "Pasos", "Add step" to "Añadir paso", "Break this task into smaller steps when helpful." to "Divide la tarea en pasos más pequeños si te ayuda.", "Cancel" to "Cancelar", "Delete this task?" to "¿Eliminar esta tarea?", "Delete this note?" to "¿Eliminar esta nota?", "Keep task" to "Conservar tarea", "Keep note" to "Conservar nota", "This note will be permanently removed from this device." to "Esta nota se eliminará permanentemente del dispositivo.", "Save note" to "Guardar nota", "I need to…" to "Necesito…", "Sample AI preview · no service is contacted" to "Vista previa de IA · no se contacta ningún servicio", "Review task ideas" to "Revisar ideas de tareas", "SAMPLE PREVIEW · NOTHING SAVED YET" to "VISTA PREVIA · AÚN NO SE HA GUARDADO NADA", "Choose what you’d like to keep. You can edit each task later." to "Elige qué quieres conservar. Luego podrás editar cada tarea.", "Keep editing" to "Seguir editando"
)

private val persianExtra = mapOf(
    "App language" to "زبان برنامه", "Choose language" to "انتخاب زبان", "Done" to "انجام شد", "Back" to "بازگشت", "New task" to "کار جدید",
    "A fresh start" to "شروعی تازه", "Refine your task" to "کار را کامل‌تر کن", "Give this task a little shape." to "کمی به این کار شکل بده.",
    "Task details" to "جزئیات کار", "Start with the outcome you want." to "با نتیجه‌ای که می‌خواهی شروع کن.", "Plan" to "برنامه", "Choose a priority, date, and realistic time estimate." to "اولویت، تاریخ و زمان واقع‌بینانه را انتخاب کن.",
    "Choose a date" to "انتخاب تاریخ", "Clear date" to "پاک کردن تاریخ", "Select date" to "ثبت تاریخ", "Break the task into smaller wins when it helps." to "اگر مفید است، کار را به گام‌های کوچک‌تر تقسیم کن.", "No steps yet" to "هنوز گامی اضافه نشده", "steps" to "گام",
    "Your rhythm" to "ریتم شما", "A clear look at your recent progress." to "نمایی روشن از پیشرفت اخیر شما.",
    "7 days" to "۷ روز", "30 days" to "۳۰ روز", "Focus time" to "زمان تمرکز", "Sessions" to "جلسه‌ها",
    "Tasks completed" to "کارهای انجام‌شده", "Avg. session" to "میانگین هر جلسه", "Completed focus sessions" to "جلسه‌های تمرکز کامل‌شده",
    "Rolling window ending now · completed sessions only for focus stats." to "بازه شناور تا اکنون · آمار تمرکز فقط جلسه‌های کامل‌شده را می‌شمارد.",
    "Focus time sums saved minutes; average divides by completed sessions." to "زمان تمرکز مجموع دقیقه‌های ثبت‌شده است؛ میانگین بر تعداد جلسه‌های کامل‌شده تقسیم می‌شود.",
    "Seven equal windows across the selected period · based on completed sessions." to "هفت بازه برابر در دوره انتخاب‌شده · بر اساس جلسه‌های کامل‌شده.",
    "Task completion history starts tracking from this update." to "ثبت تاریخچه تکمیل کارها از این به‌روزرسانی آغاز می‌شود.",
    "All" to "همه", "Open" to "باز", "Completed" to "انجام‌شده", "To review" to "برای بررسی", "Reviewed" to "بررسی‌شده", "Archived" to "بایگانی‌شده",
    "Low" to "کم", "Medium" to "متوسط", "High" to "زیاد", "Use year-month-day format" to "از قالب سال-ماه-روز استفاده کن",
        "Enter a real calendar date, such as 2026-10-12" to "یک تاریخ معتبر وارد کن، مثل ۲۰۲۶-۱۰-۱۲",
    "Edit note" to "ویرایش یادداشت", "Untitled note" to "یادداشت بدون عنوان",
        "Save changes" to "ذخیره تغییرات", "Suggest tasks" to "پیشنهاد کارها", "Reading…" to "در حال خواندن…",
        "Add a little more detail and try again." to "جزئیات بیشتری اضافه کن و دوباره امتحان کن.",
        "Couldn’t save this task. Please try again." to "ذخیره کار انجام نشد. دوباره امتحان کن.",
        "Couldn’t save this note. Please try again." to "ذخیره یادداشت انجام نشد. دوباره امتحان کن.",
        "Couldn’t create suggestions. Your note is saved; try again." to "ساخت پیشنهاد انجام نشد. یادداشت ذخیره شده؛ دوباره امتحان کن.",
    "Soft lilac canvas and apricot accents." to "زمینه یاسی ملایم با رگه‌های زردآلویی.", "Ink surfaces with luminous lilac accents." to "سطوح جوهری با رگه‌های یاسی روشن.", "Matches your device appearance." to "هماهنگ با ظاهر دستگاه."
)
private val spanishExtra = mapOf(
    "App language" to "Idioma de la aplicación", "Choose language" to "Elegir idioma", "Done" to "Listo", "Back" to "Volver", "New task" to "Nueva tarea",
    "A fresh start" to "Un nuevo comienzo", "Refine your task" to "Ajusta tu tarea", "Give this task a little shape." to "Dale forma a esta tarea.",
    "Task details" to "Detalles de la tarea", "Start with the outcome you want." to "Empieza por el resultado que buscas.", "Plan" to "Plan", "Choose a priority, date, and realistic time estimate." to "Elige prioridad, fecha y una duración realista.",
    "Choose a date" to "Elegir fecha", "Clear date" to "Quitar fecha", "Select date" to "Seleccionar fecha", "Break the task into smaller wins when it helps." to "Divide la tarea en pasos más pequeños si te ayuda.", "No steps yet" to "Aún no hay pasos", "steps" to "pasos",
    "Your rhythm" to "Tu ritmo", "A clear look at your recent progress." to "Una vista clara de tu progreso reciente.",
    "7 days" to "7 días", "30 days" to "30 días", "Focus time" to "Tiempo de enfoque", "Sessions" to "Sesiones",
    "Tasks completed" to "Tareas completadas", "Avg. session" to "Promedio por sesión", "Completed focus sessions" to "Sesiones de enfoque completadas",
    "Rolling window ending now · completed sessions only for focus stats." to "Periodo móvil hasta ahora · las estadísticas solo cuentan sesiones completadas.",
    "Focus time sums saved minutes; average divides by completed sessions." to "El tiempo suma los minutos guardados; el promedio se divide entre las sesiones completadas.",
    "Seven equal windows across the selected period · based on completed sessions." to "Siete intervalos iguales del periodo · según las sesiones completadas.",
    "Task completion history starts tracking from this update." to "El historial de tareas empieza a registrarse desde esta actualización.",
    "All" to "Todas", "Open" to "Abiertas", "Completed" to "Completadas", "To review" to "Por revisar", "Reviewed" to "Revisadas", "Archived" to "Archivadas",
    "Low" to "Baja", "Medium" to "Media", "High" to "Alta", "Use year-month-day format" to "Usa el formato año-mes-día",
        "Enter a real calendar date, such as 2026-10-12" to "Introduce una fecha válida, como 2026-10-12",
    "Edit note" to "Editar nota", "Untitled note" to "Nota sin título",
        "Save changes" to "Guardar cambios", "Suggest tasks" to "Sugerir tareas", "Reading…" to "Leyendo…",
        "Add a little more detail and try again." to "Añade un poco más de detalle e inténtalo de nuevo.",
        "Couldn’t save this task. Please try again." to "No se pudo guardar la tarea. Inténtalo de nuevo.",
        "Couldn’t save this note. Please try again." to "No se pudo guardar la nota. Inténtalo de nuevo.",
        "Couldn’t create suggestions. Your note is saved; try again." to "No se pudieron crear sugerencias. La nota está guardada; inténtalo de nuevo.",
    "Soft lilac canvas and apricot accents." to "Fondo lila suave con toques de albaricoque.", "Ink surfaces with luminous lilac accents." to "Superficies tinta con acentos lilas luminosos.", "Matches your device appearance." to "Se adapta al aspecto del dispositivo."
)

private val french = mapOf(
    "Today" to "Aujourd’hui", "Inbox" to "Boîte de réception", "Tasks" to "Tâches", "Focus" to "Concentration", "Settings" to "Réglages",
    "YOUR SPACE TO FOCUS" to "VOTRE ESPACE DE CONCENTRATION", "Make today count." to "Faites de cette journée un succès.", "YOUR DAILY FOCUS" to "VOTRE CONCENTRATION DU JOUR", "One thing at\na time." to "Une chose à la\nfois.", "A little clarity\ngoes a long way." to "Un peu de clarté\nchange tout.", "done" to "terminées", "left" to "restantes", "Your priorities" to "Vos priorités",
    "Turn a thought into a plan" to "Transformez une idée en plan", "Capture a note and review task ideas" to "Notez une idée et examinez les tâches suggérées", "A moment of focus" to "Un instant de concentration", "Make space for one thing" to "Faites de la place pour une chose", "Your day is open" to "Votre journée est libre", "Add a task or start with a note." to "Ajoutez une tâche ou commencez par une note.", "Capture a thought" to "Noter une idée", "A clear view of what’s on your plate." to "Une vue claire de vos priorités.", "open" to "ouvertes", "completed" to "terminées", "Search tasks" to "Rechercher des tâches", "A clear slate" to "Tout est clair", "Add a task when something needs your attention." to "Ajoutez une tâche dès qu’un sujet demande votre attention.", "Add task" to "Ajouter une tâche", "No matching tasks" to "Aucune tâche correspondante", "Try another search or filter." to "Essayez une autre recherche ou un autre filtre.",
    "Smart Inbox" to "Boîte de réception", "A safe place for everything on your mind." to "Un espace sûr pour toutes vos pensées.", "Search notes" to "Rechercher des notes", "Your inbox is clear" to "Votre boîte est vide", "Drop in a thought whenever it arrives.\nIt’ll be here when you need it." to "Notez vos idées dès qu’elles arrivent.\nElles seront là quand vous en aurez besoin.", "No matching notes" to "Aucune note correspondante", "Try a different search or filter." to "Essayez une autre recherche ou un autre filtre.",
    "A little room to do your best work." to "Un espace pour donner le meilleur de vous-même.", "FOCUS ON A TASK" to "SE CONCENTRER SUR UNE TÂCHE", "Just focus" to "Concentration libre", "Start focus" to "Commencer", "Finish" to "Terminer", "Discard" to "Abandonner", "RECENT SESSIONS" to "SESSIONS RÉCENTES", "Focus completed" to "Session terminée", "Focus in progress" to "Session en cours", "Focus paused" to "Session en pause", "PAUSED" to "EN PAUSE", "MINDFUL MINUTES" to "MINUTES DE CONCENTRATION", "STAY WITH IT" to "CONTINUEZ", "TIME TO FINISH" to "IL EST TEMPS DE TERMINER", "Take a breath, then begin." to "Respirez, puis commencez.", "Your time, at your pace." to "Votre temps, à votre rythme.", "Discard this session?" to "Abandonner cette session ?", "This focus time will not be included in your history." to "Cette durée ne sera pas ajoutée à votre historique.", "Keep going" to "Continuer", "Task completed" to "Tâche terminée", "Task marked open" to "Tâche rouverte", "Note deleted" to "Note supprimée", "Archive note" to "Archiver la note", "Restore note" to "Restaurer la note", "Delete note" to "Supprimer la note", "Complete task" to "Terminer la tâche", "Reopen task" to "Rouvrir la tâche", "Edit task" to "Modifier la tâche", "Move task up" to "Déplacer la tâche vers le haut", "Move task down" to "Déplacer la tâche vers le bas", "Undo" to "Annuler",
    "Make it yours" to "Personnalisez", "A calmer setup, tuned to you." to "Un espace apaisant, adapté à vos besoins.", "Appearance" to "Apparence", "Choose the look that feels right." to "Choisissez le thème qui vous convient.", "System" to "Système", "Light" to "Clair", "Dark" to "Sombre", "Focus session" to "Session de concentration", "Pick a comfortable starting length." to "Choisissez une durée confortable.", "New focus sessions will use this duration." to "Les nouvelles sessions utiliseront cette durée.", "Privacy" to "Confidentialité", "Nothing leaves your device during normal use." to "En usage normal, vos données restent sur cet appareil.", "Suggestions are generated on-device for this preview. Your note is never uploaded." to "Les suggestions sont générées sur l’appareil. Votre note n’est jamais envoyée.", "Productivity, with a little more clarity." to "De la productivité en toute clarté.", "Language" to "Langue", "Choose your app language." to "Choisissez la langue de l’application.",
    "Task" to "Tâche", "Details (optional)" to "Détails (facultatif)", "Due date (optional)" to "Échéance (facultative)", "PRIORITY" to "PRIORITÉ", "Time estimate in minutes" to "Durée estimée en minutes", "Choose between 5 and 1,440 minutes" to "Choisissez entre 5 et 1 440 minutes", "Steps" to "Étapes", "Add step" to "Ajouter une étape", "Break this task into smaller steps when helpful." to "Divisez cette tâche en petites étapes si besoin.", "Cancel" to "Annuler", "Delete this task?" to "Supprimer cette tâche ?", "Delete this note?" to "Supprimer cette note ?", "Keep task" to "Garder la tâche", "Keep note" to "Garder la note", "This note will be permanently removed from this device." to "Cette note sera définitivement supprimée de cet appareil.", "Details" to "Détails", "Save note" to "Enregistrer la note", "I need to…" to "Je dois…", "Sample AI preview · no service is contacted" to "Aperçu IA · aucun service n’est contacté", "Write it as it comes. Your note is saved here before any suggestions are reviewed." to "Écrivez librement. Votre note est enregistrée ici avant l’examen des suggestions.", "Review task ideas" to "Examiner les tâches proposées", "SAMPLE PREVIEW · NOTHING SAVED YET" to "APERÇU · RIEN N’EST ENCORE ENREGISTRÉ", "Choose what you’d like to keep. You can edit each task later." to "Choisissez les éléments à garder. Vous pourrez les modifier plus tard.", "Keep editing" to "Continuer la modification",
    "Your rhythm" to "Votre rythme", "A clear look at your recent progress." to "Un aperçu clair de vos progrès récents.", "7 days" to "7 jours", "30 days" to "30 jours", "Focus time" to "Temps de concentration", "Sessions" to "Sessions", "Tasks completed" to "Tâches terminées", "Avg. session" to "Durée moyenne", "Completed focus sessions" to "Sessions terminées", "Rolling window ending now · completed sessions only for focus stats." to "Période glissante jusqu’à maintenant · seules les sessions terminées comptent.", "Focus time sums saved minutes; average divides by completed sessions." to "Le temps totalise les minutes enregistrées ; la moyenne est calculée sur les sessions terminées.", "Seven equal windows across the selected period · based on completed sessions." to "Sept intervalles égaux sur la période · selon les sessions terminées.", "Task completion history starts tracking from this update." to "Le suivi des tâches terminées commence avec cette version.",
    "App language" to "Langue de l’application", "Choose language" to "Choisir la langue", "Done" to "Terminé", "Back" to "Retour", "New task" to "Nouvelle tâche", "A fresh start" to "Un nouveau départ", "Refine your task" to "Affiner votre tâche", "Give this task a little shape." to "Donnez forme à cette tâche.", "Task details" to "Détails de la tâche", "Start with the outcome you want." to "Commencez par le résultat souhaité.", "Plan" to "Planifier", "Choose a priority, date, and realistic time estimate." to "Choisissez une priorité, une date et une durée réaliste.", "Choose a date" to "Choisir une date", "Clear date" to "Effacer la date", "Select date" to "Sélectionner la date", "Break the task into smaller wins when it helps." to "Découpez la tâche en petites étapes si cela vous aide.", "No steps yet" to "Aucune étape", "steps" to "étapes", "Save changes" to "Enregistrer les modifications", "Low" to "Basse", "Medium" to "Moyenne", "High" to "Haute", "English" to "Anglais", "Persian" to "Persan", "Spanish" to "Espagnol", "French" to "Français", "Arabic" to "Arabe", "Capture a note" to "Noter une idée", "Save task" to "Enregistrer la tâche", "Delete task" to "Supprimer la tâche"
)

private val arabic = mapOf(
    "Today" to "اليوم", "Inbox" to "الوارد", "Tasks" to "المهام", "Focus" to "التركيز", "Settings" to "الإعدادات", "YOUR SPACE TO FOCUS" to "مساحتك للتركيز", "Make today count." to "اجعل يومك مثمراً.", "YOUR DAILY FOCUS" to "تركيزك اليومي", "One thing at\na time." to "شيء واحد\nفي كل مرة.", "A little clarity\ngoes a long way." to "قليل من الوضوح\nيصنع فرقاً كبيراً.", "done" to "مكتملة", "left" to "متبقية", "Your priorities" to "أولوياتك",
    "Turn a thought into a plan" to "حوّل فكرة إلى خطة", "Capture a note and review task ideas" to "دوّن فكرة وراجع المهام المقترحة", "A moment of focus" to "لحظة تركيز", "Make space for one thing" to "خصص وقتاً لشيء واحد", "Your day is open" to "يومك متاح", "Add a task or start with a note." to "أضف مهمة أو ابدأ بملاحظة.", "Capture a thought" to "دوّن فكرة", "A clear view of what’s on your plate." to "نظرة واضحة على مهامك.", "open" to "مفتوحة", "completed" to "مكتملة", "Search tasks" to "ابحث في المهام", "A clear slate" to "بداية جديدة", "Add a task when something needs your attention." to "أضف مهمة عندما تحتاج إلى اهتمامك.", "Add task" to "إضافة مهمة", "No matching tasks" to "لا توجد مهام مطابقة", "Try another search or filter." to "جرّب بحثاً أو تصفية أخرى.",
    "Smart Inbox" to "الوارد الذكي", "A safe place for everything on your mind." to "مساحة آمنة لكل ما يشغل بالك.", "Search notes" to "ابحث في الملاحظات", "Your inbox is clear" to "الوارد فارغ", "Drop in a thought whenever it arrives.\nIt’ll be here when you need it." to "دوّن أفكارك فور ورودها.\nستجدها هنا عندما تحتاج إليها.", "No matching notes" to "لا توجد ملاحظات مطابقة", "Try a different search or filter." to "جرّب بحثاً أو تصفية مختلفة.", "A little room to do your best work." to "مساحة صغيرة لتنجز أفضل ما لديك.", "FOCUS ON A TASK" to "التركيز على مهمة", "Just focus" to "تركيز فقط", "Start focus" to "ابدأ التركيز", "Finish" to "إنهاء", "Discard" to "تجاهل", "RECENT SESSIONS" to "الجلسات الأخيرة", "Focus completed" to "اكتملت جلسة التركيز", "Focus in progress" to "جلسة التركيز جارية", "Focus paused" to "جلسة التركيز متوقفة", "PAUSED" to "متوقف مؤقتاً", "MINDFUL MINUTES" to "دقائق التركيز", "STAY WITH IT" to "واصل", "TIME TO FINISH" to "حان وقت الإنهاء", "Take a breath, then begin." to "تنفس بعمق ثم ابدأ.", "Your time, at your pace." to "وقتك وبالوتيرة التي تناسبك.", "Discard this session?" to "هل تريد تجاهل هذه الجلسة؟", "This focus time will not be included in your history." to "لن تُضاف مدة التركيز هذه إلى السجل.", "Keep going" to "تابع", "Task completed" to "اكتملت المهمة", "Task marked open" to "أعيد فتح المهمة", "Note deleted" to "حُذفت الملاحظة", "Archive note" to "أرشفة الملاحظة", "Restore note" to "استعادة الملاحظة", "Delete note" to "حذف الملاحظة", "Complete task" to "إكمال المهمة", "Reopen task" to "إعادة فتح المهمة", "Edit task" to "تعديل المهمة", "Move task up" to "نقل المهمة إلى الأعلى", "Move task down" to "نقل المهمة إلى الأسفل", "Undo" to "تراجع",
    "Make it yours" to "اجعلها تناسبك", "A calmer setup, tuned to you." to "إعداد هادئ يناسبك.", "Appearance" to "المظهر", "Choose the look that feels right." to "اختر المظهر المناسب لك.", "System" to "النظام", "Light" to "فاتح", "Dark" to "داكن", "Focus session" to "جلسة تركيز", "Pick a comfortable starting length." to "اختر مدة بداية مريحة.", "New focus sessions will use this duration." to "ستستخدم جلسات التركيز الجديدة هذه المدة.", "Privacy" to "الخصوصية", "Nothing leaves your device during normal use." to "تبقى بياناتك على جهازك أثناء الاستخدام العادي.", "Suggestions are generated on-device for this preview. Your note is never uploaded." to "تُنشأ الاقتراحات على الجهاز. لا تُرفع ملاحظتك أبداً.", "Productivity, with a little more clarity." to "إنجاز أوضح وأكثر هدوءاً.", "Language" to "اللغة", "Choose your app language." to "اختر لغة التطبيق.",
    "Task" to "المهمة", "Details (optional)" to "التفاصيل (اختياري)", "Due date (optional)" to "موعد التسليم (اختياري)", "PRIORITY" to "الأولوية", "Time estimate in minutes" to "الوقت المتوقع بالدقائق", "Choose between 5 and 1,440 minutes" to "اختر مدة بين ٥ و١٤٤٠ دقيقة", "Steps" to "الخطوات", "Add step" to "إضافة خطوة", "Break this task into smaller steps when helpful." to "قسّم المهمة إلى خطوات أصغر عند الحاجة.", "Cancel" to "إلغاء", "Delete this task?" to "هل تريد حذف هذه المهمة؟", "Delete this note?" to "هل تريد حذف هذه الملاحظة؟", "Keep task" to "الاحتفاظ بالمهمة", "Keep note" to "الاحتفاظ بالملاحظة", "This note will be permanently removed from this device." to "ستُحذف هذه الملاحظة نهائياً من الجهاز.", "Details" to "التفاصيل", "Save note" to "حفظ الملاحظة", "I need to…" to "أحتاج إلى…", "Sample AI preview · no service is contacted" to "معاينة الذكاء الاصطناعي · لا يتم الاتصال بأي خدمة", "Write it as it comes. Your note is saved here before any suggestions are reviewed." to "اكتب ما يخطر ببالك. تُحفظ ملاحظتك هنا قبل مراجعة الاقتراحات.", "Review task ideas" to "مراجعة أفكار المهام", "SAMPLE PREVIEW · NOTHING SAVED YET" to "معاينة · لم يُحفظ شيء بعد", "Choose what you’d like to keep. You can edit each task later." to "اختر ما تريد الاحتفاظ به. يمكنك تعديل كل مهمة لاحقاً.", "Keep editing" to "متابعة التعديل",
    "Your rhythm" to "إيقاعك", "A clear look at your recent progress." to "نظرة واضحة على تقدمك مؤخراً.", "7 days" to "٧ أيام", "30 days" to "٣٠ يوماً", "Focus time" to "وقت التركيز", "Sessions" to "الجلسات", "Tasks completed" to "المهام المكتملة", "Avg. session" to "متوسط الجلسة", "Completed focus sessions" to "جلسات التركيز المكتملة", "Rolling window ending now · completed sessions only for focus stats." to "فترة متحركة تنتهي الآن · تُحتسب الجلسات المكتملة فقط لإحصاءات التركيز.", "Focus time sums saved minutes; average divides by completed sessions." to "وقت التركيز هو مجموع الدقائق المحفوظة؛ ويُحسب المتوسط على الجلسات المكتملة.", "Seven equal windows across the selected period · based on completed sessions." to "سبع فترات متساوية ضمن المدة المحددة · وفق الجلسات المكتملة.", "Task completion history starts tracking from this update." to "يبدأ تسجيل إكمال المهام من هذا التحديث.",
    "App language" to "لغة التطبيق", "Choose language" to "اختيار اللغة", "Done" to "تم", "Back" to "رجوع", "New task" to "مهمة جديدة", "A fresh start" to "بداية جديدة", "Refine your task" to "حسّن المهمة", "Give this task a little shape." to "أضف بعض التفاصيل إلى هذه المهمة.", "Task details" to "تفاصيل المهمة", "Start with the outcome you want." to "ابدأ بالنتيجة التي تريدها.", "Plan" to "التخطيط", "Choose a priority, date, and realistic time estimate." to "اختر الأولوية والتاريخ والوقت المتوقع.", "Choose a date" to "اختر تاريخاً", "Clear date" to "مسح التاريخ", "Select date" to "تحديد التاريخ", "Break the task into smaller wins when it helps." to "قسّم المهمة إلى خطوات صغيرة عند الحاجة.", "No steps yet" to "لا توجد خطوات بعد", "steps" to "خطوات", "Save changes" to "حفظ التغييرات", "Low" to "منخفضة", "Medium" to "متوسطة", "High" to "عالية", "English" to "الإنجليزية", "Persian" to "الفارسية", "Spanish" to "الإسبانية", "French" to "الفرنسية", "Arabic" to "العربية", "Capture a note" to "دوّن ملاحظة", "Save task" to "حفظ المهمة", "Delete task" to "حذف المهمة"
)

private val completedCountPattern = Regex("\\d+ done")
private val remainingCountPattern = Regex("\\d+ left")
private val openCompletedCountPattern = Regex("\\d+ open  ·  \\d+ completed")
private val stepCountPattern = Regex("\\d+ steps")

fun localizedText(text: String, language: String): String {
    if (language == "en") return text
    focusCopyByLanguage[language]?.get(text)?.let { return it }
    if (text == "Notification access is off. Allow notifications to receive reminders.") {
        return when (language) {
            "fa" -> "اعلان‌ها خاموش هستند. برای دریافت یادآوری، اجازهٔ اعلان‌ها را فعال کنید."
            "es" -> "Las notificaciones están desactivadas. Permítelas para recibir recordatorios."
            "fr" -> "Les notifications sont désactivées. Autorisez-les pour recevoir des rappels."
            "ar" -> "الإشعارات متوقفة. اسمح بالإشعارات لتلقي التذكيرات."
            else -> text
        }
    }
    val recurringCopy = when (language) {
        "fa" -> mapOf("Repeat" to "تکرار", "Never" to "هرگز", "Daily" to "روزانه", "Weekly" to "هفتگی", "Monthly" to "ماهانه", "Choose a due date to repeat this task." to "برای تکرار این کار، تاریخ سررسید را انتخاب کنید.", "Reminder" to "یادآوری", "Choose reminder time" to "انتخاب زمان یادآوری", "Clear reminder" to "پاک کردن یادآوری", "Choose a due date before setting a reminder." to "پیش از تنظیم یادآوری، تاریخ سررسید را انتخاب کنید.", "Notification settings" to "تنظیمات اعلان", "Couldn’t schedule the reminder. Check notification access and choose a future time." to "یادآوری تنظیم نشد. دسترسی اعلان را بررسی و زمان آینده‌ای انتخاب کنید.", "Reminder time" to "زمان یادآوری", "Select time" to "انتخاب زمان", "Task added" to "کار اضافه شد", "Task updated" to "کار به‌روزرسانی شد", "List" to "فهرست", "Week" to "هفته", "Previous week" to "هفته قبل", "Next week" to "هفته بعد", "This week" to "این هفته", "No tasks for this day" to "کاری برای این روز نیست", "Unscheduled" to "بدون زمان‌بندی")
        "es" -> mapOf("Repeat" to "Repetir", "Never" to "Nunca", "Daily" to "Diariamente", "Weekly" to "Semanalmente", "Monthly" to "Mensualmente", "Choose a due date to repeat this task." to "Elige una fecha límite para repetir esta tarea.", "Reminder" to "Recordatorio", "Choose reminder time" to "Elegir hora del recordatorio", "Clear reminder" to "Borrar recordatorio", "Choose a due date before setting a reminder." to "Elige una fecha límite antes de configurar un recordatorio.", "Notification settings" to "Ajustes de notificaciones", "Couldn’t schedule the reminder. Check notification access and choose a future time." to "No se pudo programar el recordatorio. Revisa los permisos y elige una hora futura.", "Reminder time" to "Hora del recordatorio", "Select time" to "Seleccionar hora", "Task added" to "Tarea añadida", "Task updated" to "Tarea actualizada", "List" to "Lista", "Week" to "Semana", "Previous week" to "Semana anterior", "Next week" to "Semana siguiente", "This week" to "Esta semana", "No tasks for this day" to "No hay tareas para este día", "Unscheduled" to "Sin fecha")
        "fr" -> mapOf("Repeat" to "Répéter", "Never" to "Jamais", "Daily" to "Chaque jour", "Weekly" to "Chaque semaine", "Monthly" to "Chaque mois", "Choose a due date to repeat this task." to "Choisissez une échéance pour répéter cette tâche.", "Reminder" to "Rappel", "Choose reminder time" to "Choisir l’heure du rappel", "Clear reminder" to "Effacer le rappel", "Choose a due date before setting a reminder." to "Choisissez une échéance avant de définir un rappel.", "Notification settings" to "Réglages des notifications", "Couldn’t schedule the reminder. Check notification access and choose a future time." to "Impossible de programmer le rappel. Vérifiez les notifications et choisissez une heure future.", "Reminder time" to "Heure du rappel", "Select time" to "Choisir l’heure", "Task added" to "Tâche ajoutée", "Task updated" to "Tâche mise à jour", "List" to "Liste", "Week" to "Semaine", "Previous week" to "Semaine précédente", "Next week" to "Semaine suivante", "This week" to "Cette semaine", "No tasks for this day" to "Aucune tâche pour ce jour", "Unscheduled" to "Sans date")
        "ar" -> mapOf("Repeat" to "التكرار", "Never" to "أبداً", "Daily" to "يومياً", "Weekly" to "أسبوعياً", "Monthly" to "شهرياً", "Choose a due date to repeat this task." to "اختر موعداً لتكرار هذه المهمة.", "Reminder" to "تذكير", "Choose reminder time" to "اختر وقت التذكير", "Clear reminder" to "مسح التذكير", "Choose a due date before setting a reminder." to "اختر موعداً قبل ضبط التذكير.", "Notification settings" to "إعدادات الإشعارات", "Couldn’t schedule the reminder. Check notification access and choose a future time." to "تعذر جدولة التذكير. تحقق من الإشعارات واختر وقتاً مستقبلياً.", "Reminder time" to "وقت التذكير", "Select time" to "اختيار الوقت", "Task added" to "تمت إضافة المهمة", "Task updated" to "تم تحديث المهمة", "List" to "القائمة", "Week" to "الأسبوع", "Previous week" to "الأسبوع السابق", "Next week" to "الأسبوع التالي", "This week" to "هذا الأسبوع", "No tasks for this day" to "لا توجد مهام لهذا اليوم", "Unscheduled" to "بلا موعد")
        else -> emptyMap()
    }
    recurringCopy[text]?.let { return it }
    val dictionary = when (language) { "fa" -> persian; "es" -> spanish; "fr" -> french; "ar" -> arabic; else -> persian }
    dictionary[text]?.let { return it }
    val extra = when (language) { "fa" -> persianExtra; "es" -> spanishExtra; else -> emptyMap() }
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
        language == "fr" && text.startsWith("Due ") -> "Échéance : ${text.removePrefix("Due ")}"
        language == "ar" && text.startsWith("Due ") -> "الموعد: ${text.removePrefix("Due ")}"
        language == "fa" && text.startsWith("Updated ") -> "به‌روزرسانی ${text.removePrefix("Updated ")}"
        language == "es" && text.startsWith("Updated ") -> "Actualizada: ${text.removePrefix("Updated ")}"
        language == "fa" && text.endsWith(" min") -> "${text.removeSuffix(" min")} دقیقه"
        language == "es" && text.endsWith(" min") -> "${text.removeSuffix(" min")} min"
        language == "fr" && text.endsWith(" min") -> "${text.removeSuffix(" min")} min"
        language == "ar" && text.endsWith(" min") -> "${text.removeSuffix(" min")} د"
        language == "fa" && text.matches(completedCountPattern) -> "${text.substringBefore(' ')} انجام‌شده"
        language == "es" && text.matches(completedCountPattern) -> "${text.substringBefore(' ')} hechas"
        language == "fa" && text.matches(remainingCountPattern) -> "${text.substringBefore(' ')} باقی‌مانده"
        language == "es" && text.matches(remainingCountPattern) -> "${text.substringBefore(' ')} pendientes"
        language == "fa" && text.matches(stepCountPattern) -> "${text.substringBefore(' ')} گام"
        language == "es" && text.matches(stepCountPattern) -> "${text.substringBefore(' ')} pasos"
        language == "fr" && text.matches(stepCountPattern) -> "${text.substringBefore(' ')} étape${if (text.substringBefore(' ') == "1") "" else "s"}"
        language == "ar" && text.matches(stepCountPattern) -> "${text.substringBefore(' ')} ${if (text.substringBefore(' ') == "1") "خطوة" else "خطوات"}"
        language == "fa" && text.matches(openCompletedCountPattern) -> "${text.substringBefore(" open")} باز · ${text.substringAfter("·  ").removeSuffix(" completed")} انجام‌شده"
        language == "es" && text.matches(openCompletedCountPattern) -> "${text.substringBefore(" open")} abiertas · ${text.substringAfter("·  ").removeSuffix(" completed")} completadas"
        language == "fa" && text.startsWith("Step ") -> "گام ${text.removePrefix("Step ")}"
        language == "es" && text.startsWith("Step ") -> "Paso ${text.removePrefix("Step ")}"
        language == "fr" && text.startsWith("Step ") -> "Étape ${text.removePrefix("Step ")}"
        language == "ar" && text.startsWith("Step ") -> "الخطوة ${text.removePrefix("Step ")}"
        language == "fa" && text == "Add task" -> "افزودن کار"
        language == "es" && text == "Add task" -> "Añadir tarea"
        language == "fa" && text == "Save task" -> "ذخیره کار"
        language == "es" && text == "Save task" -> "Guardar tarea"
        language == "fa" && text == "Capture a note" -> "ثبت یادداشت"
        language == "es" && text == "Capture a note" -> "Anotar"
        language == "fa" && text == "Start a focus session" -> "شروع جلسه تمرکز"
        language == "es" && text == "Start a focus session" -> "Empezar sesión de enfoque"
        language == "fa" && text == "Delete note" -> "حذف یادداشت"
        language == "es" && text == "Delete note" -> "Eliminar nota"
        language == "fa" && text == "Dismiss" -> "بستن"
        language == "es" && text == "Dismiss" -> "Cerrar"
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

private val focusCopyByLanguage = mapOf(
    "fa" to mapOf(
        "Today's focus goal" to "هدف تمرکز امروز", "Build a steady rhythm, one session at a time." to "با هر جلسه، ریتمی پایدار بسازید.", "of" to "از",
        "Daily focus goal" to "هدف روزانهٔ تمرکز", "Set a gentle target for your focused time." to "برای زمان تمرکزتان هدفی متعادل تعیین کنید.", "Your progress appears on the Focus screen." to "پیشرفت شما در صفحهٔ تمرکز نمایش داده می‌شود.",
        "Break reminders" to "یادآوری استراحت", "Get a gentle nudge after completing a focus session." to "پس از پایان جلسهٔ تمرکز، یادآوری ملایمی دریافت کنید.",
        "Remind me to take a break" to "برای استراحت یادآوری کن", "A short pause helps you reset." to "وقفه‌ای کوتاه به بازیابی تمرکز کمک می‌کند.",
        "Notification access is off. Enable it to receive break reminders." to "اعلان‌ها خاموش هستند. برای دریافت یادآوری استراحت، آن‌ها را فعال کنید.",
        "FocusAI break" to "استراحت FocusAI", "Great work. Take a short break." to "عالی بود. کمی استراحت کنید.", "Focus session complete" to "جلسهٔ تمرکز تمام شد",
        "Sort" to "مرتب‌سازی", "Sort tasks" to "مرتب‌سازی کارها", "My order" to "ترتیب من", "Due date" to "مهلت", "Priority" to "اولویت", "Title" to "عنوان"
    ),
    "es" to mapOf(
        "Today's focus goal" to "Meta de enfoque de hoy", "Build a steady rhythm, one session at a time." to "Crea un ritmo constante, sesión a sesión.", "of" to "de",
        "Daily focus goal" to "Meta diaria de enfoque", "Set a gentle target for your focused time." to "Elige un objetivo realista para tu tiempo de enfoque.", "Your progress appears on the Focus screen." to "Tu progreso aparece en la pantalla Enfoque.",
        "Break reminders" to "Recordatorios de descanso", "Get a gentle nudge after completing a focus session." to "Recibe un aviso amable al terminar una sesión.",
        "Remind me to take a break" to "Recordarme descansar", "A short pause helps you reset." to "Una pausa breve te ayuda a recargar.",
        "Notification access is off. Enable it to receive break reminders." to "Las notificaciones están desactivadas. Actívalas para recibir recordatorios de descanso.",
        "FocusAI break" to "Descanso de FocusAI", "Great work. Take a short break." to "¡Buen trabajo! Tómate un breve descanso.", "Focus session complete" to "Sesión de enfoque completada",
        "Sort" to "Ordenar", "Sort tasks" to "Ordenar tareas", "My order" to "Mi orden", "Due date" to "Fecha límite", "Priority" to "Prioridad", "Title" to "Título"
    ),
    "fr" to mapOf(
        "Today's focus goal" to "Objectif de concentration du jour", "Build a steady rhythm, one session at a time." to "Trouvez votre rythme, une session à la fois.", "of" to "sur",
        "Daily focus goal" to "Objectif quotidien", "Set a gentle target for your focused time." to "Choisissez un objectif adapté à votre temps de concentration.", "Your progress appears on the Focus screen." to "Votre progression apparaît sur l’écran Concentration.",
        "Break reminders" to "Rappels de pause", "Get a gentle nudge after completing a focus session." to "Recevez un rappel à la fin d’une session.",
        "Remind me to take a break" to "Me rappeler de faire une pause", "A short pause helps you reset." to "Une courte pause aide à reprendre son souffle.",
        "Notification access is off. Enable it to receive break reminders." to "Les notifications sont désactivées. Activez-les pour recevoir les rappels de pause.",
        "FocusAI break" to "Pause FocusAI", "Great work. Take a short break." to "Bravo. Faites une courte pause.", "Focus session complete" to "Session de concentration terminée",
        "Sort" to "Trier", "Sort tasks" to "Trier les tâches", "My order" to "Mon ordre", "Due date" to "Échéance", "Priority" to "Priorité", "Title" to "Titre"
    ),
    "ar" to mapOf(
        "Today's focus goal" to "هدف التركيز اليوم", "Build a steady rhythm, one session at a time." to "أنشئ إيقاعاً ثابتاً، جلسة بعد جلسة.", "of" to "من",
        "Daily focus goal" to "هدف التركيز اليومي", "Set a gentle target for your focused time." to "حدد هدفاً مناسباً لوقت تركيزك.", "Your progress appears on the Focus screen." to "يظهر تقدمك في شاشة التركيز.",
        "Break reminders" to "تذكيرات الاستراحة", "Get a gentle nudge after completing a focus session." to "احصل على تذكير لطيف بعد إكمال جلسة تركيز.",
        "Remind me to take a break" to "ذكّرني بأخذ استراحة", "A short pause helps you reset." to "استراحة قصيرة تساعدك على استعادة نشاطك.",
        "Notification access is off. Enable it to receive break reminders." to "الإشعارات متوقفة. فعّلها لتلقي تذكيرات الاستراحة.",
        "FocusAI break" to "استراحة FocusAI", "Great work. Take a short break." to "عمل رائع. خذ استراحة قصيرة.", "Focus session complete" to "اكتملت جلسة التركيز",
        "Sort" to "ترتيب", "Sort tasks" to "ترتيب المهام", "My order" to "ترتيبي", "Due date" to "تاريخ الاستحقاق", "Priority" to "الأولوية", "Title" to "العنوان"
    )
)
