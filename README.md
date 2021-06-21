# android-itunes-sample
an iTunes client for android – the app searches for movie content within Australia.

## UI and Design

Master and Detail Layout.

It has caters 2 screen types:
1. Screens with 600dp and above will be considered as tablet mode.
It always show the Master List on the left and Details on the right.

2. Anything below 600dp. Initially shows a the master list and
opens details screen on item tap.

I used Glide for asynchronous loading of images.

## Architecture: MVVM

The choice was made by as this is the recommended patter from Google's jetpack architecture components.

It is also a lot easier to construct and follow the Single Responsibility Principle.

1. Model - represents the data layer of application which contains source of data.
2. View - represents the UI layer and listens for state changes from the ViewModel.
3. ViewModel - mediates between the View and Model, and mostly contains the business logic.

### Dependency Injection

Used Android's Hilt to automated dependency Injection

### Navigation

I used the Single Activity architecture and Navigation Component
from Jetpack's architecture component to implement screen navigations.

I also implemeted a Coordinator like pattern for navigation where the activity handles all screen traversal
and listens to navigation changes thru an activity-wide viewModel.

### Programming

I implemented the observer pattern using kotlin coroutines' Flow.

Used kotlin's suspending function feature for easier implementation  
of coroutines for threading and asynchronous task.

## Persistence

I used Android Room to have easier interface to Android's sqlite  
for local data storage.

I implemented repository pattern to centralize the data from both local  
and network sources.

I implemented one of the most common data access strategy to access data:

1. On query return observable local data.
2. Invoke API network call.
3. Save response to local storage.
4. Updates on the local storage are automatically available for the subscribers.

Also used a helper data class (Resource) to encapsulate data according to their states
which makes it easier for views to consume.

### API

Per instruction, iTunes api was used.

Consumed using Retrofit Network Library and mapped data using GSON.

Created a service with 1 function:

1. getMovieByCountry() - which accept a map of key-values pairs like term, country, and media.

By default, search field has "Star" value, so "Star" related movies are initially shown.

### Tests
I made unit testing for both network api call and repository since I
created the data layer first.
