package kr.co.wanted.gongzone.utils

import android.content.Context

class PreferenceManager {
    companion object {
        private const val PREFERENCE_NAME = "GongZone"
        private const val DEFAULT_VALUE_STRING = ""
        private const val DEFAULT_VALUE_BOOLEAN = false
        private const val DEFAULT_VALUE_INT = -1
        private const val DEFAULT_VALUE_LONG = -1L
        private const val DEFAULT_VALUE_FLOAT = -1f

        private fun getPreferences(context: Context)
        = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        /**
         * String 값 저장
         * @param context
         * @param key
         * @param value
         */
        fun setString(context: Context, key: String, value: String) {
            val prefs = getPreferences(context)
            val editor = prefs.edit()
            editor.putString(key, value)
            editor.apply()
        }

        /**
         * Boolean 값 저장
         * @param context
         * @param key
         * @param value
         */
        fun setBoolean(context: Context, key: String, value: Boolean) {
            val prefs = getPreferences(context)
            val editor = prefs.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        /**
         * Int 값 저장
         * @param context
         * @param key
         * @param value
         */
        fun setInt(context: Context, key: String, value: Int) {
            val prefs = getPreferences(context)
            val editor = prefs.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        /**
         * Long 값 저장
         * @param context
         * @param key
         * @param value
         */
        fun setInt(context: Context, key: String, value: Long) {
            val prefs = getPreferences(context)
            val editor = prefs.edit()
            editor.putLong(key, value)
            editor.apply()
        }

        /**
         * Float 값 저장
         * @param context
         * @param key
         * @param value
         */
        fun setFloat(context: Context, key: String, value: Float) {
            val prefs = getPreferences(context)
            val editor = prefs.edit()
            editor.putFloat(key, value)
            editor.apply()
        }

        /**
         * String 값 로드
         * @param context
         * @param key
         * @return
         */
        fun getString(context: Context, key: String)
        = getPreferences(context).getString(key, DEFAULT_VALUE_STRING)

        /**
         * Boolean 값 로드
         * @param context
         * @param key
         * @return
         */
        fun getBoolean(context: Context, key: String)
        = getPreferences(context).getBoolean(key, DEFAULT_VALUE_BOOLEAN)

        /**
         * Int 값 로드
         * @param context
         * @param key
         * @return
         */
        fun getInt(context: Context, key: String)
        = getPreferences(context).getInt(key, DEFAULT_VALUE_INT)

        /**
         * Long 값 로드
         * @param context
         * @param key
         * @return
         */
        fun getLong(context: Context, key: String)
        = getPreferences(context).getLong(key, DEFAULT_VALUE_LONG)

        /**
         * Float 값 로드
         * @param context
         * @param key
         * @return
         */
        fun getFloat(context: Context, key: String)
        = getPreferences(context).getFloat(key, DEFAULT_VALUE_FLOAT)

        /**
         * 키 값 삭제
         * @param context
         * @param key
         */
        fun removeKey(context: Context, key: String) {
            val prefs = getPreferences(context)
            val edit = prefs.edit()
            edit.remove(key)
            edit.apply()
        }

        /**
         * 모든 저장 데이터 삭제
         * @param context
         */
        fun clear(context: Context) {
            val prefs = getPreferences(context)
            val edit = prefs.edit()
            edit.clear()
            edit.apply()
        }
    }
}