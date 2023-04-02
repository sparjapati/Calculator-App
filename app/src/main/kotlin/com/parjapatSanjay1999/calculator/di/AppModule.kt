package com.parjapatSanjay1999.calculator.di

import android.app.Application
import androidx.room.Room
import com.parjapatSanjay1999.calculator.data.CalculatorRepositoryImpl
import com.parjapatSanjay1999.calculator.data.datastore.CalculatorDataStore
import com.parjapatSanjay1999.calculator.data.db.CalculationDatabase
import com.parjapatSanjay1999.calculator.data.db.CalculatorDao
import com.parjapatSanjay1999.calculator.domain.CalculatorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCalculationDatabase(app: Application): CalculationDatabase = Room.databaseBuilder(
        app, CalculationDatabase::class.java, CalculationDatabase.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun providesCalculationDao(db: CalculationDatabase): CalculatorDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesCalculatorDatastore(context: Application): CalculatorDataStore {
        return CalculatorDataStore(context)
    }

    @Provides
    @Singleton
    fun providesCalculationRepository(
        dao: CalculatorDao, calculatorDataStore: CalculatorDataStore
    ): CalculatorRepository {
        return CalculatorRepositoryImpl(dao, calculatorDataStore)
    }
}
