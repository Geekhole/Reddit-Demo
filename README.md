# Reddit Demo App

This is taken and modified from a technical test I was once given. Below is the original brief:
- You will build an app that works across a range of device sizes.
- The app will load the current hot submissions from the Android subreddit. You can use https://www.reddit.com/r/Android/hot.json to get posts.
- The posts will be displayed in a list view.
- If the user scrolls to the bottom of the list, the app should load the next page of results.
- If the user taps on a post, they should be taken to that post on reddit.
- Each post should give attribution to the author

## Overview

The main technologies and libraries used in this project are as follows:
- Kotlin
- Dagger & Hilt (Dependency injection)
- Retrofit 2 (API operations)
- Room (Offline storage and caching)
- Rx (Easy management of data flows)
- Glide (Loading thumbnail images on posts)

## Design Decisions
I am using a take on the Repository model. This allows there to be a single source of truth for each type of object downloaded from the API which makes it easily extensible.

Each new model, such as user’s comments etc. would have their own repository that would look after the data including downloading and storing that data in the database.

This can get complicated when you have a lot of interlinked models (e.g a posts have comments and other objects associated with them). However this can be mitigated by creating a repository that uses a Domain model. This would bring together all the information from multiple sources and put it into a single model.

This method also allows for a degree of abstraction meaning that the underlying methods used to download and store the data can be changed without changing the way the rest of the way the app interacts with it.

This model is definitely overkill for such a simple app, but should it need to be extended it gives a lot more flexibility for adding components and making changes.

### Rx
By using a database first approach, we subscribe to data changes in the database. Every time a subscription is done, a network call for the relevant data is fired off (using the doOnSubscribe method) which then saves the data once it is returned. Saving the data then triggers the subscriber to be notified with the newly saved data.

This is particularly good where there are interlinked models, as mentioned above, as it means even if only one part of the model is changed (e.g. a comment added to a post), we can get notified of that change and update the data everywhere in the app, rather than having to refresh the data manually every time we go back to a specific page.


## Future Improvements
- Addition of the Paging Library
- Espresso testing
- Visibility of different subreddits
- View posts within the app
- Potentially move Networking / Database / Main app into separate modules