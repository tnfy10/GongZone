package kr.co.wanted.gongzone.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

class Size {
    companion object {
        /**
         * 화면 높이 구하는 메서드
         */
        fun getDisplayHeight(resources: Resources): Int {
            return resources.displayMetrics.heightPixels
        }

        /**
         * 상태바 높이 구하는 메서드
         */
        fun getStatusBarHeight(resources: Resources): Int {
            var result = 0
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

        /**
         * 액션바 높이 구하는 메서드
         */
        fun getActionBarHeight(theme: Resources.Theme): Int {
            val actionBarHeight: Int
            val styledAttributes = theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
            actionBarHeight = styledAttributes.getDimension(0, 0f).toInt()
            styledAttributes.recycle()
            return actionBarHeight
        }

        /**
         * 내비게이션바 높이 구하는 메서드
         */
        fun getNavigationBarHeight(resources: Resources): Int {
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            var deviceHeight = 0
            if (resourceId > 0) {
                deviceHeight = resources.getDimensionPixelSize(resourceId)
            }
            return deviceHeight
        }

        /**
         * DP to PX
         */
        fun dpToPx(context: Context?, dp: Float): Float {
            return if (context != null) {
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
            } else {
                0f
            }
        }
    }
}