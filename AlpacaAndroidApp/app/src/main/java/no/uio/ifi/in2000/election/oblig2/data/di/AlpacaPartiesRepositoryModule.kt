package no.uio.ifi.in2000.election.oblig2.data.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.election.oblig2.data.alpacas.AlpacaPartiesDataSource
import no.uio.ifi.in2000.election.oblig2.data.alpacas.AlpacaPartiesRepository
import no.uio.ifi.in2000.election.oblig2.data.alpacas.NetworkAlpacaPartiesRepository
import no.uio.ifi.in2000.election.oblig2.data.votes.AggregatedVotesDataSource
import no.uio.ifi.in2000.election.oblig2.data.votes.IndividualVotesDataSource
import no.uio.ifi.in2000.election.oblig2.data.votes.VotesRepository
import no.uio.ifi.in2000.election.oblig2.data.votes.VotesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlpacaPartiesRepositoryModule {
    @Provides
    @Singleton // one instance for the entire app's lifecycle
    fun provideAlpacaPartiesDataSource(): AlpacaPartiesDataSource {
        Log.d("AlpacaPartiesRepositoryModule", "fun provideAlpacaPartiesDataSource called")
        return AlpacaPartiesDataSource()
    }

    @Provides
    @Singleton
    fun provideIndividualVotesDataSource(): IndividualVotesDataSource {
        return IndividualVotesDataSource()
    }
        @Provides
    @Singleton
    fun provideAggregatedVotesDataSource(): AggregatedVotesDataSource {
        return AggregatedVotesDataSource()
    }

    @Provides
    @Singleton
    fun provideVotesRepository(
        individualVotesDatasource: IndividualVotesDataSource,
        aggregatedVotesDataSource: AggregatedVotesDataSource
    ) : VotesRepository {
        return VotesRepositoryImpl(individualVotesDatasource, aggregatedVotesDataSource)
    }

    @Provides
    @Singleton // one instance for the entire app's lifecycle
    fun provideAlpacaPartiesRepository(
        dataSource: AlpacaPartiesDataSource ,
        votesRepository: VotesRepository,
    ): AlpacaPartiesRepository {
        Log.d("AlpacaPartiesRepositoryModule", "fun provideAlpacaPartiesRepository called")
        return NetworkAlpacaPartiesRepository(dataSource, votesRepository)
    }

}
