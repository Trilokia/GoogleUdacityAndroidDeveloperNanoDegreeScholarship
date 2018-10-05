package inc.trilokia.popularmovies_2;

import javax.inject.Singleton;

import dagger.Component;
import inc.trilokia.popularmovies_2.api.NetworkModule;
import inc.trilokia.popularmovies_2.ui.activities.MovieDetailsActivity;
import inc.trilokia.popularmovies_2.ui.activities.MovieListActivity;
import inc.trilokia.popularmovies_2.ui.fragments.MovieDetailsFragment;
import inc.trilokia.popularmovies_2.ui.fragments.MovieListFragment;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface NetworkComponent {

    void inject(MovieListActivity movieListActivity);

    void inject(MovieListFragment movieListFragment);

    void inject(MovieDetailsActivity movieDetailsActivity);

    void inject(MovieDetailsFragment movieDetailsFragment);

}