package uk.geekhole.hotreddit.networking.models

data class ApiDataList<T>(val modHash: String, val dist: Int, val children: List<T>?, val after: String?)