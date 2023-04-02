package com.parjapatSanjay1999.calculator.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculatorDataStore(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "calculator"
    )

    companion object {
        private val UN_CALCULATED_EXPRESSIONS_KEY = stringPreferencesKey("un_calculated_expression")
    }

    suspend fun saveUnCalculatedExpressions(expressions: String) {
        context.dataStore.edit { preferences ->
            preferences[UN_CALCULATED_EXPRESSIONS_KEY] = expressions
        }
    }

    val unCalculatedExpressions: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[UN_CALCULATED_EXPRESSIONS_KEY] ?: "[]"
    }
}
