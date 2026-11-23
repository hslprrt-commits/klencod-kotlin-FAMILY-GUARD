
package com.family.guard.app

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private val bg = Color.rgb(10, 10, 16)
    private val surface = Color.rgb(20, 19, 29)
    private val surface2 = Color.rgb(29, 26, 41)
    private val purple = Color.rgb(157, 78, 221)
    private val purpleLight = Color.rgb(190, 120, 255)
    private val white = Color.rgb(245, 243, 250)
    private val muted = Color.rgb(155, 150, 169)
    private val green = Color.rgb(65, 190, 125)
    private val border = Color.rgb(55, 45, 70)

    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = bg
        window.navigationBarColor = bg

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            layoutDirection = View.LAYOUT_DIRECTION_RTL
        }

        val scroll = ScrollView(this).apply {
            isFillViewport = true
            clipToPadding = false
        }

        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(24))
        }

        scroll.addView(content)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        root.addView(createBottomNavigation())

        setContentView(root)

        buildDashboard()
    }

    private fun buildDashboard() {
        content.removeAllViews()

        addHeader()
        addWelcomeSection()
        addFamilyStatus()
        addStatistics()
        addQuickActions()
        addRecentActivity()
        addFooter()
    }

    // Header
    private fun addHeader() {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val brand = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        brand.addView(label("family guard", 21f, white, true))
        brand.addView(
            label("FAMILY SAFETY PLATFORM", 9f, muted)
        )

        row.addView(
            brand,
            LinearLayout.LayoutParams(0, -2, 1f)
        )

        val notification = makeIconButton("♢") {
            showMessage(
                "الإشعارات",
                "سيظهر هنا مركز إشعارات العائلة بعد ربط قاعدة البيانات."
            )
        }

        row.addView(notification)

        val profile = makeIconButton("●") {
            showMessage(
                "حساب ولي الأمر",
                "إعدادات الحساب ستتوفر في مرحلة الحسابات."
            )
        }

        val profileParams = LinearLayout.LayoutParams(dp(44), dp(44))
        profileParams.marginStart = dp(8)
        row.addView(profile, profileParams)

        content.addView(row)
    }

    // Welcome
    private fun addWelcomeSection() {
        val title = label("مرحباً بك", 25f, white, true)
        val subtitle = label(
            "إدارة أمان عائلتك تبدأ من هنا.",
            13f,
            muted
        )

        val params = LinearLayout.LayoutParams(-1, -2)
        params.topMargin = dp(28)
        content.addView(title, params)

        val subParams = LinearLayout.LayoutParams(-1, -2)
        subParams.topMargin = dp(5)
        content.addView(subtitle, subParams)
    }

    // Family status card
    private fun addFamilyStatus() {
        val card = cardContainer()

        val top = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val heading = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        heading.addView(
            label("حالة العائلة", 17f, white, true)
        )

        heading.addView(
            label("ملخص حالة الاتصال", 11f, muted)
        )

        top.addView(
            heading,
            LinearLayout.LayoutParams(0, -2, 1f)
        )

        val status = TextView(this).apply {
            text = "● غير متصل"
            textSize = 10f
            setTextColor(muted)
            setPadding(dp(10), dp(7), dp(10), dp(7))
            background = rounded(surface2, 10)
        }

        top.addView(status)
        card.addView(top)

        val divider = View(this).apply {
            setBackgroundColor(border)
        }

        val dividerParams = LinearLayout.LayoutParams(-1, dp(1))
        dividerParams.topMargin = dp(16)
        dividerParams.bottomMargin = dp(14)
        card.addView(divider, dividerParams)

        card.addView(
            label(
                "لم يتم ربط أجهزة العائلة بعد.",
                13f,
                white
            )
        )

        card.addView(
            label(
                "اربط جهاز الطفل لبدء استخدام ميزات FAMILY GUARD.",
                11f,
                muted
            ).apply {
                setPadding(0, dp(7), 0, 0)
            }
        )

        val connect = actionButton("＋  إضافة جهاز طفل")

        val buttonParams = LinearLayout.LayoutParams(-1, dp(46))
        buttonParams.topMargin = dp(16)
        card.addView(connect, buttonParams)

        connect.setOnClickListener {
            showMessage(
                "إضافة جهاز طفل",
                "سيتم ربط هذه الصفحة بنظام دعوات FMG. ستتم عملية الربط بموافقة المستخدم على الجهاز الآخر."
            )
        }

        addCard(card, 20)
    }

    // Statistics
    private fun addStatistics() {
        addSectionTitle("نظرة عامة", "إحصائيات العائلة")

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        row.addView(
            statisticCard("0", "أجهزة مرتبطة", "▣"),
            LinearLayout.LayoutParams(0, -2, 1f)
        )

        val second = statisticCard("0", "تنبيهات جديدة", "♢")
        val params = LinearLayout.LayoutParams(0, -2, 1f)
        params.marginStart = dp(10)
        row.addView(second, params)

        content.addView(row)
    }

    private fun statisticCard(
        number: String,
        title: String,
        symbol: String
    ): LinearLayout {
        val card = cardContainer()

        card.addView(
            label(symbol, 22f, purpleLight, true)
        )

        val numberView = label(number, 27f, white, true)
        val numberParams = LinearLayout.LayoutParams(-1, -2)
        numberParams.topMargin = dp(10)
        card.addView(numberView, numberParams)

        val titleParams = LinearLayout.LayoutParams(-1, -2)
        titleParams.topMargin = dp(4)
        card.addView(
            label(title, 11f, muted),
            titleParams
        )

        return card
    }

    // Quick actions
    private fun addQuickActions() {
        addSectionTitle("الخدمات", "الوصول السريع")

        val actions = listOf(
            Triple("⌖", "الموقع الجغرافي", "إدارة الموقع والمناطق الآمنة."),
            Triple("✉", "الرسائل العائلية", "إرسال واستقبال الرسائل."),
            Triple("◷", "وقت الشاشة", "متابعة أوقات استخدام الجهاز."),
            Triple("▤", "التقارير", "عرض تقارير الاستخدام."),
            Triple("♧", "المهام العائلية", "تنظيم المهام والأنشطة."),
            Triple("⚙", "الإعدادات", "إعدادات العائلة والتطبيق.")
        )

        actions.chunked(2).forEach { group ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            group.forEachIndexed { index, item ->
                val action = actionCard(
                    item.first,
                    item.second,
                    item.third
                )

                val params = LinearLayout.LayoutParams(0, -2, 1f)
                if (index == 1) {
                    params.marginStart = dp(10)
                }

                row.addView(action, params)
            }

            val rowParams = LinearLayout.LayoutParams(-1, -2)
            rowParams.bottomMargin = dp(10)
            content.addView(row, rowParams)
        }
    }

    private fun actionCard(
        icon: String,
        title: String,
        description: String
    ): LinearLayout {
        val card = cardContainer()

        card.addView(
            label(icon, 24f, purpleLight, true)
        )

        val titleParams = LinearLayout.LayoutParams(-1, -2)
        titleParams.topMargin = dp(10)
        card.addView(
            label(title, 13f, white, true),
            titleParams
        )

        val descriptionParams = LinearLayout.LayoutParams(-1, -2)
        descriptionParams.topMargin = dp(5)
        card.addView(
            label(description, 10f, muted),
            descriptionParams
        )

        card.setOnClickListener {
            showMessage(title, description)
        }

        return card
    }

    // Recent activity
    private fun addRecentActivity() {
        addSectionTitle("النشاط", "آخر التحديثات")

        val card = cardContainer()

        val empty = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(10), dp(18), dp(10), dp(18))
        }

        empty.addView(
            label("◷", 30f, purpleLight)
        )

        val titleParams = LinearLayout.LayoutParams(-1, -2)
        titleParams.topMargin = dp(10)

        empty.addView(
            label("لا توجد نشاطات حتى الآن", 14f, white, true).apply {
                gravity = Gravity.CENTER
            },
            titleParams
        )

        val subtitleParams = LinearLayout.LayoutParams(-1, -2)
        subtitleParams.topMargin = dp(6)

        empty.addView(
            label(
                "ستظهر تحديثات الأجهزة والتنبيهات هنا.",
                11f,
                muted
            ).apply {
                gravity = Gravity.CENTER
            },
            subtitleParams
        )

        card.addView(empty)
        addCard(card, 20)
    }

    // Footer
    private fun addFooter() {
        val footer = label(
            "family guard  •  إصدار تجريبي",
            10f,
            muted
        ).apply {
            gravity = Gravity.CENTER
            setPadding(0, dp(18), 0, dp(4))
        }

        content.addView(footer)
    }

    // Section heading
    private fun addSectionTitle(
        title: String,
        subtitle: String
    ) {
        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        column.addView(label(title, 18f, white, true))

        column.addView(
            label(subtitle, 10f, muted).apply {
                setPadding(0, dp(4), 0, 0)
            }
        )

        val params = LinearLayout.LayoutParams(-1, -2)
        params.topMargin = dp(24)
        params.bottomMargin = dp(12)

        content.addView(column, params)
    }

    // Bottom navigation
    private fun createBottomNavigation(): LinearLayout {
        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(8), dp(8), dp(8), dp(8))
            background = rounded(surface, 18)
        }

        val items = listOf(
            "⌂" to "الرئيسية",
            "♧" to "العائلة",
            "⌖" to "الموقع",
            "⚙" to "الإعدادات"
        )

        items.forEachIndexed { index, item ->
            val selected = index == 0

            val itemView = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(4), dp(5), dp(4), dp(5))
                setOnClickListener {
                    if (index == 0) {
                        buildDashboard()
                    } else {
                        showMessage(
                            item.second,
                            "هذه الصفحة ستُفعّل ضمن مراحل تطوير التطبيق."
                        )
                    }
                }
            }

            itemView.addView(
                label(
                    item.first,
                    20f,
                    if (selected) purpleLight else muted,
                    true
                ).apply {
                    gravity = Gravity.CENTER
                }
            )

            itemView.addView(
                label(
                    item.second,
                    9f,
                    if (selected) white else muted
                ).apply {
                    gravity = Gravity.CENTER
                    setPadding(0, dp(3), 0, 0)
                }
            )

            nav.addView(
                itemView,
                LinearLayout.LayoutParams(0, -2, 1f)
            )
        }

        return nav
    }

    // UI helpers
    private fun cardContainer(): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(15), dp(15), dp(15), dp(15))
            background = rounded(surface, 17)
            elevation = dp(2).toFloat()
        }
    }

    private fun addCard(card: View, topMargin: Int) {
        val params = LinearLayout.LayoutParams(-1, -2)
        params.topMargin = dp(topMargin)
        content.addView(card, params)
    }

    private fun actionButton(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            gravity = Gravity.CENTER
            textSize = 13f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            background = rounded(purple, 13)
            isClickable = true
            isFocusable = true
        }
    }

    private fun makeIconButton(
        text: String,
        onClick: () -> Unit
    ): TextView {
        return TextView(this).apply {
            this.text = text
            gravity = Gravity.CENTER
            textSize = 20f
            setTextColor(purpleLight)
            background = rounded(surface2, 13)
            layoutParams = LinearLayout.LayoutParams(dp(44), dp(44))
            setOnClickListener { onClick() }
        }
    }

    private fun label(
        text: String,
        size: Float,
        color: Int,
        bold: Boolean = false
    ): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(color)
            if (bold) {
                typeface = Typeface.DEFAULT_BOLD
            }
            gravity = Gravity.CENTER_VERTICAL
        }
    }

    private fun rounded(
        color: Int,
        radius: Int
    ): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = dp(radius).toFloat()
            setStroke(dp(1), border)
        }
    }

    private fun showMessage(
        title: String,
        message: String
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("حسناً", null)
            .show()
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}
