package com.example.jmucientes.popularmovies.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.jmucientes.popularmovies.di.qualifiers.ViewModelKey;
import com.example.jmucientes.popularmovies.di.scopes.ApplicationScope;
import com.example.jmucientes.popularmovies.viewmodel.MoviesDetailsViewModel;
import com.example.jmucientes.popularmovies.viewmodel.MoviesViewModel;
import com.example.jmucientes.popularmovies.viewmodel.ProjectViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel.class)
    @ApplicationScope
    abstract ViewModel bindMovieViewModel(MoviesViewModel movieDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MoviesDetailsViewModel.class)
    @ApplicationScope
    abstract ViewModel bindMoviesDetailsViewModel(MoviesDetailsViewModel movieDetailViewModel);

    @Binds
    @ApplicationScope
    abstract ViewModelProvider.Factory bindViewModelFactory(ProjectViewModelFactory moviesViewModelFactory);
}