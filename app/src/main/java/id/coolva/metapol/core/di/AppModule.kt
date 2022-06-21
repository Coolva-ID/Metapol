package id.coolva.metapol.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.coolva.metapol.core.data.source.MetapolRepository
import id.coolva.metapol.core.data.source.local.LocalDataSource
import id.coolva.metapol.core.data.source.local.MetapolDao
import id.coolva.metapol.core.data.source.local.MetapolDatabase
import id.coolva.metapol.core.domain.repository.IMetapolRepository
import id.coolva.metapol.core.domain.usecase.MetapolInteractor
import id.coolva.metapol.core.domain.usecase.MetapolUseCase
import id.coolva.metapol.utils.Constants
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context): MetapolDatabase {
        val passphrase: ByteArray = SQLiteDatabase.getBytes(Constants.PASSPHRASE.toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(app, MetapolDatabase::class.java, Constants.DB_NAME)
            .openHelperFactory(factory)
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(database: MetapolDatabase): MetapolDao{
        return database.metapolDao()
    }

    @Singleton
    @Provides
    fun provideRepository(dao: MetapolDao): IMetapolRepository {
        val localDataSource = LocalDataSource(dao)
        return MetapolRepository(localDataSource)
    }

    @Singleton
    @Provides
    fun provideUseCase(repository: MetapolRepository): MetapolUseCase {
        return MetapolInteractor(repository)
    }
}