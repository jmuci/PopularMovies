# Popular Movies App

Popular Movies is an app developed as part of the [Udacity Android Nanodegree](https://eu.udacity.com/course/android-developer-nanodegree-by-google--nd801).
This is the first of many project. The assignment just provides the requirements of the app with no code given from the beginingso it is up to the developer (me) to make the right design choices.
The app simply displays a list of movies that the user can choose to rank by most popular or to rated.

## Android Setup

This project consists of a single module. Open the app in Android Studio, fire up and Emulator or Device, build the code and intall the APK.

To hit the Movies BD end point, you will need to create an account at their site and get an API key. 
Once you have the API key paste it in the gradle.properties file in the root of your project in the variable PopularMovies_TheMovieDB_API_KEY

----
PopularMovies_TheMovieDB_API_KEY="<your_api_key>"
----

Or on the command line:
----
./gradlew installDebug
---- 

## Architecture 

The app is implemented following the MVP pattern. 
For handling network requests on the background I chose to use RxJava as it is a more convenient approach than Async Tasks. 


## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [TheMovieDB](https://www.themoviedb.org/documentation/api) - The backend API used
* [RxJava1](https://github.com/ReactiveX/RxJava) - Framework for dealing with events and threading.
* [ButterKnife](http://jakewharton.github.io/butterknife/) - Used to bind Android views.
* [OkHttp](http://square.github.io/okhttp/) - Client for Java and Android applications.


## Contributing

This project is cloded for contributions and not maintained. It's for learning and showcase purposes only.



## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* [Udacity Android Nanodegree](https://eu.udacity.com/course/android-developer-nanodegree-by-google--nd801). 
