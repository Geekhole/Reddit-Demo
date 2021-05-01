package uk.geekhole.hotreddit.global

interface Mapper<INPUT, OUTPUT> {
    fun map(input: INPUT): OUTPUT
    fun mapList(input: List<INPUT>): List<OUTPUT>
}