package ua.pt.solapp;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract CityProfileFragment contributeCityProfileFragment();
}