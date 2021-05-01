package uk.geekhole.hotreddit.networking.services

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uk.geekhole.hotreddit.networking.models.ApiDataList
import uk.geekhole.hotreddit.networking.models.ApiPost
import uk.geekhole.hotreddit.networking.models.ApiRedditResponse

interface PostsService {

    // Well... yes I guess this is a little long... But it's reused and not duplicating code. Got to love generics.
    @GET("r/Android/hot.json")
    fun getHotPosts(@Query("after") after: String?): Single<Response<ApiRedditResponse<ApiDataList<ApiRedditResponse<ApiPost>>>>>
}